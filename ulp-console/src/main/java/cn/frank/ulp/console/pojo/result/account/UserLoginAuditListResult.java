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
package cn.frank.ulp.console.pojo.result.account;

import java.time.LocalDateTime;

import cn.frank.ulp.audit.enums.EventStatus;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户登录日志返回结果
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022年11月13日21:49:35
 */
@Data
@Schema(description = "用户登录日志返回响应")
public class UserLoginAuditListResult {
    /**
     * ID
     */
    @Parameter(description = "ID")
    private String        id;

    /**
     * 应用名称
     */
    @Parameter(description = "应用名称")
    private String        appName;

    /**
     * 客户端IP
     */
    @Parameter(description = "客户端IP")
    private String        clientIp;

    /**
     * 操作系统
     */
    @Parameter(description = "操作系统")
    private String        platform;

    /**
     * 登录结果
     */
    @Parameter(description = "浏览器")
    private String        browser;

    /**
     * 位置
     */
    @Parameter(description = "位置")
    private String        location;

    /**
     * 事件状态
     */
    @Parameter(description = "事件状态")
    private EventStatus   eventStatus;

    /**
     * 事件时间
     */
    @Parameter(description = "事件时间")
    private LocalDateTime eventTime;
}
