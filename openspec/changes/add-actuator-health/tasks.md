## 1. Actuator 基础接入（端点暴露 + 健康分级）

- [x] 1.1 在 root `pom.xml` 加 `io.micrometer:micrometer-registry-prometheus` 依赖（已有 actuator starter，仅缺 Prometheus registry）
- [x] 1.2 三个部署单元（ulp-console / ulp-portal / ulp-openapi）`application.yml` 新增 `management.endpoints.web.exposure.include: health, info, metrics, prometheus, loggers, env, mappings`
- [x] 1.3 三个 `application.yml` 新增 `management.endpoint.health.show-details: always` + `management.endpoint.health.probes.enabled: true`
- [x] 1.4 三个 `application.yml` 新增 `management.info.env.enabled: true`、`management.info.build.enabled: true`、`management.info.git.enabled: true`、`management.info.git.mode: full`
- [x] 1.5 `mvnw.cmd clean compile -Dlicense.skip=true` 全模块通过
- [x] 1.6 本地启动 ulp-console，`curl http://localhost:1898/actuator/health` 返回 200 含 components.db / components.redis 详情（注：SB4 默认不带 LiquibaseHealthIndicator，仅有 LiquibaseEndpoint；liquibase 健康项由 Phase 3 自定义指标提供）
- [x] 1.7 `curl http://localhost:1898/actuator/health/liveness` 与 `/readiness` 返回 200 + `{"status":"UP"}`
- [ ] 1.8 commit: `feat(actuator): expose health/info/metrics/prometheus endpoints with probe support`

## 2. build-info / git-info 接入

- [ ] 2.1 root `pom.xml` 的 `<build><plugins>` 加 `spring-boot-maven-plugin` 的 `build-info` execution（如已存在则跳过）
- [ ] 2.2 root `pom.xml` 加 `pl.project13.maven:git-commit-id-maven-plugin`，配置 `failOnNoGitDirectory=false`、`includeOnlyProperties=^git\.(branch|commit\.id\.abbrev|commit\.time)$`
- [ ] 2.3 在 `ulp-support` 创建 `cn.frank.ulp.support.observability.RuntimeBaselineInfoContributor`，实现 `InfoContributor`，namespace `runtime`，暴露 springBoot / java / jackson 三个字段；通过 `@Component` 注册
- [ ] 2.4 `mvnw.cmd clean package -DskipTests=true` 后检查任一部署单元 jar 内含 `META-INF/build-info.properties` 与 `git.properties`
- [ ] 2.5 本地启动后 `curl http://localhost:1898/actuator/info` 返回含 `build.version` / `git.commit.id.abbrev` / `runtime.springBoot` 三个字段
- [ ] 2.6 commit: `feat(actuator): expose build/git/runtime info via /actuator/info`

## 3. 自定义 LiquibaseChangelogHealthIndicator

- [ ] 3.1 在 `ulp-support` 创建 `cn.frank.ulp.support.observability.LiquibaseChangelogHealthIndicator`，bean 名 `liquibaseChangelogDrift`
- [ ] 3.2 实现读取 `DATABASECHANGELOG` 表的 last N 行（`SELECT ID, AUTHOR, FILENAME, MD5SUM FROM DATABASECHANGELOG ORDER BY ORDEREXECUTED DESC LIMIT 50`）
- [ ] 3.3 实现 classpath changelog 的 checksum 解析（复用 Liquibase SDK：`Liquibase` + `ChangeLogParser`）
- [ ] 3.4 实现内存 cache（5 分钟 TTL，`Caffeine` 或 `ConcurrentHashMap + Instant`）
- [ ] 3.5 漂移时 status 设为 `OUT_OF_SERVICE`，details 含 `driftedChangesets` 数组（含 id / filename / dbChecksum / classpathChecksum）
- [ ] 3.6 单元测试：mock Liquibase + JDBC，覆盖 UP / OUT_OF_SERVICE / 缓存命中 三种场景
- [ ] 3.7 本地启动后 `curl http://localhost:1898/actuator/health` 主端点 components 段含 `liquibaseChangelogDrift: { status: UP }`
- [ ] 3.8 手动 `UPDATE DATABASECHANGELOG SET MD5SUM='deadbeef' WHERE ORDEREXECUTED=1;`，等 5 分钟后再请求 health，验证 status 变为 `OUT_OF_SERVICE`（验证后回滚）
- [ ] 3.9 commit: `feat(actuator): add LiquibaseChangelogHealthIndicator with 5min cache`

## 4. SecurityConfiguration 鉴权放行

- [ ] 4.1 修改 `ulp-console/.../ConsoleSecurityConfiguration.java`：`authorizeHttpRequests` 中加 `requestMatchers("/actuator/health", "/actuator/health/**", "/actuator/info", "/actuator/prometheus").permitAll()`，加 `requestMatchers("/actuator/**").hasRole("ADMIN")`
- [ ] 4.2 修改 `ulp-portal/.../PortalSecurityConfiguration.java`：加 `permitAll` 公开端点；其余 `/actuator/**` 用 `denyAll()`
- [ ] 4.3 修改 `ulp-openapi/.../OpenApiSecurityConfiguration.java`：同 portal，公开端点 `permitAll`，其余 `denyAll`
- [ ] 4.4 三处确认未使用 `antMatchers`，全部 `requestMatchers`（runtime-baseline 已禁 antMatchers）
- [ ] 4.5 写一个 `ActuatorSecurityIT` 集成测试（基于 `AbstractIntegrationTest`），覆盖：
  - [ ] 4.5.1 未鉴权 GET `/actuator/health` → 200
  - [ ] 4.5.2 未鉴权 GET `/actuator/info` → 200
  - [ ] 4.5.3 未鉴权 GET `/actuator/prometheus` → 200，body 含 `http_server_requests_seconds_count`
  - [ ] 4.5.4 未鉴权 GET `/actuator/env` → 401 或 403
  - [ ] 4.5.5 未鉴权 GET `/actuator/loggers` → 401 或 403
  - [ ] 4.5.6 未鉴权 GET `/actuator/heapdump` → 404（未暴露）
- [ ] 4.6 三个部署单元各跑一次该 IT（或在 ulp-support 提供 abstract 基类后各模块继承）
- [ ] 4.7 commit: `feat(security): wire actuator endpoint authorization across 3 services`

## 5. Docker HEALTHCHECK 集成

- [ ] 5.1 `deploy/docker/ulp-console.Dockerfile` 加 `HEALTHCHECK --interval=30s --timeout=10s --retries=3 --start-period=60s CMD curl -fs http://localhost:1898/actuator/health/liveness || exit 1`
- [ ] 5.2 `deploy/docker/ulp-portal.Dockerfile` 同上（port 1989）
- [ ] 5.3 `deploy/docker/ulp-openapi.Dockerfile` 同上（port 1988）
- [ ] 5.4 Dockerfile 基础镜像若未含 `curl` 则改用 `wget --spider` 或加 `RUN apt-get install -y curl`（取决于 base image，azul/zulu-openjdk 基于 ubuntu，含 wget 但无 curl）
- [ ] 5.5 `deploy/docker/docker-compose.yml` 三个 ulp 服务段加 `healthcheck:` 段，参数与 Dockerfile 一致
- [ ] 5.6 `docker-compose.yml` 中下游依赖 ulp-* 的服务（如 nginx）`depends_on` 段使用 `condition: service_healthy`
- [ ] 5.7 本地 `docker compose build && docker compose up -d` 启动后 `docker ps` 显示三个 ulp 容器 STATUS 为 `(healthy)`
- [ ] 5.8 commit: `chore(deploy): add HEALTHCHECK to Dockerfiles + docker-compose healthcheck`

## 6. 虚拟线程评估 + spec 锁定

- [ ] 6.1 本地启动 ulp-console，**不** 设 `spring.threads.virtual.enabled`（保持默认 false），跑现有 IT 套件确认基线绿
- [ ] 6.2 临时改 `application.yml` 加 `spring.threads.virtual.enabled: true`，重新启动 ulp-console，观察启动日志是否含 `pinning detected` 警告或线程死锁
- [ ] 6.3 在虚拟线程开启状态下跑 `mvnw.cmd verify -pl ulp-console -Dit.test=*IT`，记录是否有挂死的测试
- [ ] 6.4 记录评估结果（任一情况）：
  - 若稳定：在 design.md Decision 6 改记录评估结果为"评估稳定，本期暂不启用以保持保守"，但 spec 改为允许后续 PR 启用
  - 若不稳定：保留 design.md 原结论，spec 维持本次写的版本（暂不启用 + 触发条件）
- [ ] 6.5 恢复 `application.yml`，确认 `spring.threads.virtual.enabled` 未声明（或显式 `false`）
- [ ] 6.6 commit: `chore(runtime): evaluate virtual threads on SB4 + Hibernate7 + Lettuce, lock baseline`

## 7. 文档 + 最终验证

- [ ] 7.1 更新 root `README.md`：部署章节加"健康检查 URL"段，列出三个端口的 `/actuator/health` 与 `/actuator/prometheus`
- [ ] 7.2 更新 `CLAUDE.md` 的 "Configuration that's easy to get wrong" 段：加一条"Actuator 鉴权三处 SecurityConfiguration 必须同步更新；env/loggers 默认必须鉴权"
- [ ] 7.3 全模块 `mvnw.cmd clean verify -DskipTests=false` 通过（含新增的 `ActuatorSecurityIT` × 3）
- [ ] 7.4 三个服务本地各启动一次，烟测：
  - [ ] 7.4.1 console 1898：登录 + 创建用户 + `/actuator/health` 200 + `/actuator/env` 未鉴权 403
  - [ ] 7.4.2 portal 1989：访问登录页 + `/actuator/health` 200 + `/actuator/env` 403
  - [ ] 7.4.3 openapi 1988：调用一个公开端点 + `/actuator/health` 200 + `/actuator/env` 403
- [ ] 7.5 `openspec validate add-actuator-health --strict` 通过
- [ ] 7.6 commit: `docs: actuator health check URLs + actuator security pitfall in CLAUDE.md`

## 8. 归档

- [ ] 8.1 通过 `openspec-archive-change` skill 归档本 change 到 `openspec/changes/archive/YYYY-MM-DD-add-actuator-health/`
- [ ] 8.2 归档时 promote `observability` 新 spec 到 `openspec/specs/observability/spec.md`
- [ ] 8.3 归档时把 `runtime-baseline` delta 的虚拟线程 Requirement 合并进 `openspec/specs/runtime-baseline/spec.md`
- [ ] 8.4 `openspec validate --specs --strict` 通过（含 observability + runtime-baseline 二个 spec）
- [ ] 8.5 开 PR 合 main
