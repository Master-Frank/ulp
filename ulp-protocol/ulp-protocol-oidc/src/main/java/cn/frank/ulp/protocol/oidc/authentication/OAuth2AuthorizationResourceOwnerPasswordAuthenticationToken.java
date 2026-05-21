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

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;
import org.springframework.util.Assert;

/**
 * An {@link Authentication} implementation used for the OAuth 2.0 Authorization Username Grant.
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/6/26 22:30
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken extends
                                                                         OAuth2AuthorizationGrantAuthenticationToken {

    /**
     * 授权类型：密码模式
     */
    public static final AuthorizationGrantType PASSWORD = new AuthorizationGrantType("password");

    private final String                       username;
    private final String                       password;
    private final Set<String>                  scopes;

    /**
     * Constructs
     *
     * @param username             username
     * @param password             password
     * @param scopes               scopes
     * @param clientPrincipal      the authenticated client principal
     * @param additionalParameters the additional parameters
     */
    public OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken(String username,
                                                                       @Nullable String password,
                                                                       Set<String> scopes,
                                                                       Authentication clientPrincipal,
                                                                       @Nullable Map<String, Object> additionalParameters) {
        super(PASSWORD, clientPrincipal, additionalParameters);
        Assert.hasText(username, "username cannot be empty");
        Assert.hasText(password, "password cannot be empty");
        this.username = username;
        this.password = password;
        this.scopes = Collections
            .unmodifiableSet(scopes != null ? new HashSet<>(scopes) : Collections.emptySet());
    }

    /**
     * Returns the username
     *
     * @return the username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Returns the password.
     *
     * @return the password
     */
    @Nullable
    public String getPassword() {
        return this.password;
    }

    /**
     * getScopes
     *
     * @return scopes
     */
    public Set<String> getScopes() {
        return scopes;
    }

    public Object getClientPrincipal() {
        return super.getPrincipal();
    }
}
