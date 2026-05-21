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

import cn.frank.ulp.common.message.enums.MailProvider;
import cn.frank.ulp.common.message.enums.MailSafetyType;

import lombok.Builder;
import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 邮件服务商配置查询结果
 *
 * @author TopIAM
 */
@Data
@Builder
@Schema(description = "邮件服务商配置查询响应")
public class EmailProviderConfigResult implements Serializable {
    @Serial
    private static final long serialVersionUID = 8584300384703986791L;

    /**
     * smtp地址
     */
    @Parameter(description = "smtp地址")
    private String            smtpUrl;

    /**
     * 端口
     */
    @Parameter(description = "端口")
    private Integer           port;

    /**
     * 安全验证
     */
    @Parameter(description = "安全验证")
    private MailSafetyType    safetyType;

    /**
     * 用户名
     */
    @Parameter(description = "用户名")
    private String            username;

    /**
     * 秘钥
     */
    @Parameter(description = "秘钥")
    private String            secret;

    /**
     * 配置JSON串
     */
    @Parameter(description = "配置JSON串")
    private String            config;

    /**
     * 平台
     */
    @Parameter(description = "平台")
    private MailProvider      provider;

    /**
     * 描述
     */
    @Parameter(description = "描述")
    private String            desc;
    /**
     * 是否启用
     */
    @Parameter(description = "是否启用")
    private Boolean           enabled;
}
