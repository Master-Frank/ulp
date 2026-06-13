## Context

ULP 当前密码哈希链路：
- `ulp-support/.../CustomSecurityBeansConfiguration` 暴露 `PasswordEncoder` @Bean，调用仓库自有的 `PasswordEncoderFactories.createDelegatingPasswordEncoder()`
- 该工厂注册 `bcrypt` (`BCryptPasswordEncoder` 默认 cost 10) + `noop`，默认 id = `bcrypt`
- `ulp-console` 与 `ulp-portal` 各自有 `UserDetailsServiceImpl` 与 `UserDetailsPasswordServiceImpl`（`@Service` bean，impl 已完整：调 `Repository.updatePassword(id, newPassword, now)` 写 DB）
- 但 Console / Portal 主登录依赖 Spring Security 自动构建的 `DaoAuthenticationProvider`，**该自动 DAP 不会自动注入 `UserDetailsPasswordService`**（Spring Security 6/7 `InitializeUserDetailsManagerConfigurer` 只接 `UserDetailsService` + `PasswordEncoder`，不接 password service）
- ROPC 自定义 `OAuth2AuthorizationResourceOwnerPasswordAuthenticationProvider extends DaoAuthenticationProvider` 构造器也只 `setPasswordEncoder(...)`，无 `setUserDetailsPasswordService(...)`

结论：现状下即使把默认 encoder 换 Argon2id，老 bcrypt 用户登录后 **不会自动 rehash** —— `upgradeEncoding` 会被调到 returns true，但没有 password service 可调 → silent no-op。

约束：
- 不允许批量 reset 用户密码（明文已丢、用户体验差）
- 不允许 schema migration（旧 hash 留在原列即可）
- 现存所有 IT 使用 bcrypt seed 数据，必须继续可登录

## Goals / Non-Goals

**Goals:**
- 新建账号 / 改密 → Argon2id 哈希
- 老 bcrypt 用户登录成功后 DB 中密文自动重写为 `{argon2}` 前缀
- 在 `security-baseline` spec 中锁定算法 + 参数下限 + 自动升级要求
- IT 直接覆盖"老 bcrypt 登录 → DB 验证密文升级"路径，避免静默回归

**Non-Goals:**
- 不停掉 bcrypt 校验能力（必须保留以校验未升级的老密文）
- 不淘汰 `noop`（test fixture 仍用）
- 不做 PBKDF2 / scrypt 评估（OWASP 已直接推荐 Argon2id，不重复造决策）
- 不动 OIDC 协议层的 token 签名 / JWT 算法（与密码哈希正交）
- 不做密码强度 / 复杂度策略改动（独立维度）

## Decisions

### Decision 1：算法 = Argon2id；参数 = OWASP 2023 推荐 + Spring 默认对齐

- 算法 `Argon2id`：抗 GPU / ASIC，内存硬性需求；OWASP Password Storage Cheat Sheet 与 NIST SP 800-63B Rev.4 (draft) 一致推荐
- 参数：`new Argon2PasswordEncoder(saltLength=16, hashLength=32, parallelism=1, memoryInKB=19456, iterations=2)`
  - 比 Spring `defaultsForSpringSecurity_v5_8()` 的 `(16, 32, 1, 16384, 2)` 略保守，对齐 OWASP 2023 最低
  - 单次校验耗时实测目标 < 100ms（JDK 21 服务端硬件），登录延迟不显著恶化
- 不引入 BouncyCastle —— JDK 21 + spring-security-crypto 自带 Argon2 实现已够用

**备选**：Spring 默认参数。否决理由：OWASP 19 MB 是明文公开推荐，stick to it 便于审计。

### Decision 2：encoder 注册三件套：argon2（默认）+ bcrypt（legacy 校验）+ noop（test fixture）

- `PasswordEncoderFactories.createDelegatingPasswordEncoder()` 改为：
  ```java
  String defaultEncoder = "argon2";
  encoders.put("argon2", argon2Encoder());
  encoders.put("bcrypt", new BCryptPasswordEncoder());
  encoders.put("noop", NoOpPasswordEncoder.getInstance());
  return new DelegatingPasswordEncoder(defaultEncoder, encoders);
  ```
- `DelegatingPasswordEncoder` 自动按 `{id}` 前缀路由校验；`upgradeEncoding(encoded)` 仅当前缀 != 默认 id 时返回 true → 老 bcrypt 自动标记 upgrade
- bcrypt 保留无期限 —— 强制下线需要先有 metric 证明 `{bcrypt}` 密文占比降到可接受阈值，非本期范围

### Decision 3：显式声明 `DaoAuthenticationProvider` @Bean，注入 `UserDetailsPasswordService`

否决"靠 Spring 自动 DAP"路径：自动 DAP 不接 password service → 自动升级失效。

采用：在 `ulp-support` 提供共享构造方法 `DaoAuthenticationProviders.create(UserDetailsService, PasswordEncoder, UserDetailsPasswordService)`，console / portal 各自的 `SecurityConfiguration` 用它声明 `@Bean DaoAuthenticationProvider`：

```java
@Bean
DaoAuthenticationProvider daoAuthenticationProvider(
        UserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder,
        UserDetailsPasswordService userDetailsPasswordService) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder);
    provider.setUserDetailsPasswordService(userDetailsPasswordService);
    return provider;
}
```

Spring Security 检测到上下文里有 `AuthenticationProvider` bean 会优先用，覆盖 auto-DAP。

ulp-openapi 无密码登录入口（走 access_token），**不**声明该 bean。

### Decision 4：ROPC `DaoAuthenticationProvider` 子类扩构造器

`OAuth2AuthorizationResourceOwnerPasswordAuthenticationProvider` 当前构造器：
```java
public ROPC(UserDetailsService uds, OAuth2AuthorizationService as, OAuth2TokenGenerator tg, PasswordEncoder pe)
```

新增重载 / 改为接收可选 `UserDetailsPasswordService`：
```java
public ROPC(UserDetailsService uds, OAuth2AuthorizationService as, OAuth2TokenGenerator tg, PasswordEncoder pe, @Nullable UserDetailsPasswordService upds) {
    super(uds);
    // ... existing
    setPasswordEncoder(pe);
    if (upds != null) setUserDetailsPasswordService(upds);
}
```

调用方（portal `OidcProtocolSecurityConfiguration`）改造为注入并传入。ROPC 与 form login 升级路径一致。

### Decision 5：IT 通过 `AbstractPasswordUpgradeIT` 复用，console + portal 各自子类继承

- 基类放 `ulp-support/src/test/java/cn/frank/ulp/support/testsupport/AbstractPasswordUpgradeIT.java`（沿用 `AbstractActuatorSecurityIT` 模式）
- 测试用例：
  1. `newAccountUsesArgon2Prefix`：通过 service 层 / repo 创建新账号，密文以 `{argon2}` 开头
  2. `bcryptAccountUpgradesOnLogin`：直接往 DB 插一行 `{bcrypt}xxx` 的账号 → 触发 form login → 200 → 重新查 DB，密文已变 `{argon2}`
  3. `bcryptAccountReloginUsesArgon2`：上一步完成后再次登录，校验路径走 argon2（用 mock 或反射检查 DelegatingPasswordEncoder 校验链路日志）—— 此条可降为可选
- 加 unit test `Argon2PasswordEncoderConfigTest`：断言 `PasswordEncoderFactories` 创建的 encoder 默认 id = argon2、注册了 bcrypt + noop、`upgradeEncoding("{bcrypt}xxx")` = true

### Decision 6：不动现有 IT 的测试数据 seed

- 已有 OIDC / Controller IT 在 `@Sql` / fixture 里用 `{bcrypt}xxx` seed admin 账号
- 切换默认到 argon2 后这些账号校验路径走 bcrypt 分支，**仍能通过**（DelegatingPasswordEncoder 按前缀分发）
- 但首次成功登录会触发 rehash → 写回 DB → 测试结束 `@Transactional` rollback → 不污染
- 唯一风险：测试断言里 `assertThat(password).startsWith("{bcrypt}")` 的会挂 —— grep 一遍确认无此断言

## Risks / Trade-offs

- **[Argon2 校验耗时增加]** Argon2id 19 MB memory + 2 iter 实测 ~50–80ms vs bcrypt cost 10 ~80ms → 持平。**Mitigation**：上线前在 ulp-console 跑一次本地登录压测（10 并发 / 100 req）记录 p50/p99，若 p99 > 200ms 调低 memory 到 12 MB 重测
- **[DAP @Bean 覆盖 Spring 默认行为可能影响其他 provider]** 显式 DAP @Bean 会让 Spring Security `InitializeUserDetailsManagerConfigurer` 跳过自动注册 → 不影响（我们本来就只用 DAO 这一种 provider，加 LDAP / OAuth2 那些都是独立 provider）。**Mitigation**：IT 跑现有所有登录方式（form / OIDC AC / ROPC）确认无回归
- **[Bcrypt 老密文长期残留]** 用户不登录就不升级 → 长期混用。**Mitigation**：本期不强制收口；后续可加 metric `password_hash_algorithm{algo="bcrypt"}` 暴露在 prometheus，等占比 < 5% 再决策强制重置
- **[ROPC `@Nullable UserDetailsPasswordService` 注入失败]** Spring autowire `@Nullable` 在 javax → jakarta 迁移后行为偶有变化。**Mitigation**：用 `ObjectProvider<UserDetailsPasswordService>` 更稳，调用方 `.getIfAvailable()`
- **[Argon2PasswordEncoder 在 fork/join 池里高并发 OOM]** memory hard 算法多线程压测可能撞 JVM heap。**Mitigation**：默认 parallelism=1 → 单次调用单线程；并发由请求线程承担，与连接池水位耦合。线上发现问题先看 hikari + 容器内存，再考虑降 memory KB

## Migration Plan

1. 部署本期改动 → 新建密文走 argon2，老密文继续校验
2. 观察登录流量 + p99 一周 → 无回归
3. （可选，本期不做）加 micrometer 自定义 metric 暴露 `{bcrypt}` 占比
4. （未来某期）当 bcrypt 占比足够低，单独 spec change 强制重置剩余 bcrypt 用户密码 → 移除 bcrypt encoder

**回滚**：把 `PasswordEncoderFactories` 默认 id 改回 `bcrypt` 即可。已升级到 argon2 的密文继续可校验（encoder 仍注册），完全反向兼容。

## Open Questions

- 是否需要把 argon2 参数也写进 `application.yml` 暴露成可配置？倾向 **否**：参数选择是 security 决策不是运维 knob，藏在代码里防误调。如果需要不同环境用不同参数（如开发环境用更弱参数加速 IT），用 Spring Profile 切换 `PasswordEncoderFactories` 子类，比 yml 键更显式
