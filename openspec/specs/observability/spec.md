# observability Specification

## Purpose

锁定 ULP 三个部署单元（ulp-console、ulp-portal、ulp-openapi）对外暴露的健康检查、指标、构建信息端点形态与鉴权边界，确保生产可观测性面（health / liveness / readiness / info / prometheus）稳定可依赖，敏感端点（env / loggers / metrics / mappings）严格鉴权且永不裸奔。

由 openspec change `add-actuator-health`（2026-06-13）首次落地，promote 自该变更的 spec delta。

## Requirements

### Requirement: Actuator 端点暴露白名单基线

所有部署单元（ulp-console、ulp-portal、ulp-openapi）SHALL 在 `application.yml` 中显式声明 `management.endpoints.web.exposure.include`，且仅暴露下述端点白名单：`health`、`info`、`metrics`、`prometheus`、`loggers`、`env`、`mappings`。

MUST NOT 暴露：`heapdump`、`threaddump`、`shutdown`、`startup`、`caches`、`scheduledtasks`、`beans`、`configprops`、`conditions`、`httpexchanges`。

MUST NOT 使用 `*` 通配符暴露全部端点。

#### Scenario: 暴露白名单未含 heapdump
- **WHEN** 评审任一部署单元的 `application.yml`
- **THEN** `management.endpoints.web.exposure.include` 是显式列表，且不包含 `heapdump`、`threaddump`、`shutdown`

#### Scenario: 三个部署单元配置一致
- **WHEN** diff `ulp-console/src/main/resources/application.yml`、`ulp-portal/src/main/resources/application.yml`、`ulp-openapi/src/main/resources/application.yml` 中的 `management.endpoints.web.exposure.include`
- **THEN** 三者声明的端点白名单一致（差异仅允许在 endpoint-specific 配置如 health.show-details 上存在）

#### Scenario: 未在白名单内的端点 404
- **WHEN** 任意客户端对部署单元发起 `GET /actuator/heapdump`
- **THEN** 响应状态码为 404（端点未暴露）

### Requirement: 健康端点分级与 probe 支持

所有部署单元 SHALL 启用 Spring Boot 健康端点的探针子端点（`management.endpoint.health.probes.enabled=true`），暴露 `/actuator/health/liveness` 与 `/actuator/health/readiness`。

`/actuator/health` 默认 `show-details=ALWAYS`（暴露所有组件 status 与详情），便于运维定位故障点；前提是该端点必须通过鉴权策略（见 `security` 边界 Requirement）开放给受信客户端。

`/actuator/health` 主端点 SHALL 仅返回 `UP` / `DOWN` / `OUT_OF_SERVICE` / `UNKNOWN` 四种顶层 status。

#### Scenario: liveness 子端点响应
- **WHEN** 部署单元启动完成后客户端请求 `GET /actuator/health/liveness`
- **THEN** 返回 200 + JSON body `{"status":"UP"}`

#### Scenario: readiness 反映依赖状态
- **WHEN** Redis 不可达（连接超时）
- **THEN** `GET /actuator/health/readiness` 返回 503 + `{"status":"DOWN"}` 且 `/actuator/health` 主端点的 `components.redis.status` 为 `DOWN`

#### Scenario: show-details 暴露 component breakdown
- **WHEN** ADMIN 客户端请求 `GET /actuator/health`
- **THEN** 响应 JSON 至少包含 `components.db`、`components.redis`、`components.liquibase`、`components.liquibaseChangelogDrift` 四个组件 status

### Requirement: Liquibase changelog 漂移自定义健康指标

所有部署单元 SHALL 注册一个 `HealthIndicator` bean 名为 `liquibaseChangelogDrift`，检测运行时 `DATABASECHANGELOG` 表中已执行 changeset 的 checksum 是否与 classpath changelog 文件计算所得的 checksum 一致。

漂移定义：表中存在某 changeset，其 `MD5SUM` 列值与 classpath 上同 `id + author + filename` 的 changeset 当前 checksum 不一致。

检测到漂移时，health status MUST 为 `OUT_OF_SERVICE`（区别于 `DOWN` 的资源不可用），并在 details 中列出至少 changeset id 与 filename。

每次 health 探测无需重新查 DB；indicator MUST 使用 ≥ 5 分钟的内存 cache。

#### Scenario: 无漂移时报 UP
- **WHEN** classpath changelog 与 `DATABASECHANGELOG` 表 checksum 一致
- **THEN** `/actuator/health/liquibaseChangelogDrift` 返回 `{"status":"UP"}`

#### Scenario: 检测到漂移
- **WHEN** 手动修改 `DATABASECHANGELOG` 表中某行 `MD5SUM` 值使其与 classpath 不一致，等待 cache 过期后再请求 health
- **THEN** `/actuator/health/liquibaseChangelogDrift` 返回 `{"status":"OUT_OF_SERVICE","details":{...}}`，且 details 至少含 `driftedChangesets` 数组

#### Scenario: 缓存有效期内不重复查 DB
- **WHEN** 连续 100 次请求 `/actuator/health`（间隔 1 秒）
- **THEN** 对 `DATABASECHANGELOG` 表的 SELECT 查询次数 ≤ 2 次（一次启动初始化 + 一次 cache miss）

### Requirement: build-info 与 git-info 信息暴露

所有部署单元 SHALL 在 Maven 构建时生成 `META-INF/build-info.properties`（通过 `spring-boot-maven-plugin` 的 `build-info` goal）与 `git.properties`（通过 `git-commit-id-maven-plugin`），让 `/actuator/info` 自动暴露构建版本与 git 提交信息。

`/actuator/info` 响应 MUST 包含 `build.version`、`build.time`、`git.commit.id.abbrev`、`git.branch` 四个字段。

`git-commit-id-maven-plugin` MUST 配置 `failOnNoGitDirectory=false`，避免源码包构建（无 `.git` 目录）时失败。

此外，所有部署单元 SHALL 注册一个 `InfoContributor` bean 暴露 ULP 运行时基线版本，contributes namespace 为 `runtime`，至少含 `runtime.springBoot`、`runtime.java`、`runtime.jackson` 三个字段。

#### Scenario: info 端点暴露 build 与 git 信息
- **WHEN** 客户端请求 `GET /actuator/info`
- **THEN** 响应 JSON 包含 `build.version`（与 `pom.xml` 中 `<version>` 一致）、`git.commit.id.abbrev`（7 位短 hash）

#### Scenario: info 端点暴露 runtime baseline
- **WHEN** 客户端请求 `GET /actuator/info`
- **THEN** 响应 JSON 包含 `runtime.springBoot`（以 `4.` 开头）、`runtime.java`（以 `21` 开头）、`runtime.jackson`（以 `3.` 开头）

#### Scenario: 源码包构建不依赖 .git 目录
- **WHEN** 在没有 `.git` 目录的源码 tarball 中执行 `mvnw.cmd clean package -DskipTests=true`
- **THEN** 构建成功（`git-commit-id-maven-plugin` 不致中断构建），且 `git.properties` 字段可为空但文件存在

### Requirement: Prometheus 指标导出

所有部署单元 SHALL 暴露 `/actuator/prometheus` 端点（通过引入 `io.micrometer:micrometer-registry-prometheus`），提供 Micrometer 默认指标集（JVM、HTTP server、HikariCP、Lettuce、Hibernate）。

响应 content-type MUST 为 `text/plain; version=0.0.4`（Prometheus 抓取协议要求）。

至少暴露下列指标：`jvm_memory_used_bytes`、`http_server_requests_seconds_count`、`hikaricp_connections_active`、`lettuce_command_completion_seconds_count`、`hibernate_sessions_open`。

#### Scenario: Prometheus 端点格式正确
- **WHEN** 客户端请求 `GET /actuator/prometheus`
- **THEN** 响应 Content-Type 包含 `text/plain`，body 含至少一行符合 Prometheus exposition format（`<metric_name>{<labels>} <value>`）的指标

#### Scenario: HTTP 请求被纳入指标
- **WHEN** 对部署单元发起任意业务请求后再访问 `/actuator/prometheus`
- **THEN** 响应 body 含 `http_server_requests_seconds_count` 指标且 count > 0

### Requirement: Actuator 端点鉴权边界

所有部署单元 SHALL 在 `SecurityFilterChain` 中显式定义 actuator 端点的鉴权规则：

公开端点（`permitAll()`）：
- `/actuator/health`
- `/actuator/health/**`
- `/actuator/info`
- `/actuator/prometheus`

鉴权端点（要求 ADMIN 角色，Console）或 `denyAll()`（Portal / OpenApi 默认）：
- `/actuator/env`
- `/actuator/loggers`
- `/actuator/loggers/**`
- `/actuator/mappings`
- `/actuator/metrics`
- `/actuator/metrics/**`

未在 expose 白名单的端点由 Spring Boot 直接 404，不依赖 Security 拦截。

#### Scenario: health 端点无需认证
- **WHEN** 未携带任何 Cookie / Authorization header 请求 `GET /actuator/health`
- **THEN** 返回 200（非 401/403）

#### Scenario: env 端点拒绝未鉴权访问
- **WHEN** 未携带认证信息请求任一部署单元的 `GET /actuator/env`
- **THEN** 返回 401（未登录）或 403（已登录但角色不足）

#### Scenario: loggers 端点对普通用户拒绝
- **WHEN** 普通登录用户（非 ADMIN）在 ulp-console 上请求 `GET /actuator/loggers`
- **THEN** 返回 403

#### Scenario: Portal / OpenApi 的鉴权端点默认 denyAll
- **WHEN** 任意客户端（无论是否登录）请求 ulp-portal 或 ulp-openapi 的 `/actuator/env`
- **THEN** 返回 403（这两个服务无 ADMIN 角色概念，默认拒绝）

### Requirement: Docker HEALTHCHECK 集成

部署侧的 Docker 镜像 SHALL 在 `Dockerfile` 中声明 `HEALTHCHECK` 指令，调用本机 `/actuator/health/liveness` 端点判定容器存活。

`docker-compose.yml` 中三个服务段 SHALL 同步声明 `healthcheck` 段，参数与 Dockerfile 一致；启动依赖（如 `depends_on`）MUST 使用 `condition: service_healthy` 而非 `service_started`，确保下游服务等到 ULP 真正可用再启动。

`HEALTHCHECK` interval 推荐 30s，timeout 推荐 10s，retries 推荐 3，start-period 推荐 60s（覆盖 Liquibase 启动期 + Spring Boot 装配）。

#### Scenario: Dockerfile 含 HEALTHCHECK 指令
- **WHEN** 评审 `deploy/docker/ulp-console.Dockerfile`、`deploy/docker/ulp-portal.Dockerfile`、`deploy/docker/ulp-openapi.Dockerfile`
- **THEN** 每个文件含一行以 `HEALTHCHECK` 开头的指令，调用 `/actuator/health/liveness`

#### Scenario: docker-compose 用 service_healthy
- **WHEN** 评审 `deploy/docker/docker-compose.yml`
- **THEN** 任意依赖 ulp-* 服务的下游 `depends_on` 段使用 `condition: service_healthy`
