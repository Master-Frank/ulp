## 1. 构建系统

- [x] 1.1 在根 `pom.xml` 的 `<dependencyManagement>` 中 import `testcontainers-bom`
- [x] 1.2 在 `<dependencyManagement>` 中加入 `testcontainers`、`mysql`（testcontainers）、`junit-jupiter`（testcontainers）的托管依赖，scope 为 `test`
- [x] 1.3 在根 `<build><pluginManagement>` 中加入 `maven-failsafe-plugin`，绑定 `integration-test` 和 `verify` 阶段，默认 include `**/*IT.java`
- [x] 1.4 确认 Surefire 配置仍只匹配 `**/*Test.java`，与 Failsafe 没有重叠
- [x] 1.5 `mvnw.cmd verify -pl ulp-support` 跑通现有 5 个单元测试，且零 IT 测试，整体 pass

## 2. ulp-support 中的共享测试基础设施

- [x] 2.1 在 `ulp-support/pom.xml` 中加入 `maven-jar-plugin` 的 `test-jar` execution
- [x] 2.2 新建 `ulp-support/src/test/java/.../testsupport/SharedContainers.java`，提供工厂方法返回配置好的 `MySQLContainer` 和 `GenericContainer`（Redis 7-alpine，端口 6379）
- [x] 2.3 新建 `ulp-support/src/test/java/.../testsupport/AbstractIntegrationTest.java`，按 design D8 装配 `@SpringBootTest(MOCK)` + `@AutoConfigureMockMvc` + `@Transactional` + `@Testcontainers` + `@DynamicPropertySource`
- [x] 2.4 新建 `ulp-support/src/test/java/.../testsupport/RedisIntegrationTest.java`，作为 Redis 相关测试的子基类，加入 `@AfterEach flushDb()`
- [x] 2.5 编译 `ulp-support` 测试代码，确认 `target/` 下产出了 test-jar

## 3. 把 ulp-console 接成第一个消费方

- [x] 3.1 在 `ulp-console/pom.xml` 加入 `<dependency><groupId>...</groupId><artifactId>ulp-support</artifactId><type>test-jar</type><scope>test</scope></dependency>`
- [x] 3.2 写一个一行的烟雾测试（`SmokeIT extends AbstractIntegrationTest`），断言 `mockMvc` 非空
- [x] 3.3 `mvnw.cmd verify -pl ulp-console` 启动容器、跑 Liquibase、执行 `SmokeIT`，pass
- [x] 3.4 验证通过后删掉 `SmokeIT`（它只是脚手架检查，不是永久测试）

## 4. OIDC 集成测试

- [ ] 4.1 确认哪个协议模块持有 OIDC 授权服务器（大概率是 `ulp-protocol/ulp-protocol-oidc`），把 test-jar 依赖加进它的 `pom.xml`
- [ ] 4.2 决定并记录 OIDC client + 测试用户的种子数据策略（SQL fixture vs `@BeforeAll` 服务调用）—— 对应 design 的 open question
- [ ] 4.3 写 `OidcAuthorizationCodeFlowIT.happyPath()` —— 请求 `/oauth2/authorize`、跟随 redirect、用 `/oauth2/token` 换 access token、断言 token 形状
- [ ] 4.4 写 `OidcAuthorizationCodeFlowIT.invalidClientId()` —— 未知 client_id 返回配置中的错误响应
- [ ] 4.5 写 `OidcAuthorizationCodeFlowIT.expiredAuthorizationCode()` —— 授权码 TTL 过期后再换 token，返回 `invalid_grant`
- [ ] 4.6 写 `OidcAuthorizationCodeFlowIT.mismatchedRedirectUri()` —— 用未注册的 redirect_uri 换 token，返回 `invalid_grant`

## 5. Console controller 集成测试

- [ ] 5.1 写 `UserControllerIT`（list / create / delete）
- [ ] 5.2 写 `AppControllerIT`（list / create / delete）
- [ ] 5.3 写 `OrganizationControllerIT`（list / create / delete）
- [ ] 5.4 每个测试断言 HTTP 状态码、响应体形状、以及数据库副作用（在独立事务中后查一次，验证回滚生效）

## 6. 文档

- [ ] 6.1 在顶层 `README.md` 增加"运行集成测试"章节：Docker 前置依赖、`mvnw.cmd verify` 入口、`~/.testcontainers.properties` 的复用 opt-in 写法
- [ ] 6.2 在 `ulp-support/src/test/java/.../testsupport/package-info.java` 中说明下游模块如何消费该 test-jar

## 7. 端到端验证

- [ ] 7.1 干净 checkout 上 `mvnw.cmd clean verify` 跑通 —— 5 个单元测试 + 4 个 IT 类 + 约 16 个 IT 方法全部绿
- [ ] 7.2 同一份 checkout 上 `mvnw.cmd clean test` 跑通且不启动任何容器（运行期间用 `docker ps` 验证）
- [ ] 7.3 测一次冷启动 + 热启动 IT 套件耗时，把两个数字写进 implementation PR 描述里供后续参考
