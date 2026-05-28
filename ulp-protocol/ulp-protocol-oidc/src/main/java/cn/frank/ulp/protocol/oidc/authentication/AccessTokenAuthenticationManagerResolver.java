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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContext;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ConfigurationSettingNames;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.util.Assert;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Frank Zhang
 */
public class AccessTokenAuthenticationManagerResolver implements
                                                      AuthenticationManagerResolver<HttpServletRequest> {

    private final Log logger = LogFactory.getLog(getClass());

    /**
     * Resolve an {@link AuthenticationManager} from a provided context
     *
     * @param request {@link HttpServletRequest}
     * @return the {@link AuthenticationManager} to use
     */
    @Override
    public AuthenticationManager resolve(HttpServletRequest request) {
        AuthorizationServerContext context = AuthorizationServerContextHolder.getContext();
        AuthorizationServerSettings settings = context.getAuthorizationServerSettings();
        String tokenFormat = settings
            .getSetting(ConfigurationSettingNames.Token.ACCESS_TOKEN_FORMAT);
        //根据令牌格式调用不同的解析实现
        if (OAuth2TokenFormat.SELF_CONTAINED.getValue().equals(tokenFormat)) {
            return jwtAuthenticationProvider::authenticate;
        }
        if (OAuth2TokenFormat.REFERENCE.getValue().equals(tokenFormat)) {
            return opaqueTokenAuthenticationProvider::authenticate;
        }
        return null;
    }

    /**
     * JWT 身份验证提供
     */
    private final JwtAuthenticationProvider         jwtAuthenticationProvider;

    /**
     * 不透明令牌身份验证提供
     */
    private final OpaqueTokenAuthenticationProvider opaqueTokenAuthenticationProvider;

    public AccessTokenAuthenticationManagerResolver(JwtAuthenticationProvider jwtAuthenticationProvider,
                                                    OpaqueTokenAuthenticationProvider opaqueTokenAuthenticationProvider) {
        Assert.notNull(jwtAuthenticationProvider, "jwtAuthenticationProvider cannot be null");
        Assert.notNull(opaqueTokenAuthenticationProvider,
            "opaqueTokenAuthenticationProvider cannot be null");
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.opaqueTokenAuthenticationProvider = opaqueTokenAuthenticationProvider;
    }

}
