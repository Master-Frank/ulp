/*
 * ulp-authentication-gitee - United Login Platform
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
package cn.frank.ulp.authentication.gitee;

import java.io.Serial;

import cn.frank.ulp.authentication.common.client.IdentityProviderConfig;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;

/**
 * Gitee 登录
 *
 * @author Frank Zhang
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GiteeIdentityProviderOAuth2Config extends IdentityProviderConfig {
    @Serial
    private static final long serialVersionUID = -5831048603320371078L;
    /**
     * 客户端id
     */
    @NotBlank(message = "应用clientId不能为空")
    private String            clientId;

    /**
     * 客户端Secret
     */
    @NotBlank(message = "应用clientSecret不能为空")
    private String            clientSecret;
}
