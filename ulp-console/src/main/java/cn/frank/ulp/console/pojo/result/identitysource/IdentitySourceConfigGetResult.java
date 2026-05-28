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
package cn.frank.ulp.console.pojo.result.identitysource;

import java.io.Serial;
import java.io.Serializable;

import cn.frank.ulp.common.entity.identitysource.config.JobConfig;
import cn.frank.ulp.common.entity.identitysource.config.StrategyConfig;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 获取身份源配置
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "获取身份源配置")
public class IdentitySourceConfigGetResult implements Serializable {
    @Serial
    private static final long serialVersionUID = -1440230086940289961L;

    /**
     * ID
     */
    @Parameter(description = "ID")
    private String            id;

    /**
     * 是否已配置
     */
    @Parameter(description = "是否已配置")
    private Boolean           configured;

    /**
     * 基础配置
     */
    @Parameter(description = "基础配置")
    private Object            basicConfig;

    /**
     * 策略配置
     */
    @Parameter(description = "策略配置")
    private StrategyConfig    strategyConfig;

    /**
     * 任务配置
     */
    @Parameter(description = "任务配置")
    private JobConfig         jobConfig;
}
