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
package cn.frank.ulp.console.pojo.result.app;

import java.time.LocalDateTime;

import cn.frank.ulp.common.enums.app.AppPolicySubjectType;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 应用访问授权策略结果
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "应用访问授权策略响应")
public class AppAccessPolicyResult {

    /**
     * id
     */
    @Schema(description = "id")
    private String               id;

    /**
     * 应用ID
     */
    @Schema(description = "应用ID")
    private String               appId;

    /**
     * 应用名称
     */
    @Schema(description = "应用名称")
    private String               appName;

    /**
     * 应用图标
     */
    @Schema(description = "应用图标")
    private String               appIcon;

    /**
     * 模板
     */
    @Schema(description = "应用模版")
    private String               appTemplate;

    /**
     * 协议
     */
    @Schema(description = "应用协议")
    private String               appProtocol;

    /**
     * 应用类型
     */
    @Schema(description = "应用类型")
    private String               appType;

    /**
     * 主体ID（用户、分组、组织机构）
     */
    @Schema(description = "主体ID")
    private String               subjectId;

    /**
     * 授权主体
     */
    @Schema(description = "授权主体")
    private String               subjectName;

    /**
     * 主体类型（用户、分组、组织机构）
     */
    @Schema(description = "主体类型")
    private AppPolicySubjectType subjectType;

    /**
     * 是否启用
     */
    @Schema(description = "是否启用")
    private Boolean              enabled;

    /**
     * 添加时间
     */
    @Schema(description = "添加时间")
    private LocalDateTime        createTime;

}
