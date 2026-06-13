## ADDED Requirements

### Requirement: 虚拟线程开关基线

项目 SHALL 显式锁定 `spring.threads.virtual.enabled` 的值，避免后续 PR 在未做兼容性评估的前提下静默切换该开关。

本次评估（2026-06-13，含 Spring Boot 4.0.7 + JDK 21 + Hibernate 7.1 + Lettuce 6.x + Spring Session Redis Indexed）结论：**暂不启用**（`spring.threads.virtual.enabled=false`，等价于不显式声明该键）。

理由：
- Hibernate 7 `Session` / `EntityManager` 生命周期仍含 `synchronized` 块，业务高并发下虚拟线程在 synchronized 上 pin 到 carrier thread，等价于退化为平台线程，收益有限
- Spring Session Redis 在 indexed repository 模式下也用了 `synchronized`，同类风险
- ULP 当前 QPS 形态为阵发性登录流量，平台线程模型未到瓶颈，引入虚拟线程的稳定性回归风险高于性能收益

任何启用该开关的 PR MUST 同时更新本 Requirement，记录新的评估结果与触发条件（如 Hibernate 8 / Lettuce 7 修了 pinning 问题，或 ULP 实际进入持续高 QPS 场景）。

允许 application.yml 显式声明 `spring.threads.virtual.enabled: false` 作为防御性配置（明确表达"评估过且故意关闭"，非"忘了配置"）。

#### Scenario: 配置文件中虚拟线程开关为 false 或未声明
- **WHEN** 评审 ulp-console / ulp-portal / ulp-openapi 任一 `application.yml`
- **THEN** 不出现 `spring.threads.virtual.enabled: true` 的声明

#### Scenario: 启用虚拟线程的 PR 需同步改 spec
- **WHEN** 贡献者在 PR 中把 `spring.threads.virtual.enabled` 改为 `true`
- **THEN** 评审 MUST 拒绝该 PR，除非同 PR 内含相应的 `runtime-baseline` spec 修改提案，更新评估结论
