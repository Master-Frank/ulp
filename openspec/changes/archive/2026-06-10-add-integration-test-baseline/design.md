## Context

ULP 是一个 Spring Boot 3.2.12 的 monorepo，约有 30 个 Maven 模块，三个可运行的应用（console:1898、portal:1989、openapi:1988）。底层依赖 MySQL 8（Liquibase 管理）和 Redis 7（Spring Session indexed-mode + token 存储）。当前测试面只有 `ulp-support/src/test/java` 下的 5 个单元测试 —— 没有集成测试、没有 Failsafe 插件、没有容器 fixture、没有共享测试基类。

路线图上接下来的四项任务（Spring Boot 升级、Actuator、i18n、TODO 清理）都会改动当前**没有自动化覆盖**的代码路径。在这些变更落地之前，我们需要先把测试基础设施搭起来。这份设计说明这套基础设施怎么搭，第一波测试放在哪里。

约束：
- 构建在 Windows 上通过 `mvnw.cmd` 运行；测试必须跨平台可用。
- JDK 17。
- Liquibase changelog 使用 MySQL 特有特性（JSON 列、utf8mb4），测试数据库必须是真实 MySQL，不能用 H2。
- 不能破坏 `ulp-support` 现有 5 个单元测试的 `mvn test` 生命周期。
- 模块 DAG：`ulp-support` 是几乎所有模块都依赖的叶子模块。共享测试代码必须从这里输出，不能引入新的依赖边。

## Goals / Non-Goals

**Goals:**
- 在干净 checkout 上 `mvn verify` 能完整跑通，包含 4 个集成测试类（约 16 个测试方法）。
- 形成一份"ULP 集成测试该怎么写"的标准模板，后续模块可以复制照用。
- 容器生命周期足够快，开发者笔记本能日常使用（目标：开启复用后的全 IT 套件耗时 < 90 秒）。
- 对现有 `mvn test` 行为零影响。

**Non-Goals:**
- 100% controller 覆盖。第一波只覆盖 OIDC happy-path 分支 + 3 个 console controller。
- portal、openapi、SAML2、OAuth2-client 覆盖。
- CI 集成（另一次变更）。
- 性能 / 压测基础设施。
- 前端测试（`console-fe`、`portal-fe`）。

## Decisions

### D1. Testcontainers 而非 H2 / 嵌入式 Redis

MySQL 8 和 Redis 7 都用 Testcontainers。

**理由：** Liquibase changelog 使用 `JSON` 列类型和 utf8mb4 字符集；H2 的 MySQL 兼容模式会"接受但存储方式不同"，导致 H2 测试通过 / 生产 migration 炸掉的场景。Redis 这边，Spring Session indexed-mode 用了 Redis 7 的特性，`embedded-redis`（最后一版 2021，已废弃）不支持。

**被驳回的方案：**
- H2 内存数据库 —— 方言静默漂移，并行 changelog 维护成本高。
- 嵌入式 Redis —— 上游已死，缺失特性。
- 共享外部测试服务器（如开发环境 MySQL）—— 开发者之间互相污染状态，没有隔离保证。

**代价：** 开发机要装 Docker（或任何 Testcontainers 支持的 OCI runtime —— Rancher Desktop、Colima、Podman）。我们接受这个代价。

### D2. MockMvc 而非全服务器测试

Controller 集成测试用 `MockMvc`，配 `@SpringBootTest(webEnvironment = MOCK)`。

**理由：** 比 `webEnvironment = RANDOM_PORT` + RestAssured 的方案每个测试类启动快 3–5 倍，而 controller 层断言能覆盖到的内容（状态码、响应体、安全过滤链、参数校验、异常映射）一个都不少。

**被驳回的方案：**
- 所有测试都用 RestAssured + RANDOM_PORT —— 慢，对常规 controller 断言没有任何收益。

**留口：** 未来端到端 OIDC 协议测试（比如完整的 redirect 跳转链路 + 真实 OAuth 客户端）**可以**用 `RANDOM_PORT`。第一波不做，但基类同时支持两种模式。

### D3. Failsafe 拆分 + `*IT.java` 命名

根 POM 中加入 `maven-failsafe-plugin`，绑定 `integration-test` 阶段和 `verify` 阶段。Failsafe 匹配 `**/*IT.java`，Surefire 保留 `**/*Test.java`。

**理由：**
- 单元测试保持快速 —— `mvn test` 永远不应该启动容器。
- 集成测试只在显式 opt-in（`mvn verify`）时跑，开发者在 `ulp-support` 跑 `mvn test` 时不付容器启动代价。
- 标准 Maven 惯例，所有 IDE 都支持。

**被驳回的方案：**
- 只用 Surefire + tag（`@Tag("integration")` + groups）—— 能行，但单元+集成会塞到同一个 Surefire execution 里，跳过语义和 IDE 行为都更复杂。
- 单独建 `*-it` Maven 模块 —— 把代码库切碎了，集成测试应该跟被测代码住在一起。

### D4. 按测试方法的事务回滚 + 显式 Redis 清理

每个 IT 方法运行在 Spring 管理的事务里，方法结束时回滚。涉及 Redis 写入的测试用 `@AfterEach` 调 `flushDb`。

**理由：** 事务回滚是 SQL 状态最干净的隔离方式 —— 不需要每个测试重建 schema，也不需要在 seed 数据之外再做测试级 setup。Redis 没有事务概念，所以显式清理是唯一选择。

**注意点：** 那些跨多个 HTTP 请求、controller 中间会自己提交事务的测试用不了这个模式。第一波没有这类测试。未来如果有，可以方法级 opt-out（`@Transactional(propagation = NEVER)` + 自己清理），不需要改基类。

**被驳回的方案：**
- `@DirtiesContext` —— 每个测试之间会重启整个 Spring context，耗时灾难。
- 每个测试 drop + recreate schema —— 每个方法多几秒成本。

### D5. 静态 `@Container` + 复用 opt-in

容器声明为基类上的 `static` 字段，加 `@Container`。复用通过开发者本机的 `~/.testcontainers.properties` 开启：
```
testcontainers.reuse.enable=true
```

**理由：** 静态容器每个 JVM 启动一次（即每次测试运行一次），不是每个测试类一次。复用更进一步 —— 开启后，容器在 Maven 调用链之间持久存活，后续 IT 运行从约 30 秒启动降到约 3 秒。

我们**不**在项目配置里默认开启复用。容器配置一变复用就会坏，必须让开发者显式 opt-in。

### D6. 通过 `ulp-support` test-jar 共享测试基础设施

测试脚手架从 `ulp-support` 以 test-jar 形式输出：

```xml
<!-- ulp-support/pom.xml -->
<plugin>
  <artifactId>maven-jar-plugin</artifactId>
  <executions>
    <execution>
      <goals><goal>test-jar</goal></goals>
    </execution>
  </executions>
</plugin>
```

消费方用 `<type>test-jar</type><scope>test</scope>` 引入。共享基础设施（基类、容器 fixture、Spring 测试配置）就出现在测试 classpath 里，不会泄漏到生产。

**理由：** `ulp-support` 本来就是所有模块都依赖的叶子模块；往它上面挂 test-scope 产物不会引入新的模块边。新建一个 `ulp-test-support` 模块反而会。

**被驳回的方案：**
- 新建 `ulp-test-support` 模块 —— 多一个模块、多一个 pom、多一份发布协调，没有清晰收益。
- 在每个消费模块里复制基类 —— 复制粘贴会腐烂。

### D7. 测试类布局

| 模块 | 测试类 | 方法 |
|---|---|---|
| OIDC 协议模块（implementation 阶段确认具体路径） | `OidcAuthorizationCodeFlowIT` | `happyPath`、`invalidClientId`、`expiredAuthorizationCode`、`mismatchedRedirectUri` |
| `ulp-console` | `UserControllerIT` | `listReturnsAllUsers`、`createPersistsAndReturnsUser`、`deleteRemovesUser` |
| `ulp-console` | `AppControllerIT` | `listReturnsAllApps`、`createPersistsAndReturnsApp`、`deleteRemovesApp` |
| `ulp-console` | `OrganizationControllerIT` | `listReturnsAllOrgs`、`createPersistsAndReturnsOrg`、`deleteRemovesOrg` |

OIDC 模块路径标注为"待定"，因为 `ulp-protocol/*` 的协议布局需要 implementation 时快速扫一遍确认（大概率是 `ulp-protocol-oidc`）。下面 Open Questions 里记着。

### D8. 基类形态

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
@Testcontainers
public abstract class AbstractIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = SharedContainers.mysql();

    @Container
    static GenericContainer<?> redis = SharedContainers.redis();

    @DynamicPropertySource
    static void wire(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }

    @Autowired protected MockMvc mockMvc;
}
```

`SharedContainers` 工厂类集中管理 image tag 和配置，版本升级是一处改动。

## Risks / Trade-offs

- **[风险] `mvn verify` 需要 Docker。** → 缓解：README 说明 Docker 前置依赖。未来 CI 接入时需要 Docker-capable runner；CI 集成本次不做。
- **[风险] 冷启动 IT 会增加约 30 秒容器启动时间。** → 缓解：复用 opt-in（D5）能把热启动降到几秒。这是业内标准模式，不是项目特定成本。
- **[风险] Liquibase 在每次容器启动时都跑一遍新 MySQL —— 慢。** → 缓解：开启复用后 schema 持久存在。不开复用的话，每个 JVM 一次约 5–10 秒。第一波可以接受。
- **[权衡] 按测试方法的 `@Transactional` 回滚对那些需要观察"跨提交状态"的测试不适用。** → 第一波不需要这种场景。未来有了再方法级 opt-out，不改基类。
- **[风险] 如果测试忘记 `flushDb`，Redis 状态会在测试类之间污染。** → 缓解：基类提供 `@BeforeEach` hook，如果继承自 `RedisIntegrationTest` 子基类就自动调 `clearRedis()`。不碰 Redis 的测试不继承它。
- **[风险] test-jar 消费方的依赖写法对新人不直观。** → 缓解：README + tasks.md 里把 `<type>test-jar</type>` 的写法写出来。

## Open Questions

- **OIDC 授权服务器逻辑住在哪个模块？** Task 4 实现时需要确认是 `ulp-protocol-oidc`、`ulp-authorization-server`、还是别的路径。不影响本设计 —— 设计契约不依赖 IT 具体住在哪个模块。
- **OIDC 的默认 client + 测试用户该用 SQL fixture 注入，还是 `@BeforeAll` 里调 service 创建？** 倾向 SQL fixture（更快、更确定），但先延后决定，等第一个 OIDC IT 写出来看到数据形状再说。
