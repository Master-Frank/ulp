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

import org.springdoc.core.annotations.ParameterObject;

import cn.frank.ulp.common.enums.app.AppPolicySubjectType;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 应用授权策略查询参数
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "应用授权策略查询参数")
@ParameterObject
public class AppAccessPolicyQuery {

    /**
     * 应用id
     */
    @Parameter(description = "应用ID")
    private String               appId;

    /**
     * 授权主体
     */
    @Parameter(description = "授权主体名称")
    private String               subjectName;

    /**
     * 授权主体ID
     */
    @Parameter(description = "授权主体ID")
    private String               subjectId;

    /**
     * 主体类型（用户、分组、组织机构）
     */
    @Parameter(description = "主体类型")
    private AppPolicySubjectType subjectType;

    /**
     * 应用名称
     */
    @Parameter(description = "应用名称")
    private String               appName;
}
