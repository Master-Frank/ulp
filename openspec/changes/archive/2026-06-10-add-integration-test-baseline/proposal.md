## Why

仓库目前总共只有 5 个单元测试 —— 全部位于 `ulp-support` 模块。OIDC 流程、console controller、以及任何依赖 MySQL/Redis 的代码都**没有任何集成测试覆盖**。这导致接下来路线图上的四项任务（Spring Boot 升级、Actuator 接入、i18n 铺设、protocol-core TODO 清理）无法验证：它们引入的任何回归都会直接在生产环境暴露，而不是在测试运行时被拦下。

现在先建立一条"窄而真实"的集成测试基线，能在我们开始动这些高爆炸半径接口之前给团队一张回归网。

## What Changes

- 新增 `integration-testing` capability，定义本项目集成测试的标准：容器化后端、MockMvc 驱动、Failsafe 执行。
- 在根 POM 中加入 `maven-failsafe-plugin`，采用 `*IT.java` 命名约定。Surefire 继续在 `mvn test` 阶段跑 `*Test.java` 单元测试；集成测试只在 `mvn verify` 阶段执行。
- 引入 Testcontainers BOM 以及 `testcontainers-mysql` + `testcontainers-junit-jupiter` 依赖。容器在每个测试类执行时启动真实的 MySQL 8 和 Redis 7；`withReuse(true)` 由开发者通过 `~/.testcontainers.properties` 自行开启。
- 从 `ulp-support` 输出一份共享 test-jar，包含 `AbstractIntegrationTest`、MySQL + Redis 容器 fixture，以及把容器 endpoint 注入 `spring.datasource.url` / `spring.data.redis.host` 的 Spring 测试配置。
- 第一波覆盖范围（**刻意收窄**，目的是树立模式而不是追求覆盖率）：
  - OIDC 模块下的 `OidcAuthorizationCodeFlowIT`：happy path、非法 `client_id`、过期授权码、`redirect_uri` 不匹配。
  - `ulp-console` 下的 `UserControllerIT`、`AppControllerIT`、`OrganizationControllerIT`：每个 controller 覆盖 list / create / delete。
- README 增加"运行集成测试"章节，说明 Docker 前置依赖、`mvn verify` 入口、以及容器复用的开启方式。

**Non-goals**（本次变更明确不做，后续可能跟进）：
- 100% controller 覆盖。这是基线，不是大扫除。
- SAML2、OAuth2-client、portal、openapi 协议测试。
- 前端测试、性能测试、压测。
- CI 接入。第一波只跑本地；CI 接入是另一次单独的变更。
- Argon2id 密码哈希测试（被推迟的 #25 任务，是另一次变更）。

## Capabilities

### New Capabilities
- `integration-testing`: 定义 ULP 中集成测试的写法、运行方式与跨模块复用契约 —— 容器后端、命名约定、生命周期、以及共享 test-jar 的约定。

### Modified Capabilities
（无 —— 这是新建的测试基础设施）

## Impact

- **构建系统**：根 `pom.xml` 增加 `maven-failsafe-plugin` 配置及 Testcontainers BOM import。`<dependencyManagement>` 新增 `testcontainers`、`testcontainers-mysql`、`testcontainers junit-jupiter` 三个管理条目。
- **ulp-support**：新增 `src/test/java` 目录及 test-jar 打包。作为共享集成测试脚手架的载体。
- **ulp-console**：在 `src/test/java` 下新增 3 个 IT 类。通过 `<type>test-jar</type>` 依赖消费上面的 test-jar。
- **OIDC 协议模块**（具体哪个模块在 design 阶段确认）：新增 1 个 IT 类。
- **README.md**：新增章节。
- **运行时依赖**：无新增。容器相关依赖全部是 `<scope>test</scope>`。
- **开发者工作流**：`mvn verify` 成为运行完整 IT 套件的入口。`mvn test` 行为不变。本地运行需要 Docker Desktop / Rancher Desktop / Colima（任何 Testcontainers 支持的 OCI runtime 都行）。

### 被驳回的方案
- **H2 内存数据库**：Liquibase changelog 使用了 MySQL 特有的 JSON 列与 utf8mb4 字符集。H2 要么会"安静地通过"那些生产会炸的测试，要么就要维护一份并行的 H2 changelog —— 既增加维护成本又有漂移风险。
- **嵌入式 Redis**（例如 `embedded-redis` 库）：上游已无人维护，最后一版发布于 2021 年，不支持 Spring Session indexed-mode 所依赖的 Redis 7 特性。
- **全服务器启动 + RestAssured 跑所有测试**：每个测试类启动时间慢 3–5 倍，对 controller 层断言没有额外信号增益。保留作为未来端到端协议测试的可选项，不作为基线。
