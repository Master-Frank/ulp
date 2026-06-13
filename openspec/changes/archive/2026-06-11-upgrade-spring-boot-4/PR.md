# PR: Spring Boot 3.2.12 → 4.0.7 + Java 17 → 21

## Summary

把 ULP 整个反应堆从 Spring Boot 3.2.12 + Java 17 升到 Spring Boot 4.0.7 + Java 21 LTS。
顺带升级了 Boot 4 BOM 强制的整套同代框架（Spring Framework 7、Spring Security 7、Hibernate 7.2、Liquibase 5、Tomcat 11、Jakarta EE 11），以及 Jackson 2.x → 3.x（包名 `com.fasterxml.jackson.databind.*` → `tools.jackson.databind.*`）。

按 OpenSpec proposal 分 12 个阶段执行，每阶段独立可验证、独立 commit，最终：
- 41/41 模块 BUILD SUCCESS
- 13 IT + 5 UT 全绿，clean verify 3:31 min
- 三应用本地烟测全通过（console:1898 + portal:1989 + openapi:1988）

## Commits（按阶段顺序）

| # | Commit | 阶段 | 描述 |
|---|---|---|---|
| 1 | `7864db7` | proposal | docs(openspec): propose spring boot 3.2.12 → 4.0.5 + java 17 → 21 upgrade |
| 2 | `c38a108` | Phase 0 | test(baseline): pin spring boot 3.2.12 + java 17 before upgrade |
| 3 | `b1b73b0` | Phase 1 | chore(deps): bump spring-boot 3.2.12 → 4.0.7 + java 17 → 21 (parent only) |
| 4 | `4bcc0f6` | Phase 1 | chore(deps): let boot 4 BOM manage jackson-dataformat-xml |
| 5 | `0dc341d` | Phase 2 | chore(deps): document compile errors after spring-boot 4 bump (WIP) |
| 6 | `adad774` | Phase 3 | refactor(deps): migrate jackson 2.x → 3.x (com.fasterxml → tools.jackson) |
| 7 | `6c3fe03` | Phase 4 | refactor(deps): migrate to spring security 7 DSL + sb4 api relocations |
| 8 | `d47aab1` | Phase 5 | refactor(config): wire @EnableRedisIndexedHttpSession after sb4 dropped session autoconfig |
| 9 | `69e84c2` | Phase 6 | refactor(deps): verify hibernate 7.2 compatibility + drop unused jackson-datatype-hibernate6 |
| 10 | `7a7a7e2` | Phase 7 | refactor(deps): bump redisson 3.40.2 → 4.4.0 + springdoc 2.6.0 → 3.0.3 for SB4 |
| 11 | `39b2664` | Phase 8 | test(it): green IT suite under spring boot 4 + java 21 |
| 12 | `d5b8ab5` | Phase 9 | fix(jpa): switch bootstrap-mode deferred → default to dodge SB4+Hibernate7 deadlock |
| 13 | `e9ac742` | Phase 9 | refactor(session): extract springSessionDefaultRedisSerializer to lightweight config |
| 14 | `d150d90` | Phase 9 | test(smoke): verify console + portal + openapi running under spring boot 4 |
| 15 | `9e1b3c8` | Phase 10 | chore(deps): remove obsolete dependency overrides after spring boot 4 upgrade |
| 16 | `fd082fb` | Phase 11 | docs: update runtime requirements for spring boot 4 |
| 17 | _this PR_ | Phase 12 | chore(openspec): promote runtime-baseline spec + archive change |

## 关键技术决策

1. **Tomcat instead of Undertow** — Undertow upstream archived；Boot 4 移除 `spring-boot-starter-undertow`，本次切回默认 Tomcat 11
2. **Jackson 3 (`tools.jackson.*`)** — Boot 4 内部已迁移；本仓库主代码全切 Jackson 3；保留 `jackson-databind` 2.x 作为 transitive，供第三方 SDK（minio / aliyun-sdk-oss 等）使用
3. **Spring Session — 代码而非配置承载** — Boot 4 移除 `spring-boot-session` auto-config。`flush-mode` 走 `SessionRepositoryCustomizer` bean，`repository-type` 由 `@EnableRedisIndexedHttpSession` 注解选择
4. **JPA bootstrap-mode = default** — `deferred` 在 Boot 4 + Hibernate 7 触发 EMF 异步装配死锁（`MultiTenancy.getTenantIdentifierResolver` 抢 BeanFactory 锁），改 default 同步装配规避
5. **虚拟线程暂不启用** — `spring.threads.virtual.enabled=true` 留给后续 Actuator + 性能基线一起评估（#39）

## IT 套件耗时

| 模块 | clean verify (3.2.12) | clean verify (4.0.7) | 差值 |
|---|---|---|---|
| ulp-console | 未基线化 | 1:10 min | n/a |
| ulp-portal | 未基线化 | 51.9 s | n/a |
| ulp-openapi | 未基线化 | 4 s | n/a |
| 全反应堆 | 未基线化 | 3:31 min | n/a |

> **说明**：升级前 baseline 未捕获冷启动 timing（Phase 0 只 pin 了版本断言），无对比意义。后续若启用虚拟线程或调整 connection pool，以本次 4.0.7 数据为基线。

## 已知 issue / 后续动作

- 启动期良性 warning，**不阻塞合入**：
  - logback 1.5.x 弃用配置警告（待 logback 2.x 全 GA）
  - SpringDoc 默认 enabled 提示（已在 yml 显式 disable）
  - Spring Security `ignore vs permitAll` 建议（DSL 语义等价，下一波 refactor 处理）
- 虚拟线程评估归并到 #39 Actuator + 健康检查 ticket
- `BaselineVersionAssertionTest` → `RuntimeBaselineAssertionTest` 永久保留，断言 Boot 4.0.x + Java 21，未来任何降级会直接红测试

## Test plan

- [x] `./mvnw.cmd clean verify -Dlicense.skip=true -DskipTests=false -DskipITs=false` — 41/41 模块 SUCCESS
- [x] console:1898 HTTP 200，登录页 SPA 加载
- [x] portal:1989 HTTP 200，`UserUnlockTask` 调度任务执行
- [x] openapi:1988 HTTP 200，`/v3/api-docs` 返回 62KB OpenAPI 规范
- [x] 全仓 grep Jackson 2 残留 / antMatchers / javax.servlet / hibernate-jpamodelgen / deprecated session keys — 命中数 = 0
- [ ] 浏览器手测（未覆盖：登录后用户列表 / 创建用户 / 角色管理 / SSO 跳转 — 留给合入后人工验收）

## 关联

- OpenSpec change: `openspec/changes/archive/2026-06-11-upgrade-spring-boot-4/`（合入时归档）
- 新增 capability spec: `openspec/specs/runtime-baseline/spec.md`
- 关联 ticket: #33 (parent)、#39 (后续 Actuator + virtual threads)
