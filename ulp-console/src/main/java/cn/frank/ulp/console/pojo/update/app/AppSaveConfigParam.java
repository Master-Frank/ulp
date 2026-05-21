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
package cn.frank.ulp.console.pojo.update.app;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 更新应用配置入参
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "更新应用配置入参")
public class AppSaveConfigParam implements Serializable {

    /**
     * id
     */
    @Schema(description = "应用id")
    @NotNull(message = "ID不能为空")
    private String              id;

    /**
     * 模版
     */
    @Schema(description = "模版")
    @NotNull(message = "模版不能为空")
    private String              template;

    /**
     * 配置
     */
    @Schema(description = "配置")
    @NotNull(message = "配置不能为空")
    private Map<String, Object> config;
}
