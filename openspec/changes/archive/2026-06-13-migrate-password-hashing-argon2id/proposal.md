## Why

ULP 是身份平台，密码哈希算法是核心安全控制。当前 `CustomSecurityBeansConfiguration` 暴露的 `PasswordEncoder` 默认走 `bcrypt`（仓库自有的 `PasswordEncoderFactories` 仅注册了 `bcrypt` + `noop`），bcrypt 在 2000s 设计、对 GPU 暴力破解抗性已弱于 Argon2 / scrypt。OWASP 2023+ 与 NIST SP 800-63B Rev.4 (draft) 都把 Argon2id 列为密码哈希首选。

迁移到 Argon2id 后能：
- 抬升新建账号的离线破解成本（默认参数下单次校验 ~50ms）
- 老 bcrypt hash 不动也能登录，登录成功瞬间在 DB 中自动 rehash 到 Argon2id，自然衰减无需停机
- 在 `security-baseline` spec 中锁死算法选择 + 自动升级要求，防后续 PR 静默退回 bcrypt

## What Changes

- `ulp-support` 的 `PasswordEncoderFactories.createDelegatingPasswordEncoder()`：默认 id 从 `bcrypt` 改为 `argon2`；同时注册 `argon2` (`Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()`) 与现有 `bcrypt` / `noop`（后两者保留用于校验老密文）
- 校验三个 `SecurityConfiguration`（ulp-console / ulp-portal / ulp-openapi）+ `OAuth2AuthorizationResourceOwnerPasswordAuthenticationProvider`：所有 `DaoAuthenticationProvider` 实例必须显式 `setUserDetailsPasswordService(...)`，否则 `upgradeEncoding` 返回 true 也不会触发 rehash 写回
- 新增集成测试：
  - 新建账号密文必须以 `{argon2}` 开头
  - 用 `{bcrypt}` 前缀的旧密文 seed 账号 → 登录成功 → DB 中密文已变 `{argon2}` 前缀（一次登录完成 silent upgrade）
  - bcrypt seed 账号登录后再次登录走 argon2 路径
- 新建 `security-baseline` capability spec，锁定：
  - 密码哈希默认算法 = Argon2id，参数下限（memoryKb / iterations / parallelism）
  - bcrypt / noop 仅作为 legacy 校验保留，**禁止**新建密文使用
  - `DaoAuthenticationProvider` 必须装配 `UserDetailsPasswordService`，老 hash 登录后必须自动 upgrade

**非破坏性**：所有现有 bcrypt 老用户继续正常登录，无需重置密码；新注册 / 改密走 Argon2id。

## Capabilities

### New Capabilities
- `security-baseline`: 锁定 ULP 平台级安全控制基线（首期：密码哈希算法 + 自动升级），后续可扩展到 CSRF / 会话 / TLS 等

### Modified Capabilities
- (none)

## Impact

- **代码**：
  - `ulp-support/.../PasswordEncoderFactories.java`（核心改动）
  - 各 `SecurityConfiguration` 中 `DaoAuthenticationProvider` 注入（若尚未注入需补）
  - `ulp-support/.../testsupport/` 新增 `AbstractPasswordUpgradeIT`（与 actuator IT 同模式，由 console / portal 子类继承）
- **依赖**：`spring-security-crypto` 已含 `Argon2PasswordEncoder`（spring-security 6+ 自带，无需新依赖；可选引入 `bouncycastle` 提速但非必须 —— JDK 21 自带 Argon2 实现性能可接受）
- **API**：无 public API 变更，纯内部行为
- **数据**：现存 bcrypt 密文不动；新写入密文带 `{argon2}` 前缀。无需 schema migration
- **运行时**：单次密码校验耗时 ~50ms（bcrypt 默认 cost 10 约 ~80ms），登录延迟基本持平
- **风险**：DAP 未注入 `UserDetailsPasswordService` 时静默不升级 —— IT 覆盖此场景
