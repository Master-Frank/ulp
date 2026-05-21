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

import cn.frank.ulp.common.storage.StorageConfig;
import cn.frank.ulp.common.storage.enums.StorageProvider;

import lombok.Builder;
import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 存储配置查询结果
 *
 * @author Frank Zhang
 */
@Data
@Builder
@Schema(description = "存储配置查询响应")
public class StorageProviderConfigResult implements Serializable {

    @Serial
    private static final long    serialVersionUID = -2667374916357438335L;
    /**
     * 服务商
     */
    @Parameter(description = "服务商")
    private StorageProvider      provider;
    /**
     * 启用
     */
    @Parameter(description = "是否启用")
    private Boolean              enabled;
    /**
     * 配置信息
     */
    @Parameter(description = "配置信息")
    private StorageConfig.Config config;

}
