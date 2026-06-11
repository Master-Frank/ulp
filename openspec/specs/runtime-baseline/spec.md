# runtime-baseline Specification

## Purpose

锁定项目的运行时栈基线（Spring Boot 主版本、JVM 版本、Jackson / Spring Security / Hibernate / Liquibase / Jakarta EE 主版本、Spring Boot 配置属性键命名）。
任何降级或破坏性升级必须通过新提案修改本 spec 才允许，防止 silent regression。

由 openspec change `upgrade-spring-boot-4`（2026-06-11）首次落地，promote 自该变更的 spec delta。

## Requirements

### Requirement: Spring Boot 主版本基线
项目所有模块 SHALL 通过 `spring-boot-starter-parent` 继承 Spring Boot 主版本 **4.x**。`<spring-boot.version>` property MUST 与 `<parent>` 中声明的版本一致。任何降级回 3.x 或 2.x MUST 通过新提案改 spec 才允许。

#### Scenario: Parent POM declares Spring Boot 4
- **WHEN** 任意贡献者读根 `pom.xml`
- **THEN** `<parent>` 的 `<artifactId>` 是 `spring-boot-starter-parent`、`<version>` 以 `4.` 开头

#### Scenario: Downgrade attempt is blocked
- **WHEN** 贡献者尝试在 PR 中把 Spring Boot 版本改回 3.x
- **THEN** 评审 MUST 拒绝该 PR，除非同 PR 内含相应的 `runtime-baseline` spec 修改提案

### Requirement: JVM 大版本基线
所有模块的 Maven `<java.version>` / `<maven.compiler.source>` / `<maven.compiler.target>` SHALL 统一为 **21**（Java 21 LTS）。开发者本机与构建机 JVM 大版本 MUST ≥ 21。

#### Scenario: All modules compile under Java 21
- **WHEN** 执行 `mvnw.cmd clean compile -Dlicense.skip=true`
- **THEN** 不出现 `class file has wrong version` / `release version N not supported` 类报错

#### Scenario: New module inherits same Java baseline
- **WHEN** 新模块被加入 `<modules>`
- **THEN** 该模块的有效 Java target 通过 parent 继承为 21，不需要在子 POM 重复声明

### Requirement: Jackson 3 包路径基线
所有业务代码、配置、序列化器 SHALL 仅使用 Jackson 3 的包路径（`tools.jackson.*`，注解包 `com.fasterxml.jackson.annotation.*` 除外）。MUST NOT 引用 `com.fasterxml.jackson.databind.*` / `com.fasterxml.jackson.core.*` / `com.fasterxml.jackson.module.*` 等 Jackson 2 包。

#### Scenario: No Jackson 2 databind imports remain
- **WHEN** 全仓执行 `grep -rn "com\.fasterxml\.jackson\.databind" --include="*.java"`
- **THEN** 命中数 = 0

#### Scenario: Custom deserializer extends Jackson 3 base
- **WHEN** 贡献者新增自定义 `JsonDeserializer`
- **THEN** 该类 extends `tools.jackson.databind.JsonDeserializer<T>`，而非 `com.fasterxml.jackson.databind.JsonDeserializer<T>`

### Requirement: Spring Security 7 DSL 基线
所有 `SecurityFilterChain` bean SHALL 使用 Spring Security 7 推荐 DSL 形态：lambda-style configurer、`requestMatchers(String...)` 简化形式、方法引用形式 disable。MUST NOT 使用 Security 6.x 及更早的 deprecated 调用（如 `antMatchers(String...)`、`csrf().disable()` 链式 DSL）。

#### Scenario: SecurityConfig uses lambda DSL
- **WHEN** 评审任意 `SecurityFilterChain` bean 定义
- **THEN** 使用 `http.csrf(csrf -> csrf.disable())` 或 `http.csrf(AbstractHttpConfigurer::disable)` 形态，非 `http.csrf().disable()` 链式

#### Scenario: antMatchers is not used
- **WHEN** 全仓搜 `\.antMatchers\(`
- **THEN** 命中数 = 0

### Requirement: Spring Authorization Server 1.5+ 基线
项目 SHALL 使用 Spring Authorization Server **1.5.x** 或更新版本（与 Spring Boot 4 兼容）。所有 SAS 自定义配置（custom `AuthorizationServerSettings`、`RegisteredClientRepository`、token customizer）MUST 基于 SAS 1.5 API。

#### Scenario: Starter version matches Boot baseline
- **WHEN** 评审 `pom.xml`
- **THEN** `spring-boot-starter-oauth2-authorization-server` 由 Boot BOM 解析为 4.x（内部传递 SAS 1.5+）

### Requirement: Hibernate 7 基线
ORM 层 SHALL 使用 Hibernate ORM **7.x**。Liquibase 与 JPA 注解层 MUST 与 Hibernate 7 兼容。废弃的 `hibernate-jpamodelgen` MUST 由 `hibernate-processor` 替代。

#### Scenario: Annotation processor uses new artifact name
- **WHEN** 评审 POM 中的 `<annotationProcessorPaths>` 或 `<dependency>` 声明
- **THEN** 不出现 `hibernate-jpamodelgen` 引用

### Requirement: Liquibase 5 基线
DB schema 演进工具 SHALL 使用 Liquibase **5.x**。所有 changelog 文件 MUST 通过 Liquibase 5 的 `validate` 命令。

#### Scenario: Changelog validates under Liquibase 5
- **WHEN** 执行 `liquibase validate` 对 `db/changelog.xml`
- **THEN** 输出无 error / warning

### Requirement: Jakarta EE 11 / Servlet 6.1 基线
Web 层 SHALL 基于 Jakarta EE 11、Servlet API 6.1。所有 import MUST 使用 `jakarta.*` 包路径（无 `javax.servlet.*` 残留）。

#### Scenario: No javax.servlet imports
- **WHEN** 全仓 `grep -rn "import javax\.servlet" --include="*.java"`
- **THEN** 命中数 = 0

#### Scenario: Servlet API version reflects 6.1
- **WHEN** 执行 `mvnw.cmd dependency:tree -pl ulp-console | grep jakarta.servlet-api`
- **THEN** 解析版本 ≥ 6.1

### Requirement: 运行时配置属性键基线
所有 `application.yml` / `application.properties` 文件 SHALL 使用 Spring Boot 4 BOM 当前承认的属性键，MUST NOT 残留 Boot 4 已废弃且不再绑定的旧 auto-config 键。

具体到 Session：Boot 4 移除了 `spring-boot-session` auto-configuration。下列 Boot 3 自动绑定的键在 Boot 4 不再被识别，应迁移到代码侧（`@EnableRedisIndexedHttpSession` 注解 + `SessionRepositoryCustomizer` bean）：
- `spring.session.redis.flush-mode` → `SessionRepositoryCustomizer` 中 `setFlushMode(FlushMode.IMMEDIATE)`
- `spring.session.redis.repository-type` → `@EnableRedisIndexedHttpSession` vs `@EnableRedisHttpSession` 注解选择

允许：把 `spring.session.redis.namespace` 当作**自定义占位符**保留（由我们自己的 `${...}` 表达式消费，不依赖 Boot 自动绑定），便于把 redis 命名空间集中在 yml 中管理。

#### Scenario: No deprecated session auto-config keys remain
- **WHEN** 全仓 `grep -rn "spring\.session\.redis\.flush-mode\|spring\.session\.redis\.repository-type" --include="*.yml" --include="*.properties"`
- **THEN** 命中数 = 0

#### Scenario: Session flush mode wired in code
- **WHEN** 评审 `*SessionConfiguration.java`（console / portal）
- **THEN** 存在 `SessionRepositoryCustomizer<RedisIndexedSessionRepository>` bean 显式 `setFlushMode(FlushMode.IMMEDIATE)`
