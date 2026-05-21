/*
 * ulp-authentication-github - United Login Platform
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
package cn.frank.ulp.authentication.github;

import java.io.Serial;

import cn.frank.ulp.authentication.common.client.IdentityProviderConfig;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;

/**
 * GITHUB 认证配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/4/4 23:58
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GithubIdentityProviderOAuth2Config extends IdentityProviderConfig {
    @Serial
    private static final long serialVersionUID = -6850223527422243176L;

    /**
     * Client ID
     */
    @NotBlank(message = "Client ID 不能为空")
    private String            clientId;

    /**
     * Client Secret
     */
    @NotBlank(message = "Client Secret 不能为空")
    private String            clientSecret;
}
