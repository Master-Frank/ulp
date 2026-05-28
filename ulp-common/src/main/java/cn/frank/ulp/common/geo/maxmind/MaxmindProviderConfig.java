/*
 * ulp-common - United Login Platform
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
package cn.frank.ulp.common.geo.maxmind;

import cn.frank.ulp.common.geo.GeoLocationProviderConfig;
import cn.frank.ulp.common.jackjson.encrypt.JsonPropertyEncrypt;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotEmpty;

/**
 *
 * @author Frank Zhang
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MaxmindProviderConfig extends GeoLocationProviderConfig.GeoLocationConfig {
    /**
     * 密码
     */
    @JsonPropertyEncrypt
    @NotEmpty(message = "密码不能为空")
    private String sessionKey;
}
