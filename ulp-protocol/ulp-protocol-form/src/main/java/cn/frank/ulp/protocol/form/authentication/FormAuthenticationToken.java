/*
 * ulp-protocol-form - United Login Platform
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
package cn.frank.ulp.protocol.form.authentication;

import java.util.ArrayList;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

import cn.frank.ulp.application.form.model.FormProtocolConfig;
import cn.frank.ulp.application.jwt.model.JwtProtocolConfig;

import lombok.Getter;

/**
 *
 * @author Frank Zhang
 */
public class FormAuthenticationToken extends AbstractAuthenticationToken {

    /**
     * Authentication
     */
    private final Authentication     principal;

    /**
     * 账户名称
     */
    @Getter
    private final String             accountUsername;

    /**
     * 账户凭据
     */
    @Getter
    private final String             accountCredential;

    /**
     * 协议配置
     */
    @Getter
    private final FormProtocolConfig config;

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param principal         {@link Authentication}
     * @param accountUsername   {@link String}
     * @param accountCredential {@link String}
     * @param config {@link JwtProtocolConfig}
     */
    public FormAuthenticationToken(Authentication principal, String accountUsername,
                                   String accountCredential, FormProtocolConfig config) {
        super(new ArrayList<>());
        Assert.notNull(principal, "principal must not be null ");
        Assert.notNull(accountUsername, "accountUsername must not be null ");
        Assert.notNull(accountCredential, "accountCredential must not be null ");
        Assert.notNull(config, "config must not be null ");
        this.config = config;
        this.principal = principal;
        this.accountUsername = accountUsername;
        this.accountCredential = accountCredential;
        setAuthenticated(true);
    }

    /**
     * The credentials that prove the principal is correct. This is usually a password,
     * but could be anything relevant to the <code>AuthenticationManager</code>. Callers
     * are expected to populate the credentials.
     *
     * @return the credentials that prove the identity of the <code>Principal</code>
     */
    @Override
    public Object getCredentials() {
        return "";
    }

    /**
     * The identity of the principal being authenticated. In the case of an authentication
     * request with username and password, this would be the username. Callers are
     * expected to populate the principal for an authentication request.
     * <p>
     * The <tt>AuthenticationManager</tt> implementation will often return an
     * <tt>Authentication</tt> containing richer information as the principal for use by
     * the application. Many of the authentication providers will create a
     * {@code UserDetails} object as the principal.
     *
     * @return the <code>Principal</code> being authenticated or the authenticated
     * principal after authentication.
     */
    @Override
    public Object getPrincipal() {
        return this.principal;
    }

}
