# integration-testing Specification (delta)

## MODIFIED Requirements

### Requirement: Container-backed test database
项目集成测试 SHALL 使用 Testcontainers 启动的真实 MySQL 8 作为数据库后端。集成测试 MUST NOT 使用内存替代品（如 H2）。集成测试 SHALL 在 Spring Boot **4.x** + Spring TestContext **7.x** 环境下运行。

#### Scenario: Integration test boots against MySQL container
- **WHEN** 一个继承共享基类的集成测试类启动
- **THEN** 启动一个 MySQL 8 容器，对其执行 Liquibase **5** changelog，`spring.datasource.url` 解析为容器的 JDBC URL

#### Scenario: H2 is not used for integration tests
- **WHEN** 开发者试图新增一个依赖 H2 的集成测试
- **THEN** 测试基础设施 SHALL NOT 提供 H2 数据源，开发者 MUST 改用容器化的基类

### Requirement: Failsafe-driven `*IT.java` naming
集成测试 SHALL 由 `maven-failsafe-plugin` **3.5+** 在 `verify` 阶段执行（版本由 Spring Boot 4 BOM 接管）。测试类 MUST 以 `IT` 为后缀。单元测试继续使用 `Test` 后缀，由 `maven-surefire-plugin` 在 `test` 阶段执行。

#### Scenario: `mvn test` skips integration tests
- **WHEN** 开发者运行 `mvnw.cmd test`
- **THEN** Surefire 只执行 `*Test.java` 类，不启动任何容器

#### Scenario: `mvn verify` executes integration tests under Boot 4
- **WHEN** 开发者运行 `mvnw.cmd verify`
- **THEN** Surefire 先跑单元测试，然后 Failsafe 在已启动的容器上跑 `*IT.java` 类，Spring Boot 启动横幅显示 4.0.x

### Requirement: Per-test isolation via transaction rollback and explicit Redis cleanup
每个集成测试方法 SHALL 运行在 Spring 管理的事务里，方法结束时回滚。涉及 Redis 写入的测试 MUST 在 `@AfterEach` 中清空测试数据库。Spring TestContext 7 下的 `@Transactional` 语义与 6.x 一致，本约定无变更。

#### Scenario: SQL changes do not leak between tests under Spring TestContext 7
- **WHEN** 测试方法 A 插入一行，然后测试方法 B 运行
- **THEN** 方法 B 观察不到方法 A 插入的行

## ADDED Requirements

### Requirement: 框架升级前必跑 IT 全套
任何修改 Spring Boot / Spring Framework / Spring Security / Spring Session / Hibernate / Liquibase / Jackson 主版本号的 PR SHALL 在 PR 描述中附 `mvnw.cmd clean verify` 完整输出（或 CI 等价记录），证明 IT 套件在升级后全绿。

#### Scenario: Major version bump PR includes verify output
- **WHEN** 评审一个修改 `<spring-boot.version>` 或 `<java.version>` 主版本的 PR
- **THEN** PR 描述包含 IT 套件运行结果（通过 / 失败数 / 耗时），且通过率 = 100%

#### Scenario: Minor version bump skips this requirement
- **WHEN** PR 仅修改 patch / minor 版本号（如 4.0.5 → 4.0.6）
- **THEN** 不强制附完整 verify 输出，但 PR 描述 MUST 说明已本地跑过 verify
