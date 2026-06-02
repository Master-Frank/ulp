## ADDED Requirements

### Requirement: Container-backed test database
项目集成测试 SHALL 使用 Testcontainers 启动的真实 MySQL 8 作为数据库后端。集成测试 MUST NOT 使用内存替代品（如 H2）。

#### Scenario: Integration test boots against MySQL container
- **WHEN** 一个继承共享基类的集成测试类启动
- **THEN** 启动一个 MySQL 8 容器，对其执行 Liquibase changelog，`spring.datasource.url` 解析为容器的 JDBC URL

#### Scenario: H2 is not used for integration tests
- **WHEN** 开发者试图新增一个依赖 H2 的集成测试
- **THEN** 测试基础设施 SHALL NOT 提供 H2 数据源，开发者 MUST 改用容器化的基类

### Requirement: Container-backed Redis
任何涉及 session 存储、token 存储或其他 Redis 后端组件的集成测试 SHALL 使用 Testcontainers 启动的真实 Redis 7 实例。

#### Scenario: Integration test boots against Redis container
- **WHEN** 一个继承共享基类的集成测试启动
- **THEN** 启动一个 Redis 7 容器，`spring.data.redis.host` / `spring.data.redis.port` 解析为容器的 endpoint

#### Scenario: Embedded Redis libraries are not used
- **WHEN** 开发者新增一个涉及 Redis 的集成测试
- **THEN** 测试 MUST NOT 依赖 `embedded-redis` 或任何其他进程内 Redis 替代品

### Requirement: Failsafe-driven `*IT.java` naming
集成测试 SHALL 由 `maven-failsafe-plugin` 在 `verify` 阶段执行。测试类 MUST 以 `IT` 为后缀（如 `UserControllerIT`）。单元测试继续使用 `Test` 后缀，由 `maven-surefire-plugin` 在 `test` 阶段执行。

#### Scenario: `mvn test` skips integration tests
- **WHEN** 开发者运行 `mvn test`
- **THEN** Surefire 只执行 `*Test.java` 类，不启动任何容器

#### Scenario: `mvn verify` executes integration tests
- **WHEN** 开发者运行 `mvn verify`
- **THEN** Surefire 先跑单元测试，然后 Failsafe 在已启动的容器上跑 `*IT.java` 类

### Requirement: Shared test infrastructure via `ulp-support` test-jar
共享的集成测试脚手架（基类、容器 fixture、Spring 测试配置）SHALL 由 `ulp-support` 模块以 test-jar 形式输出。消费模块通过 `<type>test-jar</type><scope>test</scope>` 引入。

#### Scenario: Module consumes shared base class
- **WHEN** 任意消费模块中的集成测试继承 `AbstractIntegrationTest`
- **THEN** 该类从 `ulp-support` 的 test-jar 解析得到，容器 fixture 被继承，无需重复 setup

#### Scenario: Production classpath excludes test infrastructure
- **WHEN** 构建任意模块的生产产物
- **THEN** `ulp-support` 的 test-jar 内容 NOT 出现在生产 classpath 中

### Requirement: Per-test isolation via transaction rollback and explicit Redis cleanup
每个集成测试方法 SHALL 运行在 Spring 管理的事务里，方法结束时回滚，SQL 状态不在方法之间泄漏。涉及 Redis 写入的测试 MUST 在 `@AfterEach` 中清空测试数据库。

#### Scenario: SQL changes do not leak between tests
- **WHEN** 测试方法 A 插入一行，然后测试方法 B 运行
- **THEN** 方法 B 观察不到方法 A 插入的行

#### Scenario: Redis state is cleared between tests
- **WHEN** 一个涉及 Redis 的测试写入了一个 key，然后另一个涉及 Redis 的测试运行
- **THEN** 第二个测试开始时 Redis 数据库为空

### Requirement: Container reuse is developer opt-in
共享基类 SHALL 将容器声明为 `static` `@Container` 字段，使其每个 JVM 启动一次。跨次运行的容器复用（`withReuse(true)`）MUST 由开发者通过 `~/.testcontainers.properties` 自行开启，MUST NOT 在任何提交到仓库的项目配置中默认开启。

#### Scenario: Default behavior boots containers per JVM
- **WHEN** 一个未开启复用的开发者运行 `mvn verify`
- **THEN** 容器在 JVM 启动时创建、在整个测试运行期间存活、JVM 退出时销毁

#### Scenario: Opt-in reuse persists containers across runs
- **WHEN** `~/.testcontainers.properties` 中含 `testcontainers.reuse.enable=true` 的开发者连续两次运行 `mvn verify`
- **THEN** 第二次运行复用第一次的容器，省去容器启动时间

### Requirement: First-wave coverage
第一波集成测试 SHALL 覆盖 OIDC 授权码流程以及 `ulp-console` 三个主要 controller。第一波目的是树立测试模式；更广的覆盖明确不在范围内，将在后续变更中推进。

#### Scenario: OIDC authorization code flow has IT coverage
- **WHEN** 第一波套件运行
- **THEN** `OidcAuthorizationCodeFlowIT` 覆盖 happy path、非法 `client_id`、过期授权码、`redirect_uri` 不匹配四个场景

#### Scenario: Console CRUD controllers have IT coverage
- **WHEN** 第一波套件运行
- **THEN** `UserControllerIT`、`AppControllerIT`、`OrganizationControllerIT` 各自端到端验证 list、create、delete 三类操作

### Requirement: Documentation of test runtime prerequisites
项目 README SHALL 说明 Docker 前置依赖、`mvn verify` 入口、以及容器复用 opt-in 的写法，使任何贡献者无需额外指导即可本地运行集成测试。

#### Scenario: Contributor finds run instructions in README
- **WHEN** 新贡献者阅读 README
- **THEN** 能找到一个"运行集成测试"章节，列出 Docker 依赖、`mvn verify` 命令和复用 opt-in 的配置片段
