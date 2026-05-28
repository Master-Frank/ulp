/*
 * ulp-authentication-wechatwork - United Login Platform
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
package cn.frank.ulp.authentication.wechatwork;

import java.io.Serial;

import cn.frank.ulp.authentication.common.client.IdentityProviderConfig;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;

/**
 * 企业微信扫码配置参数
 *
 * @author Frank Zhang
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WeChatWorkIdentityProviderOAuth2Config extends IdentityProviderConfig {
    @Serial
    private static final long serialVersionUID = -6850223527422243076L;

    /**
     * 企业ID
     */
    @NotBlank(message = "企业ID不能为空")
    private String            corpId;

    /**
     * 应用AgentID
     */
    @NotBlank(message = "应用AgentID不能为空")
    private String            agentId;

    /**
     * 应用Secret
     */
    @NotBlank(message = "应用Secret不能为空")
    private String            appSecret;
}
