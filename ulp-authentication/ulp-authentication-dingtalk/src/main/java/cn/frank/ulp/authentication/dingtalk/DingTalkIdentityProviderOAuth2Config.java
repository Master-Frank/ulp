/*
 * ulp-authentication-dingtalk - United Login Platform
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
package cn.frank.ulp.authentication.dingtalk;

import java.io.Serial;

import cn.frank.ulp.authentication.common.client.IdentityProviderConfig;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;

/**
 * 钉钉Oauth配置参数
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/12/8 21:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DingTalkIdentityProviderOAuth2Config extends IdentityProviderConfig {
    @Serial
    private static final long serialVersionUID = -6850223527422243076L;

    /**
     * 应用 key
     */
    @NotBlank(message = "应用AppKey不能为空")
    private String            appKey;

    /**
     * 应用 Secret
     */
    @NotBlank(message = "应用AppSecret不能为空")
    private String            appSecret;

    /**
     * 用于指定用户需要选择的组织
     */
    private String            corpId;
}
