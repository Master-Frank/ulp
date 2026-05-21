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
package cn.frank.ulp.console.pojo.query.app;

import java.io.Serializable;

import org.springdoc.core.annotations.ParameterObject;

import cn.frank.ulp.common.enums.app.AppCertUsingType;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 查询应用证书列表入参
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "查询应用证书列表入参")
@ParameterObject
public class AppCertQuery implements Serializable {

    /**
     * 应用ID
     */
    @NotBlank(message = "应用ID不能为空")
    @Parameter(description = "应用ID")
    private String           appId;

    /**
     * 使用类型
     */
    @Parameter(description = "使用类型")
    private AppCertUsingType usingType;
}
