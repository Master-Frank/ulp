## Why

ULP 当前跑在 Spring Boot **3.2.12**，本身已于 2024-11 EOL。截至 2026-06-11，整个 Spring Boot 3.x 系列要么已 EOL（3.2 / 3.3 / 3.4），要么即将 EOL（3.5 → 2026-06-30，19 天）。OSS 唯一受支持的版本是 **4.0.x**（支持到 2026-12-31）和 **4.1.x**。

不升级的实际代价：
- 安全补丁停发 —— 任何在 3.x 被发现的 CVE 都不会 backport，必须自己 fork patch 或买 HeroDevs NES。
- 依赖锁死 —— Spring Security 7、Spring Authorization Server 1.5+、Hibernate 7 都已只发布 4.x 兼容版本，下游依赖升级窗口逐渐关闭。
- **#39 (Actuator advanced tier) 阻塞** —— Spring Boot Actuator 4.0 的 endpoint 配置 API 与 3.x 不兼容，先做 #39 等于做完之后还要再迁一次。

不升级到 4.1（直接跳）的理由：
- 4.1 计划 2026-11 GA，目前还未发布。
- 4.1 强制 Java 21；本次评估后我们选择**同步升 21**，但 4.1 同时带来的其他 breaking change 当前没有完整迁移文档。
- 4.0 → 4.1 是 minor bump，下次升级是 mechanical work；而 3.2 → 4.0 是 major + Jackson 3 + Security 7，必须先吃下这一刀。

## What Changes

- spring-boot-starter-parent **3.2.12 → 4.0.x latest patch**（写本提案时 4.0.5，实施时取最新）
- 同步升级 Spring Framework 6.1 → **7.0**、Spring Security 6.2 → **7.0**、Spring Session 3.x → **4.0**、Spring Data 2024.x → **2025.1**
- **Hibernate 6.4 → 7.1**（`hibernate-jpamodelgen` 改名 `hibernate-processor`、Dialect 类调整）
- **Liquibase 4.x → 5.0**（changelog 形态保持，但 driver 接口签名有变）
- **Jackson 2.x → 3.x**：包名 `com.fasterxml.jackson` → `tools.jackson`（注解包除外）；本仓库所有自定义反序列化器、`@JsonComponent`、`@JsonTypeInfo`、Spring Session 的 `Jackson2JsonRedisSerializer` 配置都要改。这是本次工作量最大的一项。
- **Spring Authorization Server starter 1.2.x → 1.5.x**（同 SF7 适配；API 大体不变）
- **Java 17 → 21**：Boot 4.0 仍允许 17 但 4.1 强制 21，提前到位避免 6 个月后再迁一次；同时打开 virtual thread / sealed type / pattern matching for switch 等使用空间
- **application.yml 属性迁移**：`spring.session.redis.*` → `spring.session.data.redis.*`（如启用 Mongo 也要迁，本项目未用）
- **包路径迁移**：`BootstrapRegistry` / `EnvironmentPostProcessor` 从 `org.springframework.boot[.env]` 移到 `org.springframework.boot[.bootstrap]`，本仓库扫描确认无引用即可
- **Jakarta EE 9 → 11 / Servlet 6.0 → 6.1**（包名已是 jakarta，无变化；个别 Servlet API 方法签名调整）
- **被驳回的方案 / Non-goals**：
  - **直接跳 4.1**：未 GA，breaking change 信息不全；不可控
  - **保留 3.5 等买 NES**：增加付费依赖、且 3.5 也只是续命，最终还得升 4.x
  - **本次不升 Java 21**：会把"无回归网"的迁移拆成两步，6 个月内重做一次反而更累
  - **本次完成 SAS 多租户 / Spring Cloud 整体升级**：与本次升级正交，分别立项
  - **CI 接入 Spring Boot 版本健康度检查**：值得做但属于 DevEx，单独提案

## Capabilities

### New Capabilities
- `runtime-baseline`：声明 ULP 运行时与框架基线 —— Spring Boot 版本、Java 版本、Jackson 版本、Security DSL 版本。本 capability 的目的是**把"我们跑在什么 stack 上"显式记录下来**，避免未来 drift；任何降级或换栈必须改 spec。

### Modified Capabilities
- `integration-testing`：Spring Boot / Spring TestContext / Failsafe 版本约束更新；MockMvc 的 `MOCK` web environment 在 SF7 下 API 不变，IT 类无需重写但容器初始化路径要核对一遍

## Impact

- **构建系统**：根 `pom.xml` parent 升级，**87 个 `<dependencyManagement>` 条目逐个核对**（Spring 受管的 38 项跟随 BOM 自动升；自管的 49 项手动评估是否能升、是否还需要 override）
- **POM 自管 override 评估**（很可能 4.0 自带后无需 override）：
  - `maven-failsafe-plugin 3.5.2` —— Boot 4 默认带的版本 ≥ 3.5
  - `commons-lang3 3.17.0` —— testcontainers 1.21.x 需 ≥3.14，Boot 4 默认带 ≥3.17
  - `docker-java 3.5.1` —— testcontainers BOM 默认值
  - `testcontainers 1.21.3` —— 评估是否升 1.22 / 1.23
- **业务代码**：~30 个模块、~1500 个 Java 文件，估计实际需要改动 50–100 个文件（Jackson 序列化器、Security 配置、Spring Session 配置、自定义 EnvironmentPostProcessor 如有）
- **3 个 application.yml**（console / portal / openapi）：属性键迁移
- **Spring Session redis**：当前用 `spring.session.redis.namespace` 等配置，需迁到 `spring.session.data.redis.*`
- **测试基线 (#36 IT 套件)**：是这次升级唯一的回归网，每个阶段必须跑通完整 IT
- **运行时影响**：Tomcat 10 → 11、Jetty 12（如换 web server）、Reactor 2025.0；端口、监听行为应无变化
- **前端**：不影响（pure frontend build）
- **DB schema**：Liquibase 5 changelog 兼容性问题，实施时 verify；预计不需要 schema 改动

### 风险三档
- **HIGH**：Jackson 3 包名迁移 —— `tools.jackson` 是新包，IDE 自动 import 不一定灵；序列化器漏改会 NPE/类找不到，运行时才暴露。**缓解：IT 套件全跑一遍 + 浏览器手测 SSO 登录 + OIDC 完整 flow**
- **MEDIUM**：Spring Security 7 DSL refactor —— filterChain bean 的 lambda DSL 在 7.x 有 deprecation 清理；当前 ULP 的 SecurityConfig 类至少 4 个，每个都要按 7.x 模板重写
- **MEDIUM**：Java 21 升级带来的反射 / 模块系统行为变化 —— `--add-opens` 可能要补，Lombok 在 Java 21 之前 patch version 有 bug，需固定到 1.18.32+
- **LOW**：第三方依赖如 redisson 3.40、easyexcel 3.3、springdoc 2.6 与 Boot 4 兼容性 —— 大概率需要 minor bump，少量 API 调整

### 实施分阶段策略
共 12 个阶段（详见 tasks.md），每阶段一个 commit：先升基础（POM + Java 21）→ 修编译 → 修 Jackson → 修 Security → 修 Session/Hibernate/Liquibase → IT 套件回归 → 三个应用手工烟测 → 文档更新。

**预计工期**：40–75 小时聚焦工作，跨 5–8 个工作日。比 HeroDevs 给的"企业规模 200–500h"短，因为 (a) ULP 是 fork、规模 ~30 模块；(b) #36 已建立 IT 基线作为回归网；(c) 本仓库 SecurityConfig / Jackson 自定义代码量有限。

## 参考链接

- [Spring Boot 4.0 Migration Guide](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Migration-Guide)
- [Spring Boot 4.0 Release Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Release-Notes)
- [Spring Boot EOL Schedule (HeroDevs, 2026-04)](https://www.herodevs.com/blog-posts/spring-boot-versions-eol-dates-and-latest-releases-april-2026)
- [Spring Authorization Server starter 4.0.5 on Maven Central](https://repo1.maven.org/maven2/org/springframework/boot/spring-boot-security-oauth2-authorization-server/4.0.5)
