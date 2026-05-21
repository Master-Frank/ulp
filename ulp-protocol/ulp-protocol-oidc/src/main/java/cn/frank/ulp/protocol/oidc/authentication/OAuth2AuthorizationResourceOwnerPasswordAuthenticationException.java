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
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

/**
 * This exception is thrown by {@link OAuth2AuthorizationResourceOwnerPasswordAuthenticationProvider}
 * when an attempt to authenticate the OAuth 2.0 Authorization Request (or Consent) fails.
 *
 * @author TopIAM
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class OAuth2AuthorizationResourceOwnerPasswordAuthenticationException extends
                                                                             OAuth2AuthenticationException {
    private final OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken authorizationPasswordAuthenticationToken;

    /**
     * Constructs an {@code OAuth2AuthorizationPasswordAuthenticationException} using the provided parameters.
     *
     * @param error                                    the {@link OAuth2Error OAuth 2.0 Error}
     * @param authorizationPasswordAuthenticationToken the {@link Authentication} instance of the OAuth 2.0 Authorization Request (or Consent)
     */
    public OAuth2AuthorizationResourceOwnerPasswordAuthenticationException(OAuth2Error error,
                                                                           @Nullable OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken authorizationPasswordAuthenticationToken) {
        super(error);
        this.authorizationPasswordAuthenticationToken = authorizationPasswordAuthenticationToken;
    }

    /**
     * Constructs an {@code OAuth2AuthorizationPasswordAuthenticationException} using the provided parameters.
     *
     * @param error                                  the {@link OAuth2Error OAuth 2.0 Error}
     * @param cause                                  the root cause
     * @param authorizationCodeRequestAuthentication the {@link Authentication} instance of the OAuth 2.0 Authorization Request (or Consent)
     */
    public OAuth2AuthorizationResourceOwnerPasswordAuthenticationException(OAuth2Error error,
                                                                           Throwable cause,
                                                                           @Nullable OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken authorizationCodeRequestAuthentication) {
        super(error, cause);
        this.authorizationPasswordAuthenticationToken = authorizationCodeRequestAuthentication;
    }

    /**
     * Returns the {@link Authentication} instance of the OAuth 2.0 Authorization Request (or Consent), or {@code null} if not available.
     *
     * @return the {@link OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken}
     */
    @Nullable
    public OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken getAuthorizationCodeRequestAuthentication() {
        return this.authorizationPasswordAuthenticationToken;
    }

}
