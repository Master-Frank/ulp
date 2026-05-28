/*
 * ulp-authentication-alipay - United Login Platform
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
package cn.frank.ulp.authentication.alipay;

import cn.frank.ulp.authentication.common.client.IdentityProviderConfig;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;

/**
 * 支付宝 登录配置
 *
 * @author Frank Zhang
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AlipayIdentityProviderOAuth2Config extends IdentityProviderConfig {

    /**
     * 商户ID
     */
    @NotBlank(message = "商户ID不能为空")
    private String appId;

    /**
     * 应用私钥
     */
    @NotBlank(message = "应用私钥")
    private String appPrivateKey;

    /**
     * 支付宝公钥
     */
    @NotBlank(message = "支付宝公钥")
    private String alipayPublicKey;

}
