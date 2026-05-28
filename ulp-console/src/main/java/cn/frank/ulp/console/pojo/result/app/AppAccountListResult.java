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

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * AppAccountCreateParam 应用账户查询结果
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "应用账户列表查询响应")
public class AppAccountListResult {

    /**
     * id
     */
    @Schema(description = "id")
    private String        id;

    /**
     * 应用ID
     */
    @Schema(description = "应用ID")
    private String        appId;

    /**
     * 应用名称
     */
    @Schema(description = "应用名称")
    private String        appName;

    /**
     * 模板
     */
    @Schema(description = "应用模版")
    private String        appTemplate;

    /**
     * 协议
     */
    @Schema(description = "应用协议")
    private String        appProtocol;

    /**
     * 应用类型
     */
    @Schema(description = "应用类型")
    private String        appType;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private String        userId;

    /**
     * 用户名称
     */
    @Schema(description = "用户名称")
    private String        username;

    /**
     * 账户名称
     */
    @Schema(description = "账户名称")
    private String        account;

    /**
     * 添加时间
     */
    @Schema(description = "添加时间")
    private LocalDateTime createTime;

    /**
     * 是否默认
     */
    @Schema(description = "是否默认")
    private Boolean       defaulted;
}
