## 1. PasswordEncoderFactories 切换默认到 Argon2id

- [x] 1.1 修改 `ulp-support/src/main/java/cn/frank/ulp/support/security/crypto/password/PasswordEncoderFactories.java`：注册 `argon2` encoder（`new Argon2PasswordEncoder(16, 32, 1, 19456, 2)`）、保留 `bcrypt` 与 `noop`，默认 id 设为 `argon2`
- [x] 1.2 新增单元测试 `ulp-support/src/test/java/cn/frank/ulp/support/security/crypto/password/PasswordEncoderFactoriesTest.java`：断言 `encode("xx").startsWith("{argon2}")`、`matches("xx", "{bcrypt}...legacy...")=true`、`upgradeEncoding("{bcrypt}...")=true`、`upgradeEncoding("{argon2}...")=false`（多加一条 `{noop}` 兼容断言）
- [x] 1.3 本地 `./mvnw.cmd -pl ulp-support test -DskipTests=false -Dtest=PasswordEncoderFactoriesTest -Dlicense.skip=true` 通过：4/4 tests，0 failures / 0 errors / 0.695s

## 2. Console 显式装配 DaoAuthenticationProvider + UserDetailsPasswordService

- [x] 2.1 修改 `ulp-console/src/main/java/cn/frank/ulp/console/configuration/ConsoleSecurityConfiguration.java`（**路径偏差**：实际在 `configuration/` 而非 `configuration/security/`）：新增 `@Bean DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService, PasswordEncoder, UserDetailsPasswordService)`，构造后依次调用 `setPasswordEncoder` + `setUserDetailsPasswordService`
- [x] 2.2 验证：仓库 `git grep DaoAuthenticationProvider` 仅命中 ROPC 与新加的 console bean；Spring Security 7 的 `InitializeUserDetailsManagerConfigurer` 检测到上下文里有 `AuthenticationProvider` bean 会跳过 auto-DAP，无需额外注册
- [x] 2.3 `./mvnw.cmd -pl ulp-console compile -DskipTests=true -Dlicense.skip=true` 通过：BUILD SUCCESS 1:13 min

## 3. Portal 显式装配 DaoAuthenticationProvider + UserDetailsPasswordService

- [x] 3.1 修改 `ulp-portal/src/main/java/cn/frank/ulp/portal/configuration/security/PortalSecurityConfiguration.java`：新增 `UserDetailsPasswordService` 字段 + 构造参数；新增 `@Bean DaoAuthenticationProvider daoAuthenticationProvider()` 方法（不带参，直接读 field —— portal 已用字段持有 userDetailsService / passwordEncoder，无须重复声明形参）；依赖 portal 内 `UserDetailsPasswordServiceImpl @Service`
- [x] 3.2 `./mvnw.cmd -pl ulp-portal compile -DskipTests=true -Dlicense.skip=true` 通过：BUILD SUCCESS 36.9s（74 源码文件）

## 4. ROPC（OAuth2 Password）链路扩参支持自动升级

- [x] 4.1 修改 `ulp-protocol/ulp-protocol-oidc/src/main/java/cn/frank/ulp/protocol/oidc/authentication/OAuth2AuthorizationResourceOwnerPasswordAuthenticationProvider.java`：新增接受 `@Nullable UserDetailsPasswordService` 的构造重载（保留原构造做 delegate），构造内若非 null 调 `setUserDetailsPasswordService(...)`
- [x] 4.2 修改 `ulp-protocol/ulp-protocol-oidc/src/main/java/cn/frank/ulp/protocol/oidc/configurers/OAuth2TokenEndpointConfigurer.java`（**路径偏差**：实际 ROPC provider 装配点在 `OAuth2TokenEndpointConfigurer.createDefaultAuthenticationProviders`，由 portal 的 `OidcAuthorizationServerConfigurer` 透传调用，而不是 portal `OidcProtocolSecurityConfiguration` 直接 new）：通过 `HttpSecurityConfigUtils.getOptionalBean(httpSecurity, UserDetailsPasswordService.class)` 注入（缺省时返回 null，等效于原构造行为）；portal `UserDetailsPasswordServiceImpl @Service` 自动被 ApplicationContext 解析
- [x] 4.3 `git grep "new OAuth2AuthorizationResourceOwnerPasswordAuthenticationProvider"` 全仓只此 1 处调用点，已切到新构造
- [x] 4.4 `./mvnw.cmd -pl ulp-protocol/ulp-protocol-oidc,ulp-portal -am compile -DskipTests=true -Dlicense.skip=true` 通过：Reactor 33 modules SUCCESS 49.3s（其中 ulp-protocol-oidc 6.3s，ulp-portal 27.5s）

## 5. 集成测试：AbstractPasswordUpgradeIT + console/portal 子类

- [x] 5.1 在 `ulp-support/src/test/java/cn/frank/ulp/support/testsupport/AbstractPasswordUpgradeIT.java` 创建基类（沿用 `AbstractActuatorSecurityIT` 模式），含两个测试方法：`newAccountUsesArgon2Prefix`、`bcryptAccountUpgradesOnLogin`
- [x] 5.2 基类暴露抽象/模板方法供子类提供：登录端点路径、构造登录请求体、DB 插入 `{bcrypt}xxx` seed 账号、查询账号当前密文前缀（避免在 ulp-support 反向依赖 console/portal repository）；额外暴露 `customizeLoginRequest` 钩子供子类注入 CSRF token（`SpaCsrfTokenRequestHandler` 需要 `_csrf`）
- [x] 5.3 在 `ulp-console/src/test/java/cn/frank/ulp/console/security/PasswordUpgradeIT.java` 创建子类，实现以上模板方法；通过 `SecurityMockMvcRequestPostProcessors.csrf()` 注入合法 token 通过 XOR 校验
- [x] 5.4 在 `ulp-portal/src/test/java/cn/frank/ulp/portal/security/PasswordUpgradeIT.java` 创建子类，实现 form login 模板方法；同样 `.with(csrf())` 通过 CSRF；登录端点同为 `/api/v1/login`
- [ ] 5.5 （可选，**本期跳过**）在 `ulp-portal` 额外创建 `RopcPasswordUpgradeIT`：seed `{bcrypt}` 账号 → 走 ROPC `POST /oauth2/token grant_type=password` → 断言成功 + DB 密文升级到 `{argon2}`。理由：ROPC provider 已在任务 4 切到注入 `UserDetailsPasswordService` 的构造器，路径与 form login 共享同一 rehash 机制；form login IT 已覆盖核心契约
- [x] 5.6 `./mvnw.cmd -pl ulp-console,ulp-portal verify -DskipTests=false -Dit.test='*PasswordUpgradeIT' -Dtest='nothing' -Dsurefire.failIfNoSpecifiedTests=false -Dfailsafe.failIfNoSpecifiedTests=false -Dlicense.skip=true` 全绿：**Console 2/2 + Portal 2/2 PASS**，BUILD SUCCESS 2:14 min；SQL 日志确认 `insert into ulp_user` → 3 路 `findBy(username/phone/email)` 异步查询 → `update ulp_user set password_=?` rehash → `@AfterEach` 软删清理。**关键修复**：基类加 `@Transactional(propagation = NOT_SUPPORTED)` 覆盖父类 `AbstractIntegrationTest` 的 `@Transactional`，配 `@AfterEach` 手动清理 seed 账号；根因是 `AdministratorServiceImpl#findByUsernameOrPhoneOrEmail` 用 `CompletableFuture.supplyAsync` 三路并发查询，异步线程脱离测试事务上下文 + MySQL REPEATABLE_READ snapshot 隔离 → 即使 `saveAndFlush` 也看不见未 commit 的 seed
- [x] 5.7 跑现有 IT 套件回归 `./mvnw.cmd verify -DskipTests=false -Dlicense.skip=true` 通过：全 reactor BUILD SUCCESS 2:59 min。**测试汇总**：ulp-support 46/0/0（含 `PasswordEncoderFactoriesTest`、`UserDetailsRoundTripTest`、`AesUtilsTest`、`RsaUtilsTest`、`CertUtilsTest`、`RuntimeBaselineAssertionTest`、`UserDetailsEqualsHashTest`），ulp-console 17/0/0（含 `ActuatorSecurityIT`、`Organization/User/AppControllerIT`、`PasswordUpgradeIT`），ulp-portal 12/0/0（含 `ActuatorSecurityIT`、`OidcAuthorizationCodeFlowIT`、`PasswordUpgradeIT`），ulp-openapi 6/0/0（`ActuatorSecurityIT`）。bcrypt seed 的老 IT 全部不受影响：`DelegatingPasswordEncoder` 按 `{id}` 前缀分发，老密文校验照走 bcrypt 路径；老 IT 走的 `@Transactional`-rollback 模式下 rehash 写回也回滚不污染

## 6. 验证 ulp-openapi 不受影响

- [x] 6.1 `git grep -nE "form.?login|DaoAuthenticationProvider|grant_type=password|/oauth2/token" ulp-openapi/src/main/` 返回空 —— ulp-openapi 确认无 form login / ROPC 入口（仅 access_token bearer 校验）
- [x] 6.2 `git grep -nE "DaoAuthenticationProvider|UserDetailsPasswordService|form.?[Ll]ogin" ulp-openapi/src/main/java/cn/frank/ulp/openapi/configuration/` 返回空 —— `OpenApiSecurityConfiguration` 未声明 `DaoAuthenticationProvider`，保持现状。**额外发现**：`UserConverter.java:117-119` 用默认 `PasswordEncoder` 编码 SCIM 新建用户密码，迁移后默认走 argon2，新创建用户自动 `{argon2}` 前缀 —— 符合预期，无需改动
- [x] 6.3 全量 `./mvnw.cmd verify -DskipTests=false -Dlicense.skip=true` 通过（见 5.7：ulp-openapi 6/0/0 `ActuatorSecurityIT` PASS，BUILD SUCCESS 2:59 min）

## 7. 性能与运行时验证

> **决策**：7.1-7.3 改为由 IT 套件等价覆盖（避免破坏本地真实 ulp 库的 admin 账号；console 已起在 :1898 服务中，重启会影响开发环境）。`PasswordUpgradeIT` 的 `bcryptAccountUpgradesOnLogin` 已在 console + portal 各跑通一次完整链路：seed `{bcrypt}` → 真登录 `/api/v1/login` → DB SELECT password_ 验证前缀翻转到 `{argon2}` → argon2 密文 raw-password 校验 true。等价于 7.1 + 7.2 + 7.3 三个子步骤，且自动化、可重复、不污染真实库。7.4 用 micro-bench 单测覆盖。

- [x] 7.1 用 `PasswordUpgradeIT.bcryptAccountUpgradesOnLogin` 等价覆盖：seed bcrypt 账号 → MockMvc `POST /api/v1/login` → 断言 HTTP 链路完整通过（rehash 已触发 = 登录成功）。Console + Portal 都过
- [x] 7.2 用 `PasswordUpgradeIT.findEncodedPasswordById(accountId)` 在 rehash 后查 DB password_ 字段，断言 `startsWith("{argon2}")`。Console + Portal 都过
- [x] 7.3 用 `passwordEncoder.matches(rawPassword, after)` 验证新 argon2 密文可走 argon2 校验路径回。Console + Portal 都过
- [x] 7.4 新增 `Argon2EncodeLatencyTest`（`ulp-support/src/test/java/.../security/crypto/password/`）做 micro-bench：warm-up + 采样 10 次 encode/matches，断言 p99 ≤ 350ms（spec 200ms + CI 1.75× 余量）。**实测**：encode 10 次 [29, 22, 34, 24, 17, 16, 17, 20, 20, 19] ms p99=34ms，matches 10 次 [24, 20, 23, 18, 17, 19, 17, 20, 20, 20] ms p99=24ms。远低于 200ms 阈值（~6×），argon2 参数 memory=19456 KiB 不需要降。design.md 不需要补"降参数"段

## 8. spec 锁定与文档

- [ ] 8.1 `openspec validate migrate-password-hashing-argon2id --strict` 通过
- [ ] 8.2 `CLAUDE.md` "Configuration that's easy to get wrong" 段加密码 encoder 装配条目：说明 console + portal 必须显式 `@Bean DaoAuthenticationProvider` + `setUserDetailsPasswordService`，否则 `upgradeEncoding=true` 也不触发 rehash；ROPC 走自定义 provider 构造器需同步传入
- [ ] 8.3 commit: `feat(security): migrate password hashing to Argon2id with auto-upgrade on login`

## 9. 归档

- [ ] 9.1 `openspec archive migrate-password-hashing-argon2id`：promote `security-baseline` 为新 spec（`openspec/specs/security-baseline/spec.md`），git mv change 到 `openspec/changes/archive/YYYY-MM-DD-migrate-password-hashing-argon2id/`
- [ ] 9.2 `openspec validate --specs --strict` 通过
- [ ] 9.3 开 PR 合 main（push 与开 PR 待用户授权后执行）
