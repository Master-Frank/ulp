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

import com.google.common.collect.Lists;

/**
 * 短信OPT认证
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/12/16 21:34
 */
public class OtpAuthentication extends AbstractAuthenticationToken implements java.io.Serializable {

    // ~ Instance fields

    @Serial
    private static final long serialVersionUID = -1506897701981698420L;

    /**
     * principal
     */
    private final Object      principal;

    /**
     * 邮箱/验证码
     */
    private final String      recipient;

    /**
     * 类型
     */
    private final String      type;

    // ~ Constructors

    /**
     * This constructor can be safely used by any code that wishes to create a
     * <code>UsernamePasswordAuthenticationToken</code>, as the {@link #isAuthenticated()}
     * will return <code>false</code>.
     */
    public OtpAuthentication(Object principal, String recipient, String type) {
        super(Lists.newArrayList());
        this.principal = principal;
        this.recipient = recipient;
        this.type = type;
        setAuthenticated(false);
    }

    /**
     * This constructor can be safely used by any code that wishes to create a
     * <code>UsernamePasswordAuthenticationToken</code>, as the {@link #isAuthenticated()}
     * will return <code>false</code>.
     */
    public OtpAuthentication(Object principal, String recipient, String type,
                             boolean authenticated) {
        super(Lists.newArrayList());
        this.principal = principal;
        this.recipient = recipient;
        this.type = type;
        setAuthenticated(authenticated);
    }

    /**
     * This constructor should only be used by <code>AuthenticationManager</code> or
     * <code>AuthenticationProvider</code> implementations that are satisfied with
     * producing a trusted (i.e. {@link #isAuthenticated()} = <code>true</code>)
     * authentication token.
     *
     * @param recipient      {@link  String}
     * @param type {@link  String}
     * @param authorities {@link  GrantedAuthority}
     * @param principal {@link Object}
     */
    public OtpAuthentication(Object principal, String recipient, String type,
                             Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.recipient = recipient;
        this.principal = principal;
        this.type = type;
        // must use super, as we override
        super.setAuthenticated(true);
    }

    // ~ Methods

    @Override
    public Object getCredentials() {
        return this.recipient;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getType() {
        return type;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
