/*
 * ulp-common - United Login Platform
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
package cn.frank.ulp.common.message.mail;

import cn.frank.ulp.common.jackjson.encrypt.JsonPropertyEncrypt;
import cn.frank.ulp.common.message.enums.MailProvider;
import cn.frank.ulp.common.message.enums.MailSafetyType;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * MailProviderConfig
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/10/1 21:10
 */
@Data
public class MailProviderConfig {
    public MailProviderConfig() {
    }

    public MailProviderConfig(MailProvider provider, String smtpUrl, Integer port,
                              MailSafetyType safetyType, String username, String secret) {
        this.provider = provider;
        this.smtpUrl = smtpUrl;
        this.port = port;
        this.safetyType = safetyType;
        this.username = username;
        this.secret = secret;
    }

    /**
     * 平台
     */
    @NotNull(message = "平台不能为空")
    private MailProvider   provider;

    /**
     * smtp地址
     */
    @NotEmpty(message = "smtp地址不能为空")
    private String         smtpUrl;

    /**
     * 端口
     */
    @NotNull(message = "端口号不能为空")
    private Integer        port;

    /**
     * 安全验证
     */
    @NotNull(message = "安全验证方式不能为空")
    private MailSafetyType safetyType;

    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空")
    private String         username;

    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空")
    @JsonPropertyEncrypt
    private String         secret;
}
