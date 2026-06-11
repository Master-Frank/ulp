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

## 7. Liquibase 5 + 三方依赖核对（阶段 commit 10）

- [ ] 7.1 standalone Liquibase 5 dry-run：装 `liquibase-cli-5.x`，用项目 `db/changelog.xml` 跑 `liquibase validate --url=jdbc:mysql://...` + `liquibase update-sql --url=...`
- [ ] 7.2 对比 4.x 生成的 SQL（如还能跑 4.x），确认 DDL 一致
- [ ] 7.3 不一致时排查（可能是 utf8mb4 / JSON 列方言变化）
- [ ] 7.4 三方依赖兼容性逐项 verify：
  - springdoc-openapi 2.6 → 跟随 BOM 看是否升 2.7+
  - redisson 3.40.2 → 看 GitHub 是否声明 Spring Boot 4 支持，必要时升 3.41+
  - jib-maven-plugin 3.4.4 → 3.4 应兼容 Boot 4
  - jsonwebtoken (jjwt) 0.12.6 → 与 Jackson 3 兼容性 verify
  - easyexcel 3.3.4 → 内部依赖核对
  - MapStruct 1.6.3 + lombok 1.18.34 + lombok-mapstruct-binding 0.2.0 → 已知 Java 21 兼容
- [ ] 7.5 commit: `refactor(deps): bump third-party deps for spring boot 4 compatibility`

## 8. IT 套件回归（阶段 commit 11）

- [ ] 8.1 `mvnw.cmd clean verify -Dlicense.skip=true -DskipTests=false`，所有 13 IT + 5 UT 必须全绿
- [ ] 8.2 失败时按 IT 类逐个排查；重点关注：
  - OidcAuthorizationCodeFlowIT 4 个方法（SAS 1.5 行为变化）
  - UserControllerIT.listEmptyOrPaged（Jackson 3 序列化形状）
  - AppControllerIT / OrganizationControllerIT（Spring Security 7 DSL 行为）
- [ ] 8.3 调试中如 `--add-opens` 警告刷屏，在 `<maven-failsafe-plugin>` 的 `<argLine>` 加 `--add-opens java.base/java.lang=ALL-UNNAMED` 等
- [ ] 8.4 全绿后 commit: `test(it): verify IT suite passes under spring boot 4 + java 21`
- [ ] 8.5 记录冷启动 / 热启动耗时对比 3.2 vs 4.0 数据

## 9. 三个应用本地烟测（阶段 commit 12）

- [ ] 9.1 启动 console（1898），登录 admin，至少完成：用户列表 / 创建用户 / 删除用户 / 应用列表 / 角色列表 / 退出
- [ ] 9.2 启动 portal（1989），跳转 console SSO 登录，进入用户首页
- [ ] 9.3 启动 openapi（1988），调用 `/api/v1` 任一接口（reset_password 是 #15 新加端点，可用它）
- [ ] 9.4 三服务一起跑半小时，观察日志有无非预期 warning / error / illegal reflective access
- [ ] 9.5 如有问题归类：(a) Boot 4 配置不对、(b) Jackson 3 漏改、(c) Security 7 DSL 行为变化、(d) 第三方依赖问题
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
