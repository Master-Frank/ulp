## Why

三个部署单元（ulp-console / ulp-portal / ulp-openapi）虽然在 root `pom.xml` 里已经依赖了 `spring-boot-starter-actuator`，但 application.yml 没有任何 `management.*` 配置：默认只暴露 `/actuator/health`，且仅返回 `status: UP` 而无组件级 details，不足以支撑 k8s readiness / liveness 探针、Prometheus 抓取，以及运维定位 SSO 依赖的连通性问题。另外 Spring Boot 4 + JDK 21 已就位，Phase 10.3 归档时留下的"虚拟线程评估"需要在本期一并定调——是开还是不开都要有书面结论，而不是悬而未决。

## What Changes

- 引入新 capability `observability`，定义 Actuator 端点的暴露策略、健康检查分级（liveness / readiness / 完整 health）、自定义 HealthIndicator（Liquibase changelog 漂移）、InfoContributor（build-info / git-info / runtime baseline 版本）、Micrometer Prometheus endpoint
- 三个部署单元 application.yml 加 `management.*` 配置：暴露 health、info、metrics、prometheus、loggers、env、mappings；health 显示 component breakdown；敏感端点鉴权或隔离
- 三个 SecurityConfiguration（Console / Portal / OpenApi）显式 permit `/actuator/health`、`/actuator/health/**`、`/actuator/info`、`/actuator/prometheus`；其余 actuator 端点要求 ADMIN 角色（或后续切换到独立 management port）
- Maven 接入 `spring-boot-maven-plugin` 的 `build-info` goal + `git-commit-id-maven-plugin`（如未启用），让 `/actuator/info` 自动暴露版本与提交哈希
- 自定义 `LiquibaseChangelogHealthIndicator`：检测 DATABASECHANGELOG 与 classpath changelog 是否一致，漂移则报 DOWN
- 部署侧：`deploy/docker/*.Dockerfile` 增加 `HEALTHCHECK` 指令调用 `/actuator/health/liveness`；`deploy/docker/docker-compose.yml` 给三个服务加 healthcheck 段
- 扩展 `runtime-baseline` spec：评估 `spring.threads.virtual.enabled` 与 Hibernate 7 / Lettuce / Spring Session 的兼容性，并把"启用 / 暂不启用"的结论写入 spec 的 Requirements，避免后续 PR 反复争论

## Capabilities

### New Capabilities
- `observability`: Spring Boot Actuator 端点暴露策略、健康检查分级、自定义 HealthIndicator / InfoContributor 接入规则、指标系统（Micrometer + Prometheus）契约

### Modified Capabilities
- `runtime-baseline`: 新增关于虚拟线程开关的 Requirement——明确本期评估结论（启用 / 暂不启用 + 理由），并锁入 baseline 以防止未来 PR 静默切换

## Impact

- **代码**：
  - `ulp-support/src/main/java/cn/frank/ulp/support/observability/`（新包）：`LiquibaseChangelogHealthIndicator`、可能的 `RuntimeBaselineInfoContributor`
  - `ulp-console / ulp-portal / ulp-openapi` 的 `*SecurityConfiguration.java`：在 `authorizeHttpRequests` lambda 中追加 actuator 路径
  - 三处 `application.yml`：新增 `management.endpoints.web.exposure.include`、`management.endpoint.health.show-details`、`management.endpoint.health.probes.enabled`、`management.info.*`、`management.metrics.*`
- **依赖**：
  - 可能新增：`io.micrometer:micrometer-registry-prometheus`（Prometheus endpoint）
  - 启用 `spring-boot-maven-plugin` 的 `build-info` execution
  - 可选 `pl.project13.maven:git-commit-id-maven-plugin`
- **部署**：
  - `deploy/docker/ulp-console.Dockerfile` / `ulp-portal.Dockerfile` / `ulp-openapi.Dockerfile`：新增 HEALTHCHECK
  - `deploy/docker/docker-compose.yml`：三个服务段加 healthcheck
- **配置 / 文档**：
  - `CLAUDE.md` 的 "Configuration that's easy to get wrong" 段落补一条 actuator 鉴权陷阱
  - `README.md` 部署章节补健康检查 URL（`/actuator/health` × 3 端口）
- **测试**：
  - 各模块 IT 加一条 actuator 烟测：未鉴权访问 `/actuator/health` 返回 200 + `UP`；访问 `/actuator/env` 返回 401/403
- **风险**：
  - 默认敏感端点（env / loggers / threaddump / heapdump）若漏配会泄露内存/环境变量；security 路径放行规则必须三个 SecurityConfiguration 全部覆盖，不能漏一个
  - 虚拟线程若仓促开启，Hibernate 7 同步 EMF + Lettuce pinning 可能引入难复现的死锁，本期采取"评估并记录决策"的保守策略
