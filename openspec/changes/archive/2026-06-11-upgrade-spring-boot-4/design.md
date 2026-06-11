## Context

ULP 是一个 Spring Boot 3.2.12 monorepo：12 个一级模块，部分二级模块（`ulp-protocol/*`、`ulp-application/*`、`ulp-identity-source/*`），约 1500 个 Java 文件，groupId `cn.frank.ulp` v1.1.0。三个可运行应用：console（1898）、portal（1989）、openapi（1988）。底层 MySQL 8 + Redis 7（Liquibase + Spring Session indexed-mode）。

测试面：5 个单元测试（ulp-support）+ 4 个 IT 类 13 个方法（OIDC × 4、UserController × 3、AppController × 3、OrganizationController × 3）—— #36 在 2026-06-10 刚建立。

约束：
- 构建在 Windows 上用 `mvnw.cmd`，跨平台必须保持
- 现有依赖锁定包含若干自管 override，每个都有上下文（commons-lang3 override 是因 testcontainers ArrayFill），不能盲目删
- Liquibase changelog 已含 29 张 `ulp_*` 表，schema 不能动
- 美术资源（logo / favicon）2026-05-28 完成替换，不能因升级牵连改动
- 用户偏好"详细推理 + 透明假设 + 直说问题"，本设计每个 decision 都标"被驳回方案"

## Goals / Non-Goals

**Goals:**
- 一份 clean checkout 上 `mvnw.cmd clean verify` 全绿，13 个 IT 全过、5 个单测全过
- 三个应用本地启动并通过基本烟测：console 登录 + 用户 CRUD、portal 跳 console SSO、openapi 调用 `/api/v1` 任一接口
- `runtime-baseline` capability 落定，未来任何降版本 / 换栈必须改 spec
- Java 21 启用后，至少把 IT 套件迁到 virtual thread 验证可用（不要求生产代码改）
- 文档：README 升级"运行时要求"章节（Java 21 / Boot 4 / Docker）

**Non-Goals:**
- 升 4.1（未 GA）
- 升 Spring Cloud（除 spring-cloud-context BOM 跟随 Boot 4 同步 BOM 升级外，不引入新 cloud 模块）
- 重写 SecurityConfig 拓扑 —— 沿用现有 filter chain 拆分，只做 7.x DSL 改写
- 引入新功能 / 重构业务代码
- CI 集成（仍在本地跑）
- 性能基准对比 3.2 vs 4.0（人力有限，等 Actuator #39 上线后基于 Prometheus 自然采集）

## Decisions

### D1. 升到 4.0.x 最新 patch，不跳 4.1

**理由：** 4.1 计划 2026-11 GA，目前 milestone 阶段，breaking change 文档不全。4.0.x 支持到 2026-12-31，留出 5 个月做后续 4.0 → 4.1 mechanical bump。一次跨两个 major 风险不可控。

**被驳回的方案：**
- **跳 4.1 milestone：** 不接受 milestone / RC 进生产基线
- **停在 3.5.x 续命：** 3.5 EOL 2026-06-30，19 天后失效；只能等 HeroDevs NES 商业续期（不接受付费 + 治标）
- **多版本并存（console 升、portal 不升）：** 共享 ulp-support / ulp-common / ulp-protocol 跨进程，BOM 必须一致

### D2. 同步升 Java 17 → 21

**理由：** Boot 4.0 允许 17，但 4.1 强制 21。把两个升级合并到本次：
- 一次 IT 回归覆盖两次变更；分开做要付两次 IT 调试成本
- Java 21 virtual thread / pattern matching for switch / sealed type 已成熟，IDE / Maven / Lombok 全链路稳定
- Lombok 1.18.32+ 已正式支持 Java 21（写本设计时 1.18.34）

**风险：**
- `--add-opens` JVM 参数可能要补（ITs 跑时观察 illegal reflective access 警告）
- 部分老库（如 reflections 0.10.2）在 Java 21 下可能报 ASM 版本不够，需 bump

**被驳回的方案：**
- **保留 17 直到 4.1 强制：** 等于 6 个月内再走一次 IT 回归，纯重复劳动
- **跳到 22 / 23 EA：** 非 LTS，不接受

### D3. Jackson 3 迁移用 "包名 sed + 编译驱动" 而非 "一次性手改"

**理由：** Jackson 包从 `com.fasterxml.jackson` 改成 `tools.jackson`（注解仍是 `com.fasterxml.jackson.annotation`）。本仓库 import 该包的文件估计 100+ 个。

**策略：**
1. 全仓 grep `com.fasterxml.jackson` 区分注解 vs 非注解
2. 非注解 import 用 IDEA Structural Replace 批量改 `com.fasterxml.jackson.databind.*` → `tools.jackson.databind.*` 等
3. 编译，按 IDE 报错单点修
4. 单元测试 + IT 套件确认运行时行为
5. 重点排查：`UserDetailsDeserializer`（本次升级已知会被 SAS 1.5 内部触发的反序列化路径影响）、Spring Session `Jackson2JsonRedisSerializer` 配置类、OAuth2 client 的 Authorization 持久化

**被驳回的方案：**
- **OpenRewrite recipe：** Spring 官方推出了 OpenRewrite migration recipe，但要单独搭 OpenRewrite Maven plugin。本项目规模小、手工 sed + IDE refactor 比集成 OpenRewrite 更快
- **保留 jackson 2.x（用 BOM 强制 pin）：** Boot 4 部分内部代码（如 `JsonMapperBuilderCustomizer`）已切到 3.x API，强 pin 会带来更糟的双版本共存

### D4. Spring Security 7 DSL 改写按 "每个 SecurityFilterChain 单独 PR commit"

**理由：** 本仓库目前有至少 4 个 SecurityConfig（console、portal、openapi、authorization server 自定义），每个 filter chain 拓扑独立。Security 7 主要变化：
- `requestMatchers(antMatchers(...))` → `requestMatchers("/path/**")`（简化）
- `csrf().disable()` lambda DSL 形式 → `csrf(csrf -> csrf.disable())` 仍可用，但建议统一改 `AbstractHttpConfigurer::disable` 方法引用形式
- `authorizeHttpRequests(...)` 已是 6.x 默认，无变化

**策略：** 每个 SecurityConfig 单独一个 commit，commit message 列出本 chain 涵盖哪些 URL、改了哪些 DSL 调用，便于回滚定位。

**被驳回的方案：**
- **全部 SecurityConfig 一个 commit：** 出问题难定位
- **保留 6.2 DSL（用 deprecation 警告抑制注解）：** 7.x 已 remove 部分 6.x 的 deprecated method，不只是 warning

### D5. Liquibase 5 升级前先全量 dry-run

**理由：** Liquibase 5 主要变化在 driver 接口与命令行行为，changelog DSL 兼容；但 ULP 用了 MySQL JSON 列 + utf8mb4，需 verify。

**策略：**
1. 升 Boot 4 之前先用 standalone Liquibase 5 CLI 对 changelog 跑 `validate` + `update-sql`（不写库，只生成 SQL）
2. 对比 4.x 生成的 SQL，确认 DDL 输出无差异
3. 升 Boot 4 后第一次 IT 跑 Liquibase 时观察日志
4. 出问题就 pin liquibase-core 到 4.x 最新（Boot 4 BOM 允许 override），单独排查再升

**被驳回的方案：**
- **依赖 Boot 4 BOM 隐式升级，不做 dry-run：** Liquibase 是数据持久化的命门，IT 跑炸已经晚了
- **保持 4.x 永不升：** Boot 4 BOM 推荐 5.x，长期保留 4.x 等于自己背 BOM 偏离

### D6. application.yml 属性迁移交由 spring-boot-properties-migrator 临时辅助

**理由：** Boot 4 提供 `spring-boot-properties-migrator` 模块，临时加到 dependencies 后启动应用会输出"old key X 已迁到 new key Y"日志，准确率 100%。

**策略：**
1. 升 Boot 4 后第一次启动 console，临时加 `spring-boot-properties-migrator`
2. 收集所有迁移建议
3. 改 yml
4. 移除 `properties-migrator` 依赖（生产不留）
5. 三个应用都重复一遍

**被驳回的方案：**
- **手工 grep yml 对照迁移文档：** 容易漏，自动工具准确

### D7. 升级顺序：基础 → 编译 → 运行 → 业务验证

固定 12 阶段顺序：
1. POM 升 parent + Java 21
2. 一遍 `mvnw.cmd clean compile -Dlicense.skip=true`，按报错改 import
3. Jackson 3 包名 sed
4. Spring Security 7 DSL 逐 SecurityConfig 改写
5. Spring Session 属性键迁移
6. Hibernate 7.1 配置核对（Dialect、processor）
7. Liquibase 5 dry-run + 升级
8. 三方依赖核对（springdoc / redisson / jib / MapStruct）
9. `mvnw.cmd clean verify` 全套 IT 跑通
10. 三个应用本地启动 + 烟测
11. POM override 清理（不再需要的删掉）
12. README + ChangeLog + tasks.md 更新

**理由：** 把"无法编译"问题（阶段 1-2）和"编译过但运行错"问题（阶段 3-8）严格分开，每阶段都能 commit 一个状态可观察的 baseline，出问题 git bisect 颗粒小。

**被驳回的方案：**
- **从业务测起，最后修编译：** 编译都过不去测什么
- **并行 Jackson + Security：** 互相干扰，定位问题困难

### D8. `runtime-baseline` capability 形态

新建 spec，包含三类 requirement：
- **框架版本基线**：Spring Boot major.minor、Spring Framework major、Spring Security major、Spring Authorization Server major.minor
- **运行时基线**：JVM 大版本（21）、Servlet API 版本（6.1）、Jakarta EE 版本（11）
- **关键依赖基线**：Jackson 大版本（3）、Hibernate 大版本（7）、Liquibase 大版本（5）

每条 requirement 都有"降级被禁止"的 scenario，确保未来 PR 改这些版本必须改 spec、过审批。

## Risks / Trade-offs

- **[HIGH] Jackson 3 漏改导致运行时 NPE / ClassNotFound：** IT 套件覆盖 OIDC 主路径但不覆盖所有 Jackson 序列化场景（如 audit log 写 Redis、用户偏好持久化）。**缓解：** 完成升级后强制要求三个应用都做一次"完整用户操作流"手工烟测，记录在 tasks.md `7.x` 节
- **[MEDIUM] Spring Authorization Server 1.5.x 行为变化导致 OIDC IT 失败：** SAS 1.5 同 1.4 比有 token introspection / DPoP / OIDC client registration endpoint 行为调整。**缓解：** OIDC IT 是第一个跑的回归用例，发现行为变化第一时间评估是否影响生产 OIDC flow，必要时降 SAS 版本配合
- **[MEDIUM] Java 21 + Lombok + MapStruct triple 兼容：** 三者同时升级（Lombok ≥1.18.32 + MapStruct ≥1.6 + lombok-mapstruct-binding ≥0.2.0），任何一个版本不对会编译失败。**缓解：** 已查证当前 pom 的 MapStruct 1.6.3 + lombok-mapstruct-binding 0.2.0 与 Java 21 + Lombok 1.18.34 兼容
- **[LOW] commons-lang3 override 移除后回归：** 升 testcontainers 后理应不需要 override，但 testcontainers 1.21 之外的 OCI runtime 可能仍触发 ArrayFill。**缓解：** 阶段 11 删 override 后必须跑完整 IT，遇到 NoSuchMethodError 即立刻恢复
- **[LOW] Liquibase 5 driver 行为变化导致 changelog 校验失败：** 已有 D5 dry-run 缓解
- **[权衡] 一次性升 Java + Boot + 一堆 framework：** 影响面大、回滚成本高。但拆分会带来 (a) 多次 IT 套件回归、(b) 中间状态不能 release（4.0 + Java 17 不是稳定 baseline）、(c) 同样的代码改两次。综合判断**合并更划算**

## Open Questions

- **springdoc-openapi 是否需要升到 2.7+？** 当前 2.6.0，Boot 4 兼容性需 verify。如果 2.6 在 Boot 4 跑通就不升；不通则评估 2.7 或 springdoc-openapi 3.x（如已 GA）
- **redisson 3.40.2 与 Spring Session 4 兼容性？** Redisson 主要做分布式锁，与 Spring Session 不直接相关，但都接 Redis；理论无冲突，实测确认
- **dingtalk / wework / aliyun-sdk 等三方 SDK 内部是否打包了 Jackson 2？** 如有 shaded jackson 2 进 fat jar 会出现双版本，需 verify 后决定是否给这些依赖 `<exclusions>`
- **Spring Cloud Context 4.2 在 Boot 4 下行为：** spring-cloud-context 升 5.x 会自动跟随，但 ULP 用得很轻（只有 `@RefreshScope` 极少几处），不预期问题
