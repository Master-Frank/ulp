## ADDED Requirements

### Requirement: 密码哈希算法基线

平台 SHALL 使用 **Argon2id** 作为新写入密文的密码哈希算法。所有通过 `PasswordEncoder.encode(...)` 产生的新密文 MUST 带 `{argon2}` 前缀。

Argon2id 参数下限 SHALL 满足：
- `memory ≥ 19456 KB`（≈ 19 MB，对齐 OWASP 2023 Password Storage Cheat Sheet 推荐值）
- `iterations ≥ 2`
- `parallelism ≥ 1`
- `saltLength ≥ 16`（bytes）
- `hashLength ≥ 32`（bytes）

降低任一参数 MUST 通过新提案修改本 Requirement 才允许。

#### Scenario: 新创建账号密文使用 Argon2id
- **WHEN** 通过任意路径（管理后台创建 / 用户自助注册 / 改密）写入新密文
- **THEN** 写入 DB 的密文以 `{argon2}` 前缀开头，且解析后 `memory ≥ 19456`、`iterations ≥ 2`

#### Scenario: 工厂默认 encoder 为 argon2
- **WHEN** 解析 `cn.frank.ulp.support.security.crypto.password.PasswordEncoderFactories.createDelegatingPasswordEncoder()` 返回的 `DelegatingPasswordEncoder`
- **THEN** 其默认 encoder id 为 `argon2`（即 `encode("xx")` 输出以 `{argon2}` 开头）

### Requirement: Legacy bcrypt 密文必须继续可校验

平台 SHALL 保留 `bcrypt` encoder 注册在 `DelegatingPasswordEncoder` 中，确保历史 `{bcrypt}` 前缀的密文仍可通过登录校验。

MUST NOT 将 `bcrypt` encoder 从注册表移除，除非有独立 spec change 同时规定批量重置剩余 bcrypt 用户密码的迁移方案。

#### Scenario: bcrypt 密文校验路径仍通
- **WHEN** DB 中存在密文 `{bcrypt}$2a$10$...`，用户用对应明文登录
- **THEN** `PasswordEncoder.matches(rawPassword, encodedPassword)` 返回 `true`

### Requirement: 老密文登录后自动升级到 Argon2id

任何使用密码登录的 `DaoAuthenticationProvider` 实例 SHALL 显式调用 `setUserDetailsPasswordService(...)`，并由对应 `UserDetailsPasswordService` 实现把新密文写回持久层。

适用范围：
- ulp-console form login 链路
- ulp-portal form login 链路
- ulp-portal OIDC ROPC（Resource Owner Password Credentials）链路

ulp-openapi 不接密码登录入口，无需声明该装配。

`DelegatingPasswordEncoder.upgradeEncoding(encoded)` 返回 `true` 时（即密文前缀 != 当前默认 `{argon2}`），认证成功后 MUST 触发 `UserDetailsPasswordService.updatePassword(user, newEncodedPassword)` 写回 DB。

#### Scenario: bcrypt 用户登录成功后 DB 密文升级到 argon2
- **WHEN** DB 种入一行 `{bcrypt}$2a$10$...` 的账号，用户通过 form login 用正确明文登录并成功
- **THEN** 重新查询 DB，该账号密文前缀变为 `{argon2}`，且原明文仍可校验通过

#### Scenario: DaoAuthenticationProvider 装配检查
- **WHEN** 评审 ulp-console / ulp-portal 的 `SecurityConfiguration`
- **THEN** 存在显式声明的 `DaoAuthenticationProvider` @Bean，其构造或初始化路径中调用了 `setUserDetailsPasswordService(...)`

#### Scenario: ROPC 链路升级
- **WHEN** ulp-portal `OidcProtocolSecurityConfiguration` 构造 `OAuth2AuthorizationResourceOwnerPasswordAuthenticationProvider`
- **THEN** 构造路径中传入 `UserDetailsPasswordService` 并被 set 到 provider 上（确保 ROPC 登录的 bcrypt 用户也能自动升级）

### Requirement: NoOp encoder 仅限测试使用

`NoOpPasswordEncoder` SHALL 保留在 `DelegatingPasswordEncoder` 注册表中以兼容现有测试 fixture，但生产环境 MUST NOT 写入 `{noop}` 前缀的新密文。

#### Scenario: 生产代码不主动产生 noop 密文
- **WHEN** grep `src/main/java` 全部 `.java`
- **THEN** 不出现 `NoOpPasswordEncoder.getInstance().encode(...)` 或字符串字面量 `{noop}` 拼接到 DB 写入路径
