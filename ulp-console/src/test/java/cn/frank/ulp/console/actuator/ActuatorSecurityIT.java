/*
 * ulp-console - United Login Platform
 * Copyright (c) 2022-Present Frank Zhang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.frank.ulp.console.actuator;

import org.springframework.test.context.ActiveProfiles;

import cn.frank.ulp.support.testsupport.AbstractActuatorSecurityIT;

/**
 * console 部署单元的 actuator 安全合同验证。
 *
 * <p>{@link cn.frank.ulp.console.configuration.ConsoleSecurityConfiguration#actuatorSecurityFilterChain}
 * 用 {@code hasRole("ADMIN")} 而非 {@code denyAll()}：当前没有 admin 登录走 actuator 的路径，
 * 等效于 denyAll；写成 hasRole 是为后续运维平台用 admin token 拉指标预留入口。
 */
@ActiveProfiles("test")
class ActuatorSecurityIT extends AbstractActuatorSecurityIT {
}
