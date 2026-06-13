## Context

ULP 的三个部署单元（ulp-console:1898 / ulp-portal:1989 / ulp-openapi:1988）目前依赖了 `spring-boot-starter-actuator`，但 application.yml 没有任何 `management.*` 配置。Spring Boot 默认仅暴露 `/actuator/health`，且 `show-details=NEVER`、`probes.enabled=false`、不暴露 info / metrics。生产侧观察能力等于零：

- k8s 探针只能拿到 `{"status":"UP"}`，无法区分 DB 挂了还是 Redis 挂了
- Prometheus 没有抓取入口
- 部署版本无从查询（`/actuator/info` 空）
- 运维改日志级别需要重启
- 没有自定义 HealthIndicator，覆盖不到 Liquibase changelog 漂移这种已知风险点

同时 Spring Boot 4 / JDK 21 已就位，Phase 10.3 归档时遗留的"虚拟线程评估"必须在本期定调。Hibernate 7 仍以 synchronized 块管理 `Session` 生命周期、Lettuce 在 Netty event loop 上对 `ThreadLocal` 与 synchronized 仍有 pinning 风险、Spring Session Redis 在 indexed repository 模式下也用了 synchronized——三块叠加在虚拟线程下都可能导致 carrier thread 被 pin，需要给出书面结论而非悬而未决。

Stakeholders：
- 部署/SRE：要 health probe + Prometheus
- 安全：要 actuator 敏感端点必须鉴权或隔离
- 后端：要 build/git info 暴露便于排查"线上跑的是哪个 commit"
- Frank：要虚拟线程结论落 spec，避免后续 PR 反复重提

## Goals / Non-Goals

**Goals:**

- 三个部署单元统一暴露 health（含 liveness / readiness 子端点）、info、metrics、prometheus、loggers、env、mappings
- `/actuator/health` 公网放行（适配 k8s probe / docker HEALTHCHECK 不带认证调用）
- 其余 actuator 端点（env / loggers / threaddump / heapdump / mappings 等）必须鉴权
- 自定义至少一个业务相关 HealthIndicator：`LiquibaseChangelogHealthIndicator`
- `/actuator/info` 自动包含构建版本（`build.version`、`build.time`）+ git commit hash（`git.commit.id.abbrev`、`git.branch`）+ ULP runtime baseline 摘要（Spring Boot / Java / Jackson 主版本）
- `/actuator/prometheus` 暴露 Micrometer 指标（HTTP、JVM、DataSource、Lettuce、Hibernate）
- Dockerfile + docker-compose 集成 `/actuator/health/liveness` HEALTHCHECK
- 虚拟线程评估结论写入 `runtime-baseline` spec，无论开或不开都明示理由
- IT 增加 actuator 烟测：health 200 / 敏感端点 401

**Non-Goals:**

- 不引入独立 management port（暂保留单端口，理由见 Decision 1）
- 不接入 Grafana dashboard / Prometheus server / Alertmanager（仅暴露指标，告警平台另开 change）
- 不实现外部 SSO（DingTalk / Feishu / WeCom OpenAPI）连通性 HealthIndicator（理由：这些调用经常受厂商侧限流，纳入 health 会让 readiness 假阴性误触发 k8s 重启，应放在监控告警侧）
- 不接入 distributed tracing（Micrometer Tracing / Zipkin / OpenTelemetry 另开 change）
- 不改造现有日志框架（logback-spring.xml 保持不动，只通过 `/actuator/loggers` 提供运行时改级别能力）
- 不为 Argon2id（#25）让路——本 change 不修改密码哈希相关代码

## Decisions

### Decision 1: 单端口 vs 独立 management port

**选：单端口（沿用应用主端口 1898 / 1989 / 1988）**

候选：
- A. 单端口 + URL 鉴权（permit `/actuator/health`，其余要求 ADMIN）
- B. 独立 management port（如 9898 / 9989 / 9988），单独 SecurityFilterChain
- C. 完全独立 management context path（同端口但 `management.endpoints.web.base-path=/admin`）

理由选 A：
- ULP 已经是 3 个独立进程，再加 3 个 management port = 6 端口，部署/反代/防火墙规则膨胀
- k8s probe 需要直连，独立端口意味着 Service 要多暴露一个 port
- 现有 SecurityConfiguration 已经在用 `requestMatchers("/actuator/health/**").permitAll()` 类的语法管业务路径，actuator 路径加几行 permit + ADMIN 是同套机制，运维心智一致
- 后续若运维侧确实需要隔离，独立端口可在不修改 spec 的前提下加（management.server.port 是配置项），属于运行时调参而非架构变更
- 缺点：DOS 攻击面与业务共端口；mitigation：rate limit / nginx 层挡

### Decision 2: actuator 端点暴露白名单

**选：显式 include，不用 `*`**

`management.endpoints.web.exposure.include` 设为：`health, info, metrics, prometheus, loggers, env, mappings`

不暴露：`heapdump`、`threaddump`、`shutdown`、`startup`、`caches`、`scheduledtasks`、`beans`、`configprops`、`conditions`、`httpexchanges`

理由：
- `heapdump` / `threaddump` 输出可能含敏感信息（DB 密码、redis 密码、session token 等都可能在内存对象里），暴露后即便鉴权也是高风险
- `shutdown` 直接关进程，永不开启
- 其余非必需的端点能不暴露就不暴露，最小暴露面
- `loggers` 与 `env` 暴露但**严格鉴权**——这两个在运维场景下太常用（改日志级别 / 验证配置生效），完全屏蔽反而推高运维成本

### Decision 3: actuator 鉴权策略

**选：分级鉴权**

- 公开（permitAll）：`/actuator/health`、`/actuator/health/liveness`、`/actuator/health/readiness`、`/actuator/info`、`/actuator/prometheus`
- 鉴权（要求登录 + ADMIN 角色）：`/actuator/env`、`/actuator/loggers`、`/actuator/loggers/**`、`/actuator/mappings`、`/actuator/metrics`、`/actuator/metrics/**`

理由：
- `/actuator/health` 必须公开（k8s probe 不带 token）
- `/actuator/info` 仅暴露版本/构建信息，无敏感数据，公开便于诊断
- `/actuator/prometheus` 公开是常规做法（Prometheus server 通常不做认证拉取，鉴权由网络层做）。若后续接生产 Prometheus 需要 token，可改为 Bearer Token 拦截器
- `/actuator/env` 默认对密码/secret 已做 `******` 脱敏（Spring Boot `SanitizingFunction`），但仍可能泄露非敏感配置结构，要求 ADMIN
- `/actuator/loggers` 改日志级别是运维操作，必须 ADMIN

Console 已有 `ADMIN` 角色，Portal / OpenApi 没有独立 admin 角色概念——Portal / OpenApi 的非公开端点直接 `denyAll()`（运维改这俩用 Console 的鉴权代理或临时下线服务）

### Decision 4: 自定义 HealthIndicator 范围

**选：本期仅落 `LiquibaseChangelogHealthIndicator`**

候选：
- A. 仅 Liquibase
- B. Liquibase + DingTalk/Feishu/WeCom OpenAPI 连通性
- C. Liquibase + Redis Lettuce 池容量 + DB 慢查询率

理由选 A：
- Spring Boot 已经内置 `DataSourceHealthIndicator` / `RedisHealthIndicator` / `LiquibaseHealthIndicator`（仅检测连接 + 是否能读 changelog），不重复造
- 自研 `LiquibaseChangelogHealthIndicator` 检测的是**漂移**：classpath 上的 changelog file checksum 与 `DATABASECHANGELOG` 表里记录的 checksum 不一致，则报 OUT_OF_SERVICE。这是 Spring Boot 内置没覆盖的、真实发生过的运维风险（手动改 DB 后忘了写 changelog）
- B 的外部 OpenAPI 检测会受厂商限流 / 临时网络抖动影响，纳入 readiness 会让 k8s 频繁 restart，应该走监控告警而非 health probe（Non-Goals 已说明）
- C 偏向 metric 维度，应该走 Micrometer 而非 HealthIndicator

### Decision 5: build-info / git-info 接入方式

**选：`spring-boot-maven-plugin` 的 `build-info` execution + `git-commit-id-maven-plugin`**

- `build-info`：Spring Boot 官方 plugin 的标准 goal，生成 `META-INF/build-info.properties`，Actuator 自动暴露
- `git-commit-id-maven-plugin`：行业标准，生成 `git.properties`，Actuator 自动暴露

无须自写 `InfoContributor`。`RuntimeBaselineInfoContributor`（暴露 SB / Java / Jackson 版本）需要写一个 Spring bean，但这部分逻辑 < 30 行，价值高（线上排查"这个 commit 跑的是哪个 SB 版本"），可写

### Decision 6: 虚拟线程开关

**选：本期暂不启用，但写入 runtime-baseline spec 锁定"评估完毕、暂不启用"的状态**

评估 `spring.threads.virtual.enabled=true` 在 SB4 + Hibernate 7 + Lettuce 6.x + Spring Session Redis 下的兼容性：

1. **Hibernate 7 同步 EMF 装配**：openspec 已经记录过 `bootstrap-mode: default`（不能用 `deferred`）。同步装配本身与虚拟线程无冲突，但 `Session` 生命周期 + `EntityManager` 大量用 synchronized 块，业务高并发下虚拟线程在 synchronized 上 pin 到 carrier thread，等价于退化为平台线程
2. **Lettuce 6 + Netty event loop**：Lettuce 客户端基于 Netty event loop，event loop 本身就是少量平台线程；虚拟线程调用 Lettuce 时被 park 到 event loop 等待回调，无 pinning 问题。但若业务在虚拟线程里持有 `LettuceConnection` 跨多个命令，仍可能间接 hold 平台资源
3. **Spring Session Redis Indexed**：内部用了 `synchronized` 保护 session repository 状态，与 Hibernate 同类风险
4. **ULP 业务场景**：当前 QPS 没到平台线程瓶颈（典型 IAM 流量是阵发性登录，不是持续高并发 API 调用），开虚拟线程的收益小于不可预测的稳定性回归

结论：本期 `spring.threads.virtual.enabled=false`（默认即 false）；在 `runtime-baseline` spec 加一条 Requirement 记录此决定 + 重启评估的触发条件（QPS > 500 持续、或 Hibernate 8 / Lettuce 7 修了 pinning）

**2026-06-13 评估实测补充**：

- 操作：在 `ulp-console/src/main/resources/application.yml` 临时加 `spring.threads.virtual.enabled: true`，跑 `mvnw verify -pl ulp-console -Dit.test=*IT`
- 范围：4 个 IT 类共 15 个测试方法（ActuatorSecurityIT × 6 + OrganizationControllerIT + UserControllerIT × 3 + AppControllerIT × 3），覆盖 Spring Security 7 filter 链路、JPA/Hibernate 7 事务、MySQL JDBC、Redis Lettuce（actuator/health 探针）、MapStruct、Liquibase 启动、Spring Session Indexed
- 结果：15/15 全绿（1:11 min），Spring boot 启动正常，未观察到死锁；日志无 `pinning detected` / `deadlock` 字样
- **限制**：（1）`-Djdk.tracePinnedThreads=full` 未配进 Failsafe forked JVM argLine，pinning 未真正 trace，仅说明"未明显死锁"≠"无 pinning"；（2）只跑了 ulp-console，ulp-portal / ulp-openapi 未验；（3）单线程 MockMvc 串行调用，未触发高并发竞争；（4）SSO 协议链路（OIDC AS / SAML / form-fill）目前没有 IT 覆盖

最终结论维持**本期不启用**——已知风险范围确实有缩小（基本 IT 路径在虚拟线程下可正常运行），但生产级别的并发 + pinning 压力测试缺位，本期不冒险。`spec.md` 维持"暂不启用 + 后续 PR 启用时同步改 spec"的约束，且约束语气可略放宽（实测有正面信号，不再是纯纸面评估）。

### Decision 7: Liquibase changelog 漂移如何检测

**选：启动期 hook + 定期 indicator 复查**

`LiquibaseChangelogHealthIndicator` 实现：

```java
@Component("liquibaseChangelogDrift")
public class LiquibaseChangelogHealthIndicator implements HealthIndicator {
    // 1. 启动期 Liquibase 已自动校验（与 DATABASECHANGELOG 表对比 checksum），失败直接启动失败
    // 2. 本 indicator 复查"运行时是否有人手动改了 DATABASECHANGELOG 表"
    // 3. 查询 SELECT MD5SUM, FILENAME FROM DATABASECHANGELOG ORDER BY ORDEREXECUTED DESC LIMIT N
    //    与 cached startup snapshot 对比；不一致 → OUT_OF_SERVICE 并附 diff
}
```

复用 Spring Boot 自带的 `LiquibaseEndpoint` API 拿 changelog metadata，不重新解析 changelog xml。

## Risks / Trade-offs

- **风险**：`/actuator/info` 暴露 git commit hash，理论上让攻击者更容易匹配漏洞库到具体 commit
  → **Mitigation**：内网/受信 IP 才公开；公开互联网部署默认关闭 `/actuator/info` 公开放行（这条由部署文档说明，spec 不强制）
- **风险**：`/actuator/prometheus` 公开后被恶意爬虫高频抓取（指标量级在 KB~MB 级，频繁抓影响性能）
  → **Mitigation**：反代/nginx 层 rate limit；本 change 不内置 rate limit（架构层职责）
- **风险**：自定义 `LiquibaseChangelogHealthIndicator` 每次健康检查都查 DB（额外负载）
  → **Mitigation**：cache 5 分钟；k8s readiness 默认 10s 间隔，5min cache 完全足够
- **风险**：三个 SecurityConfiguration 改动遗漏其中一个，导致敏感端点裸奔
  → **Mitigation**：写一条 IT 同时跑三个服务，断言 `/actuator/env` 必须 401/403；并在 PR review checklist 加一条
- **Trade-off**：单端口 vs 独立 management port——选了单端口图简单，未来若有强制审计要求需切到独立端口（属于配置级调整，spec 不锁端口结构）
- **Trade-off**：虚拟线程暂不启用——放弃了潜在的 QPS 提升，换稳定性。如果 ULP 实际成为高 QPS 网关，需要重开 evaluation
- **Trade-off**：build/git info plugin 增加 maven 构建步骤（~1s），换得线上排查能力大幅提升

## Migration Plan

按 tasks.md 的 5 个阶段顺序执行，每阶段独立 commit、独立可回滚：

1. 阶段 1（Actuator 基础接入）出问题 → revert commit，actuator 退回默认行为
2. 阶段 3（Security 放行）出问题 → revert 后业务安全策略回到接入前状态，actuator 端点退回 Spring Boot 默认拒绝
3. 阶段 5（虚拟线程评估）若引入虚拟线程并出问题 → application.yml 把 `spring.threads.virtual.enabled` 改回 `false` 即可（不重启进程也可通过 `/actuator/env` 改但需 ADMIN）

回滚不需要 DB 操作，纯代码 + 配置层面。

## Open Questions

1. `/actuator/prometheus` 公开默认开启，还是默认关闭由部署侧开启？建议默认开启，部署 readme 明确"公网部署需用 nginx 鉴权"。
2. `git-commit-id-maven-plugin` 在离线构建（无 `.git` 目录的源码包发布）下会失败——是否要 fail-on-no-git-directory=false？建议设 false，避免源码 tarball 构建挂掉。
3. `LiquibaseChangelogHealthIndicator` 检测频率（cache TTL）是否要配置化？建议先硬编码 5 分钟，后续若运维有诉求再开放。
