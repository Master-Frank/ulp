/*
 * ulp-protocol-oidc - United Login Platform
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
package cn.frank.ulp.protocol.oidc.authentication;

import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

/**
 * OAuth2 简化模式授权请求身份验证异常
 *
 * @author Frank Zhang
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class OAuth2AuthorizationImplicitRequestAuthenticationException extends
                                                                       OAuth2AuthenticationException {
    private final OAuth2AuthorizationImplicitRequestAuthenticationToken authorizationImplicitRequestAuthenticationToken;

    public OAuth2AuthorizationImplicitRequestAuthenticationException(OAuth2Error error,
                                                                     @Nullable OAuth2AuthorizationImplicitRequestAuthenticationToken authorizationImplicitRequestAuthenticationToken) {
        super(error);
        this.authorizationImplicitRequestAuthenticationToken = authorizationImplicitRequestAuthenticationToken;
    }

    public OAuth2AuthorizationImplicitRequestAuthenticationException(OAuth2Error error,
                                                                     Throwable cause,
                                                                     @Nullable OAuth2AuthorizationImplicitRequestAuthenticationToken authorizationImplicitRequestAuthenticationToken) {
        super(error, cause);
        this.authorizationImplicitRequestAuthenticationToken = authorizationImplicitRequestAuthenticationToken;
    }

    @Nullable
    public OAuth2AuthorizationImplicitRequestAuthenticationToken getAuthorizationImplicitRequestAuthenticationToken() {
        return this.authorizationImplicitRequestAuthenticationToken;
    }

}
