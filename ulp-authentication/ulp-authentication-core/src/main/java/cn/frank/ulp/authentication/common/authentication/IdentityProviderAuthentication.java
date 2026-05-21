/*
 * ulp-authentication-core - United Login Platform
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
package cn.frank.ulp.authentication.common.authentication;

import java.io.Serial;
import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import cn.frank.ulp.authentication.common.IdentityProviderType;

import lombok.Getter;

/**
 * Idp Authentication
 *
 * @author Frank Zhang
 */
public class IdentityProviderAuthentication extends AbstractAuthenticationToken
                                            implements java.io.Serializable {

    // ~ Instance fields

    @Serial
    private static final long          serialVersionUID = -1506897701981698420L;

    private final Object               principal;

    /**
     * 提供商类型
     */
    @Getter
    private final IdentityProviderType providerType;

    /**
     * 提供商ID
     */
    @Getter
    private final String               providerId;

    // ~ Constructors

    /**
     * This constructor can be safely used by any code that wishes to create a
     * <code>UsernamePasswordAuthenticationToken</code>, as the {@link #isAuthenticated()}
     * will return <code>false</code>.
     */
    public IdentityProviderAuthentication(Object principal, IdentityProviderType providerType,
                                          String providerId,
                                          Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.providerId = providerId;
        this.providerType = providerType;
        super.setAuthenticated(true);
    }

    // ~ Methods

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return super.getAuthorities();
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }

}
