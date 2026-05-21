/*
 * ulp-protocol-jwt - United Login Platform
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
package cn.frank.ulp.protocol.jwt.authentication;

import java.util.ArrayList;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import cn.frank.ulp.application.jwt.model.JwtProtocolConfig;

import lombok.Getter;

/**
 *
 * @author Frank Zhang
 */
public class JwtLogoutAuthenticationToken extends AbstractAuthenticationToken {

    private final Authentication    principal;

    @Getter
    private final JwtProtocolConfig config;

    @Getter
    private final String            sessionId;

    @Getter
    private final String            postLogoutRedirectUri;

    public JwtLogoutAuthenticationToken(Authentication principal, JwtProtocolConfig config,
                                        String postLogoutRedirectUri, String sessionId) {
        super(new ArrayList<>());
        this.principal = principal;
        this.config = config;
        this.sessionId = sessionId;
        this.postLogoutRedirectUri = postLogoutRedirectUri;
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
        return principal;
    }

    /**
     * Returns {@code true} if {@link #getPrincipal()} is authenticated, {@code false} otherwise.
     *
     * @return {@code true} if {@link #getPrincipal()} is authenticated, {@code false} otherwise
     */
    public boolean isPrincipalAuthenticated() {
        return !AnonymousAuthenticationToken.class.isAssignableFrom(this.principal.getClass())
               && this.principal.isAuthenticated();
    }
}
