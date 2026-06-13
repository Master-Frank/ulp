## 1. Actuator 基础接入（端点暴露 + 健康分级）

- [x] 1.1 在 root `pom.xml` 加 `io.micrometer:micrometer-registry-prometheus` 依赖（已有 actuator starter，仅缺 Prometheus registry）
- [x] 1.2 三个部署单元（ulp-console / ulp-portal / ulp-openapi）`application.yml` 新增 `management.endpoints.web.exposure.include: health, info, metrics, prometheus, loggers, env, mappings`
- [x] 1.3 三个 `application.yml` 新增 `management.endpoint.health.show-details: always` + `management.endpoint.health.probes.enabled: true`
- [x] 1.4 三个 `application.yml` 新增 `management.info.env.enabled: true`、`management.info.build.enabled: true`、`management.info.git.enabled: true`、`management.info.git.mode: full`
- [x] 1.5 `mvnw.cmd clean compile -Dlicense.skip=true` 全模块通过
- [x] 1.6 本地启动 ulp-console，`curl http://localhost:1898/actuator/health` 返回 200 含 components.db / components.redis 详情（注：SB4 默认不带 LiquibaseHealthIndicator，仅有 LiquibaseEndpoint；liquibase 健康项由 Phase 3 自定义指标提供）
- [x] 1.7 `curl http://localhost:1898/actuator/health/liveness` 与 `/readiness` 返回 200 + `{"status":"UP"}`
- [x] 1.8 commit: `feat(actuator): expose health/info/metrics/prometheus endpoints with probe support` (63bd364)

## 2. build-info / git-info 接入

- [x] 2.1 `spring-boot-maven-plugin` 加 `build-info` execution（**偏差**：放在三个 deployable pom 而非 root pom——root 加会让所有 library jar 都生成 `META-INF/build-info.properties`，Spring Boot `BuildProperties` auto-config 读 classpath 单一资源会导致下游应用拿到错乱的 ulp-* library 元数据而非自己的）
- [x] 2.2 root `pom.xml` `pluginManagement` 加 `io.github.git-commit-id:git-commit-id-maven-plugin`，配置 `failOnNoGitDirectory=false` + `includeOnlyProperties`（白名单 branch / build.{time,version} / commit.* —— 比 plan 略保守，剔除 user.email/host 等可能泄漏的字段）；三个 deployable pom 显式声明插件激活
- [x] 2.3 在 `ulp-support` 创建 `cn.frank.ulp.support.observability.RuntimeBaselineInfoContributor`，实现 `InfoContributor`，namespace `runtime`，暴露 **springBoot / spring / java / jackson** 四字段（plan 写三字段，多加 spring 是因为 Spring Boot 4 + Spring Framework 7 是分版本号锁定的，两边都要 visible）；通过 `@Component` 直接注册（不走 SupportAutoConfiguration `@Bean`——三个 Application 类都在 `cn.frank.ulp` 包根，默认 component scan 必然覆盖 `cn.frank.ulp.support.observability.**`）
- [x] 2.4 `mvnw.cmd -pl ulp-support,ulp-console -am install -DskipTests` 后 unzip ulp-console fat jar → BOOT-INF/lib/ulp-support-1.1.0.jar 含 RuntimeBaselineInfoContributor.class；ulp-console-1.1.0.jar BOOT-INF/classes/ 含 `META-INF/build-info.properties` + `git.properties`
- [x] 2.5 本地启动 ulp-console 1898，`curl /actuator/info` 返回三 namespace 齐全：`build.version=1.1.0`、`git.commit.id.abbrev=63bd364`、`runtime.springBoot=4.0.7` + spring 7.0.8 + java 21.0.11 + jackson 3.1.4
- [x] 2.6 commit: `feat(actuator): expose build/git/runtime info via /actuator/info` (aba85ce)

## 3. 自定义 LiquibaseChangelogHealthIndicator

- [x] 3.1 在 `ulp-support` 创建 `cn.frank.ulp.support.observability.LiquibaseChangelogHealthIndicator`，bean 名 `liquibaseChangelogDriftHealthIndicator`（Spring Boot 4 actuator 用 `@Component` bean 名去 `HealthIndicator` 后缀作为 components key，故最终 health JSON 里出现的是 `liquibaseChangelogDrift`）
- [x] 3.2 ~~读取 `DATABASECHANGELOG` 表的 last N 行~~ **方案修订**：不再手写 SQL，改为复用 Liquibase 5 SDK 的 `Liquibase.listUnrunChangeSets()` + `listUnexpectedChangeSets()`——前者识别 classpath 有但 DB 没跑的、后者识别 DB 跑过但 classpath 不存在的。比手写 SQL 多识别两种漂移，且无需自己解析 changeset 元数据
- [x] 3.3 ~~classpath changelog 的 checksum 解析~~ **方案修订**：checksum 漂移由 SpringLiquibase 在 boot 期 fail-fast（应用根本起不来），indicator 不再重复检测；改为聚焦 unrun / unexpected 两个运行时可见的漂移信号。规避了 `ClassLoaderResourceAccessor` + Spring `classpath*:` 前缀的复杂兼容问题
- [x] 3.4 实现内存 cache（5 分钟 TTL，`AtomicReference<CachedHealth>` + `Instant` 比较；零额外依赖，无需 Caffeine）
- [x] 3.5 漂移时 status `OUT_OF_SERVICE`，details 含 `unrunInClasspath` / `unexpectedInDb` 数组（字段名比 plan 的 `driftedChangesets` 更直接表达检测维度）
- [x] 3.6 单元测试：`LiquibaseChangelogHealthIndicatorTest` 7 个用例覆盖 UP / OUT_OF_SERVICE（仅 unrun / 仅 unexpected / 二者皆有）/ 缓存命中 / invalidate / classpath 前缀剥离。**未 mock JDBC + DatabaseFactory**——Liquibase SDK 静态调用链路 mock 成本太高且脆，改为：①提取 `buildHealth(unrun, unexpected, changelog)` 静态方法直测构造逻辑，②匿名子类覆盖 `protected compute()` + 计数器验缓存语义
- [x] 3.7 本地启动 ulp-console 后 `curl http://localhost:1898/actuator/health` 返回 `liquibaseChangelogDrift: { status: UP, details: { changelog: "classpath*:db/ulp-changelog-master.xml", status: "all changesets applied" } }`
- [x] 3.8 ~~手动 `UPDATE DATABASECHANGELOG SET MD5SUM='deadbeef'`~~ **跳过**：原 plan 假设我们检测 md5sum 漂移，但 3.3 已把检测维度收敛到 unrun/unexpected，md5sum 改了也不会触发本 indicator（boot 期 SpringLiquibase 直接 fail-fast，应用根本起不来到 indicator 阶段）。同等价值的 OUT_OF_SERVICE 验证已被 3.6 单元测试覆盖；运行时手动验证需 INSERT 假行到 `ulp_changelog_table`，属破坏性 DB 改动，本期不做
- [x] 3.9 commit: `feat(actuator): add LiquibaseChangelogHealthIndicator with 5min cache` (df94e5c)

**Phase 3 实现偏差汇总**（不是 bug，是有意识地优于 plan 的修订）：
1. **检测维度从 checksum → unrun/unexpected**：md5sum 漂移由 SpringLiquibase boot fail-fast 覆盖，运行时再查重复且实现复杂；改查 Liquibase SDK 自带的 list* 方法，覆盖更广更稳
2. **必须复刻 SpringLiquibase 的两处归一化**才能拿到正确结果：
   - 路径前缀剥离：`classpath*:db/ulp-changelog-master.xml` → `db/ulp-changelog-master.xml`，否则 `ChangeLogParseException: file not found`
   - 自定义表名传递：`ulp_changelog_table` / `ulp_changelog_lock_table`（本仓 `UlpLiquibaseConfiguration` 设定）必须 setDatabaseChangeLogTableName，否则新 `Liquibase` 实例查默认 `DATABASECHANGELOG` 空表，全部 changeset 报"unrun"假阳性
3. **测试策略**：不 mock JDBC + 静态 DatabaseFactory，改为提取静态 buildHealth + 匿名子类 override compute() — 测试成本低且行为覆盖更直接

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
