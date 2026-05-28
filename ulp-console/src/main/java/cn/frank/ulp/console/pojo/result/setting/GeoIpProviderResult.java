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
package cn.frank.ulp.console.pojo.result.setting;

import java.io.Serial;
import java.io.Serializable;

import cn.frank.ulp.common.geo.GeoLocationProviderConfig;

import lombok.Builder;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 获取地理位置服务商配置信息
 *
 * @author Frank Zhang
 */
@Data
@Builder
@Schema(description = "获取地理位置服务商配置信息")
public class GeoIpProviderResult implements Serializable {
    @Serial
    private static final long                           serialVersionUID = -6723117700517052520L;
    /**
     * 地理位置服务商
     */
    @Schema(description = "地理位置提供商")
    @NotNull(message = "地理位置提供商不能为空")
    private String                                      provider;

    /**
     * 配置信息
     */
    @Schema(description = "配置信息")
    private GeoLocationProviderConfig.GeoLocationConfig config;
    /**
     * 是否启用
     */
    @Schema(description = "是否启用")
    private Boolean                                     enabled;

}
