# Phase 2 编译扫雷 — 错误清单（WIP）

执行命令：`./mvnw.cmd clean compile -Dlicense.skip=true -DskipTests`（JDK 21.0.11 + Boot 4.0.7）

执行时间：2026-06-11

## 现状

Maven 在 `ulp-support` 编译失败立即停止（Reactor 跳过 30+ 下游模块）。本文档收录两部分：
1. **直接报错**：ulp-support 当前可见的 12 个 javac 错误
2. **必修代码面**：通过 grep 静态扫描确认的 Jackson 2 / Security 7 改写工作量

下游模块（ulp-console、ulp-protocol、ulp-portal 等）的真实错误要等 ulp-support 修通后再次 compile 才会暴露。

---

## 第一类：Boot 4 autoconfigure 包大重组 — 9 处（ulp-support）

Plan tasks.md Phase 2.2 只列了 BootstrapRegistry，但实际范围更大：Spring Boot 4 把多个 `org.springframework.boot.autoconfigure.*` 子包的类挪到了独立 starter 自身，或重组到 `org.springframework.boot.*` 主包下。

| 旧 import (3.x) | 受影响文件 | 推测 Boot 4 新位置 |
|---|---|---|
| `org.springframework.boot.web.servlet.error.DefaultErrorAttributes` | `CustomErrorAttributes`, `CustomErrorConfiguration` | 待 Boot 4 javadoc / starter-web 验证 |
| `org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration` | `CustomErrorConfiguration` | 同上 |
| `org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer` | `CustomHibernatePropertiesCustomizer` | 可能挪到 `spring-boot-starter-data-jpa` 内 |
| `org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer` | `CustomJacksonObjectMapperBuilderCustomizer` | **Jackson 3 改名** → `JsonMapperBuilderCustomizer`（tasks.md Phase 3.3 已列） |
| `org.springframework.boot.autoconfigure.cache.CacheProperties` | `RedisConfiguration` | 可能挪到 `spring-boot-starter-cache` 内 |
| `org.springframework.boot.autoconfigure.data.redis.RedisProperties` | `RedisConfiguration` | 可能挪到 `spring-boot-starter-data-redis` 内 |
| `org.springframework.boot.autoconfigure.validation.ValidationConfigurationCustomizer` | `CustomValidationConfiguration` | 待 Boot 4 starter-validation 验证 |

**修复策略**：每个挂掉的类，先去 Boot 4.0.7 release notes + javadoc 找新位置；找不到的考虑是否能用 `@AutoConfigureBefore` + 配置类替代而非依赖 internal API。

---

## 第二类：Spring Security 7 移除 AntPathRequestMatcher — 56 文件

Spring Security 7 把 `AntPathRequestMatcher` 从 `org.springframework.security.web.util.matcher` 移除，统一改用 `PathPatternRequestMatcher`。

**ulp-support 直接报错（3 文件）**：
- `cn.frank.ulp.support.security.configurer.FormLoginConfigurer`
- `cn.frank.ulp.support.security.web.CustomAuthenticationFilter`
- `cn.frank.ulp.support.web.filter.CustomRedirectFilter`

**下游受影响（53 文件，下次 compile 必报）**：
- `ulp-authentication/*`：alipay/dingtalk/feishu/gitee/github/qq/wechat/wechatwork/mail/sms + core，30 文件
- `ulp-protocol/ulp-protocol-oidc/configurers/*`：12 个 endpoint configurer
- `ulp-protocol/ulp-protocol-oidc/endpoint/*`：2 个 endpoint filter
- `ulp-protocol/ulp-protocol-oidc/OidcOpenApiCustomizer`：1
- `ulp-protocol/ulp-protocol-form/configurers/FormAuthenticationEndpointConfigurer`：1
- `ulp-protocol/ulp-protocol-jwt/configurers/*`：2
- `ulp-console/configuration/ConsoleSecurityConfiguration`：1
- `ulp-portal/configuration/security/PortalSecurityConfiguration`：1
- `ulp-openapi/configuration/OpenApiSecurityConfiguration`：1

**修复策略**：批量替换 import + 构造方式。`AntPathRequestMatcher("/api/**")` → `PathPatternRequestMatcher.withDefaults().matcher("/api/**")`（具体 API 待 Phase 4 实施时核对）。

**Plan 偏差**：tasks.md Phase 4 只规划了 4 个 SecurityFilterChain (console/portal/openapi/SAS) 的 DSL 改写，但 `AntPathRequestMatcher` 在所有 endpoint configurer 里全在用，实际改写面 14× plan 估计。Phase 4 估时 4-8h 可能要上调到 12-16h。

---

## 第三类：Hibernate Validator 8 移除内部 API — 1 文件

| 旧 import | 文件 |
|---|---|
| `org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator` | `ulp-support/EmailUtils.java` |

Hibernate Validator 8 重组了 internal 包。`EmailValidator` 可能改名或挪走。
**修复策略**：业务上其实只需要"判断字符串是不是合法 email"，直接用 `jakarta.validation.constraints.Email` 注解 + `Validator.validate()`，或用 Apache Commons Validator 的 `EmailValidator`（这是稳定 public API），不该依赖 hibernate-validator internal 包。

---

## 第四类：Jackson 2 → 3 包名 — 154 处 / 11 模块

Plan tasks.md Phase 3 已规划，本次扫雷确认工作面：

| 模块 | Jackson 2 import 数 |
|---|---|
| ulp-support | 17 |
| ulp-console | 13 |
| ulp-protocol | 9 |
| ulp-authentication | 6 |
| ulp-common | 5 |
| ulp-application | 5 |
| ulp-core | 4 |
| ulp-portal | 3 |
| ulp-openapi | 2 |
| ulp-synchronizer | 1 |
| ulp-identity-source | 1 |
| **合计** | **154** |

**注**：`com.fasterxml.jackson.annotation.*` 不在此次统计内（按 plan 它在 Jackson 3 里保留 `com.fasterxml.jackson.annotation` 包）。

**其他 Jackson 标记**：
- `@JsonComponent` / `Jackson2ObjectMapperBuilderCustomizer` / `JsonObjectSerializer`：仅 `ulp-support/CustomJacksonObjectMapperBuilderCustomizer.java` 1 处使用

---

## 第五类：Spring Session 属性键迁移 — 待 Phase 5 启动后由 properties-migrator 暴露

Plan tasks.md Phase 5 已规划：临时引入 `spring-boot-properties-migrator`，启动 console/portal/openapi 观察 deprecation 警告。本阶段不预扫，Phase 5 处理。

---

## 下一步

按 plan Phase 3 + Phase 4 优先级：
1. **Phase 3**：Jackson 2 → 3（154 处全仓 import 包前缀替换 + 4 个序列化器手工核对）
2. **Phase 4**：Security 7 DSL 改写 + AntPathRequestMatcher 全仓批量替换（**实际工作面比 plan 大 14×**）
3. **Phase 6 部分前置**：先解决 Boot 4 autoconfigure 包重组问题（与 plan 顺序略偏，但是 ulp-support 编译不过则任何下游都跑不起来）

Phase 3 + 4 同时推进时，建议每修一个模块就跑一次 `mvn compile -pl <module>` 增量验证，而不是等全部改完再 compile。

---

## Plan 偏差总结（供后续 retro）

| 项 | Plan 预期 | 实际 | 影响 |
|---|---|---|---|
| Phase 2.2 错误分组 (d) BootstrapRegistry | 1 个包重组 | autoconfigure 包大重组 (7 类) | Phase 2 多一类工作 |
| Phase 4 SecurityFilterChain 个数 | 4 | 11（5 模块） | 估时上调 |
| Phase 4 AntPathRequestMatcher 受影响文件 | 隐式包含在 SecurityFilterChain 里 | 56 文件（多数在 configurer/filter 而非 SecurityConfig） | Phase 4 工作面 14× |
| Phase 2 一次性收集所有错误 | 假设 compile 能跑完 | ulp-support 失败即停，下游不可见 | 分批暴露 |
