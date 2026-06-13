/*
 * ulp-openapi - United Login Platform
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
package cn.frank.ulp.openapi.actuator;

import org.springframework.test.context.ActiveProfiles;

import cn.frank.ulp.support.testsupport.AbstractActuatorSecurityIT;

/**
 * openapi 部署单元的 actuator 安全合同验证。
 *
 * <p>{@link cn.frank.ulp.openapi.configuration.OpenApiSecurityConfiguration#actuatorSecurityFilterChain}
 * 对非公开端点使用 {@code denyAll()}：openapi 只接受 access_token 鉴权，actuator 不参与该体系，
 * 不存在合法访问诊断端点的角色。
 */
@ActiveProfiles("test")
class ActuatorSecurityIT extends AbstractActuatorSecurityIT {
}
