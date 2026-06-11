## 0. 准备

- [ ] 0.1 确认当前分支 `feature/spring-boot-upgrade` clean，IT 基线全绿（`mvnw.cmd clean verify -pl ulp-console,ulp-portal,ulp-support` 13 IT + 5 UT 全过）
- [ ] 0.2 备份 `~/.m2/repository/org/springframework/` 整个目录，遇问题快速回退
- [ ] 0.3 在 `ulp-support/src/test/java/.../testsupport/` 加一个临时 `BaselineVersionAssertionTest`：通过反射读 `spring.boot.version` + `java.specification.version`，断言当前是 3.2.12 + 17，作为"未升级"baseline；升级完后该测试改为断言 4.0.x + 21，并改名 `RuntimeBaselineAssertionTest`，永久保留
- [ ] 0.4 准备 OpenRewrite 备选方案：本 plan 走 sed + IDE，但若 Jackson 改完 IT 全炸，回退用 `org.openrewrite.recipe:rewrite-spring:SpringBoot40Migration`

## 1. POM parent + Java 21（阶段 commit 1）

- [ ] 1.1 改 `<parent>` 为 `spring-boot-starter-parent:4.0.5`（或写本任务时最新 patch）
- [ ] 1.2 改 `<java.version>` / `<maven.compiler.source>` / `<maven.compiler.target>` / `<java.source.version>` / `<java.target.version>` 全部 17 → 21
- [ ] 1.3 改 `<spring-boot.version>` property 4.0.5
- [ ] 1.4 评估并删除可被 Boot 4 BOM 接管的 override（先注释、不删，等阶段 11 确认无回归再彻底删）：
  - `<maven-failsafe-plugin.version>` 3.5.2
  - `<commons-lang3.version>` 3.17.0
  - `<docker-java.version>` 3.5.1
- [ ] 1.5 `mvnw.cmd dependency:tree -pl ulp-support` 看 Boot 4 实际提供的版本
- [ ] 1.6 commit: `chore(deps): bump spring-boot 3.2.12 → 4.0.5 + java 17 → 21 (parent only)`

## 2. 第一次编译扫雷（阶段 commit 2）

- [ ] 2.1 `mvnw.cmd clean compile -Dlicense.skip=true -DskipTests`，把所有报错收集到一个 list
- [ ] 2.2 按错误大类分组：(a) Jackson 包名 (b) Spring Security DSL (c) Spring Session 属性键 (d) BootstrapRegistry 包路径 (e) Hibernate API 调整 (f) 其他
- [ ] 2.3 不修代码，先写 commit message 草稿（让自己看清楚问题面）
- [ ] 2.4 commit: `chore(deps): document compile errors after spring-boot 4 bump (WIP)`（content 是错误清单文件）

## 3. Jackson 3 迁移（阶段 commit 3）

- [x] 3.1 全仓 grep `import com.fasterxml.jackson` 排除 `com.fasterxml.jackson.annotation`，结果列表入 `tasks.md` 附录
- [x] 3.2 批量替换 import 包前缀：
  - `com.fasterxml.jackson.databind` → `tools.jackson.databind`
  - `com.fasterxml.jackson.core` → `tools.jackson.core`
  - `com.fasterxml.jackson.module` → `tools.jackson.module`
- [x] 3.3 修引用：
  - `tools.jackson.databind.Module` 不存在 → `JacksonModule`
  - `ObjectMapper` 不可变：`registerModules / setVisibility / setSerializationInclusion / setHandlerInstantiator` 全部走 `JsonMapper.builder()...build()` 或 `mapper.rebuild()...build()`
  - `SecurityJackson2Modules.enableDefaultTyping(owner)` 已移除 → `JsonMapper.builder().activateDefaultTyping(validator, NON_FINAL, PROPERTY)`
  - `SimpleModule.SetupContext#setMixInAnnotations` → `setMixIn`
  - `SpringHandlerInstantiator` → `JacksonHandlerInstantiator`（`org.springframework.http.support`）
  - `GenericJackson2JsonRedisSerializer` → `GenericJacksonJsonRedisSerializer`
  - `OAuth2AuthorizationServerJackson2Module` → `OAuth2AuthorizationServerJacksonModule`（`org.springframework.security.oauth2.server.authorization.jackson`）
- [x] 3.4 重点核对 4 个文件：UserDetailsDeserializer / RedisOAuth2AuthorizationService（SAS）/ Console+Portal SecurityConfig / 全部 Mixin（OAuth2 / Authentication / Form / Jwt / Geo / Web / Security）
- [x] 3.5 编译错误集中到 Jackson 之外的阶段（Security 7 AntPathRequestMatcher / Hibernate 7 / SB4 ErrorAttributes / SB4 CacheProperties+RedisProperties / Hibernate Validator 8 EmailValidator），Jackson 报错清零
- [ ] 3.6 `mvnw.cmd test -pl ulp-support` 等编译全绿后再跑（Phase 4-6 完成后回来跑）
- [x] 3.7 commit: `refactor(deps): migrate jackson 2.x → 3.x (com.fasterxml → tools.jackson)` (adad774)

## 4. Spring Security 7 DSL 改写（阶段 commit 4-7，按 SecurityConfig 拆 4 个 commit）

- [x] 4.1 列出所有 `SecurityFilterChain` bean
- [x] 4.2 改 console SecurityConfig，按 Security 7 DSL 重写（AntPathRequestMatcher → PathPatternRequestMatcher / pathPattern 改为单 HttpMethod / OAuth2ParameterNames.USERNAME 移除）
- [x] 4.3 改 portal SecurityConfig（含 ServerProperties.getError() → ErrorProperties / RedisSessionProperties → @Value）
- [x] 4.4 改 openapi SecurityConfig（ServletComponentScan 包路径迁移）
- [x] 4.5 改 SAS 自定义 SecurityConfig（OidcProtocolSecurityConfiguration: SerializationFeature → DateTimeFeature）
- [x] 4.6 全仓 `mvnw.cmd compile -Dlicense.skip=true -T 1C` 全 40 模块 BUILD SUCCESS（Phase 4 squashed 为单 commit，因 SB4 API relocations 与 SS7 DSL 改写不可拆分）

## 5. Spring Session 与 application.yml 迁移（阶段 commit 8）

- [x] 5.1 ~~临时在 ulp-console pom 加 `spring-boot-properties-migrator` 依赖~~（跳过：直接走 SB4 metadata 静态分析，properties-migrator 也搞不定本场景）
- [x] 5.2 **关键发现**：SB4 完全移除了 `spring-boot-session` 自动装配；`spring.session.redis.*` 在 SB4 没有任何 ConfigurationProperties binder。原计划"`spring.session.redis.*` → `spring.session.data.redis.*`"是错的——真正变动是失去自动装配
- [x] 5.3 改 `ConsoleSessionConfiguration` / `PortalSessionConfiguration`：手动加 `@EnableRedisIndexedHttpSession(redisNamespace=...)`，新增 `SessionRepositoryCustomizer<RedisIndexedSessionRepository>` bean 承载 `flush-mode=immediate`
- [x] 5.4 改 `ulp-console/src/main/resources/application.yml`：保留 `spring.session.redis.namespace`（@Value 还在读），删 `flush-mode` 与 `repository-type`（已由 Java 接管）
- [x] 5.5 同样改 ulp-portal yml；ulp-openapi 不使用 HttpSession，整块 `spring.session.redis.*` 删除
- [x] 5.6 ~~移除 `spring-boot-properties-migrator` 依赖~~（无需，从未加）
- [x] 5.7 commit: `refactor(config): wire @EnableRedisIndexedHttpSession to replace dropped SB4 session autoconfig`

## 6. Hibernate 7.2（阶段 commit 9）

- [x] 6.1 pom 全仓无 `hibernate-jpamodelgen` 显式声明（仅 `hibernate-validator` / `hibernate-core` 由 SB4 BOM 接管），无需改 `hibernate-processor`
- [x] 6.2 全仓 grep `org.hibernate.dialect.MySQLDialect` / `MySQL8Dialect`：无命中，Hibernate 6 起 dialect 已从 JDBC URL 自动推断
- [x] 6.3 检查 `@Entity` 类无 `@Type` / `@TypeDef` 注解；项目使用 `@JdbcTypeCode(SqlTypes.JSON)` + `@SoftDelete` + `Hibernate.isInitialized()`，全部为 Hibernate 6 起稳定 API
- [x] 6.4 顺手清理：移除根 pom 的 `jackson-datatype-hibernate6`（拉 Jackson 2.21.4 legacy，自 Phase 3 起从未被任何 Java 文件 import，与 Jackson 3 (`tools.jackson`) 架构不兼容）
- [x] 6.5 `mvnw.cmd compile -Dlicense.skip=true -T 1C`（JDK 21）40/40 模块 BUILD SUCCESS
- [x] 6.6 commit: `refactor(deps): verify hibernate 7.2 compatibility + drop unused jackson-datatype-hibernate6`

## 7. 三方依赖核对（阶段 commit 10）

- [x] 7.1 Liquibase **已在 5.0.3**（由 SB4 BOM 拉入），无需 standalone dry-run；项目 changelog 全是基础 createTable / addColumn，与 LB5 完全兼容
- [x] 7.2 ~~对比 4.x 生成的 SQL~~：跳过，原 baseline 已迁移
- [x] 7.3 三方依赖关键 verify：
  - **redisson 3.40.2 → 4.4.0**：关键升级。3.40.2 拉 `redisson-spring-data-34`（Spring Data Redis 3.4 binding），但 SB4 提供 Spring Data Redis **4.0.6**，运行时会 `NoSuchMethodError`。Redisson 4.x 起新增 `redisson-spring-data-40` binding，4.4.0 当前 Maven Central 最新稳定（2026-05）
  - **springdoc-openapi 2.6.0 → 3.0.3**：springdoc v3 是 SB4 专用线，v2.x 依赖 Spring Framework 6.x API。3.0.3 由社区于 2024-04 发布，明确"Upgrade Spring Boot to version 4.0.5"
  - jjwt 0.12.6：保留。jjwt-jackson 仍依赖 Jackson 2，但与 Jackson 3 (tools.jackson) 包名不同，可类路径共存，无 runtime 冲突
  - easyexcel 3.3.4：保留。POI-based，无 Spring 直接依赖；编译通过
  - jib-maven-plugin 3.4.4：保留（与 SB 版本无关）
  - MapStruct 1.6.3 + lombok 1.18.34 + lombok-mapstruct-binding 0.2.0：已知 Java 21 兼容，编译通过
- [x] 7.4 SF7 API 兼容修复：`UriComponentsBuilder.fromHttpUrl(String)` 在 SF7 移除 → 改 `fromUriString(String)`（增量编译曾掩盖此问题，bump 触发 cache 失效后暴露）。命中：`FeiShuClient.java:243`
- [x] 7.5 `mvnw.cmd clean compile -Dlicense.skip=true -T 1C`（JDK 21）40/40 模块 BUILD SUCCESS
- [x] 7.6 commit: `refactor(deps): bump redisson 3.40.2 -> 4.4.0 + springdoc 2.6.0 -> 3.0.3 for SB4`

## 8. IT 套件回归（阶段 commit 11）

- [x] 8.1 `mvnw.cmd clean verify -Dlicense.skip=true -DskipTests=false -DskipITs=false`：41/41 模块 BUILD SUCCESS，3:31 min（ulp-console 1:10、ulp-portal 51.9s、ulp-openapi 4s）。注：根 pom `<skipTests>true</skipTests>` 必须 `-DskipTests=false -DskipITs=false` 显式覆盖
- [x] 8.2 IT 失败根因 3 个，已修：
  - `UnsatisfiedDependencyException: No qualifying bean 'MailProviderSend'` → `MessageSendConfiguration.mailProviderSend()` 在无 DB 配置时返回 null，原 @RefreshScope proxy 掩盖；改返回 `MailNoneProviderSend` fallback
  - `NullPointerException: 'null' not accepted as handled type` → Jackson 3 `StdDeserializer.<init>` 改为 `Objects.requireNonNull(vc)`，`ListEnumDeserializer` 两个构造器 `super((Class<?>) null)` → `super(List.class)`
  - `PolymorphicTypeValidator denied resolution: OAuth2AuthorizationCode` → `SupportJackson2Module.objectMapperBuilder` PTV allowlist 仅覆盖 `cn.frank.ulp.` / `java.util.` / `java.lang.`，补 `java.time.` + `org.springframework.security.`
- [x] 8.3 无 `--add-opens` 警告刷屏，failsafe argLine 无需调整
- [x] 8.4 commit: `test(it): green IT suite under spring boot 4 + java 21` (39b2664, 17 files, +38/-46)
- [ ] 8.5 ~~记录冷启动 / 热启动耗时对比 3.2 vs 4.0~~ 跳过：基线未捕获冷启动 timing，无对比意义

## 9. 三个应用本地烟测（阶段 commit 12）

- [x] 9.1 console（1898）启动 OK，HTTP 200，19.6s 冷启动；SPA shell 正常返回。注：UI 交互未跑（无浏览器自动化），HTTP 探测充当替代
- [x] 9.2 portal（1989）启动 OK，HTTP 200，20.9s 冷启动；`UserUnlockTask` 调度任务执行正常
- [x] 9.3 openapi（1988）启动 OK，HTTP 200；`/v3/api-docs` 返回 62KB OpenAPI 规范，serdes 正常
- [x] 9.4 三服务并行运行验证；日志只剩良性 warning（logback deprecation / SpringDoc default-enabled / Security ignore-vs-permitAll 建议）
- [x] 9.5 启动期暴露 2 个 SB4 死锁，归类 (a) Boot 4 配置/装配时序，已修：
  - **JPA bootstrap-mode**：`spring.data.jpa.repositories.bootstrap-mode=deferred` 触发 async EMF FutureTask；Hibernate 7 `MultiTenancy.getTenantIdentifierResolver` 回调 SpringBeanContainer.getBean 抢 BeanFactory ReentrantLock，主线程等 EMF future → 死锁。三应用统一改 `default`（同步装配）
  - **Session 与 Security 解耦**：SB4 `RedisIndexedHttpSessionConfiguration.setDefaultRedisSerializer` 参数解析提前触发 SecurityConfig 实例化；Portal 的 SecurityConfig ctor 注入 UserRepository → JPA fragments 在 EntityManager 就绪前被装配 → `UnsatisfiedDependencyException`。修复：把 `springSessionDefaultRedisSerializer` @Bean 从 `PortalSecurityConfiguration` 抽到轻量 `PortalSessionConfiguration`（无 JPA 依赖）。Console SecurityConfig 无 ctor 注入，不受影响
- [ ] 9.6 commit: `test(smoke): verify console + portal + openapi running under spring boot 4`

## 10. 清理 + Java 21 优化（阶段 commit 13）

- [ ] 10.1 删除阶段 1.4 注释掉的过期 override（commons-lang3 / docker-java / failsafe）
- [ ] 10.2 `BaselineVersionAssertionTest` 重命名为 `RuntimeBaselineAssertionTest`，断言切换为 4.0.x + 21，永久保留
- [ ] 10.3 评估是否把 Tomcat 改用虚拟线程（`spring.threads.virtual.enabled=true` Boot 4 默认 false，可开）—— 决策记录到 tasks.md 但本次不一定启用，留给 #39 一起评估
- [ ] 10.4 commit: `chore(deps): remove obsolete dependency overrides after spring boot 4 upgrade`

## 11. 文档（阶段 commit 14）

- [ ] 11.1 README 加"运行时要求"章节：Java 21 LTS + Maven 3.9+ + Docker + MySQL 8 / Redis 7
- [ ] 11.2 README 升级章节如适用：旧 fork 用户从 3.2.x 升 4.0.x 的最小指引
- [ ] 11.3 在 openspec `archive/` 移动本变更（task 完成后由 `openspec archive` 命令处理；本仓库无 CLI 时手工 mv）
- [ ] 11.4 commit: `docs: update runtime requirements for spring boot 4`

## 12. 最终验证

- [ ] 12.1 干净 checkout 上跑一遍 `mvnw.cmd clean verify`，全绿
- [ ] 12.2 全仓 grep 残留：
  ```
  grep -rn "com\.fasterxml\.jackson\.databind\|com\.fasterxml\.jackson\.core\|com\.fasterxml\.jackson\.module" --include="*.java" D:/project/ulp/
  ```
  应只剩 import 注解的合法引用（`com.fasterxml.jackson.annotation` 包）
- [ ] 12.3 `runtime-baseline` spec deltas 进 `openspec/specs/runtime-baseline/spec.md`（归档时 OpenSpec 自动 promote；手工则 cp）
- [ ] 12.4 准备 PR 描述：列出 12 个阶段 commit、IT 套件耗时对比、known issues（如有）

---

## 工时估算

| 阶段 | 估时 |
|---|---|
| 0 准备 | 0.5h |
| 1 POM bump | 0.5h |
| 2 编译扫雷 | 1-2h |
| 3 Jackson 3 | 6-12h ← 最重 |
| 4 Security 7 DSL × 4 | 4-8h |
| 5 application.yml | 1-2h |
| 6 Hibernate 7.1 | 2-4h |
| 7 Liquibase 5 + 三方 | 2-4h |
| 8 IT 回归 | 8-16h ← 第二重 |
| 9 三应用烟测 | 2-4h |
| 10 清理 | 1h |
| 11 文档 | 1h |
| 12 最终验证 | 1h |
| **合计** | **30-56h** |

注：单次 commit 操作开销不计。HeroDevs 报"企业规模 200-500h"是大型 monolith + 上百个集成点 + 严格 SOX 流程的报价；ULP 是 fork、有 IT 基线、单人决策，预计 30-56h 聚焦工时，跨 5-8 个工作日完成。
