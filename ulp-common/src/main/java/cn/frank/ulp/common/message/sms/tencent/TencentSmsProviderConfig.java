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
package cn.frank.ulp.common.message.sms.tencent;

import cn.frank.ulp.common.jackjson.encrypt.JsonPropertyEncrypt;
import cn.frank.ulp.common.message.sms.SmsProviderConfig;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotEmpty;

/**
 * 验证码提供商配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/10/1 21:10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TencentSmsProviderConfig extends SmsProviderConfig {
    public TencentSmsProviderConfig() {
    }

    /**
     * secretId
     */
    @NotEmpty(message = "SecretId不能为空")
    private String secretId;

    /**
     * secretKey
     */
    @JsonPropertyEncrypt
    @NotEmpty(message = "SecretKey不能为空")
    private String secretKey;

    /**
     * 短信应用ID
     */
    @NotEmpty(message = "短信应用ID不能为空")
    private String sdkAppId;

    /**
     * 短信签名内容
     */
    @NotEmpty(message = "短信签名内容不能为空")
    private String signName;

    /**
     * Region
     */
    @NotEmpty(message = "Region不能为空")
    private String region;
}
