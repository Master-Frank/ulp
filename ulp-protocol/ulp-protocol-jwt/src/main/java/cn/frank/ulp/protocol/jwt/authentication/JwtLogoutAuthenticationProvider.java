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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import cn.frank.ulp.application.jwt.model.JwtProtocolConfig;
import cn.frank.ulp.protocol.jwt.exception.JwtAuthenticationException;
import cn.frank.ulp.protocol.jwt.exception.JwtError;
import static cn.frank.ulp.protocol.jwt.constant.JwtProtocolConstants.JWT_ERROR_URI;
import static cn.frank.ulp.protocol.jwt.constant.JwtProtocolConstants.POST_LOGOUT_REDIRECT_URI;
import static cn.frank.ulp.protocol.jwt.exception.JwtErrorCodes.SERVER_ERROR;

/**
 *
 * @author Frank Zhang
 */
public final class JwtLogoutAuthenticationProvider implements AuthenticationProvider {
    private final Logger logger = LoggerFactory.getLogger(JwtLogoutAuthenticationProvider.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtLogoutAuthenticationToken logoutAuthenticationToken = (JwtLogoutAuthenticationToken) authentication;
        JwtProtocolConfig config = logoutAuthenticationToken.getConfig();
        //校验注销后重定向地址
        if (!StringUtils.equals(logoutAuthenticationToken.getPostLogoutRedirectUri(),
            config.getPostLogoutRedirectUri())) {
            logger.info(String.format(
                "Jwt logout: with post_logout_redirect_uri %s does not match supplied post_logout_redirect_uri %s.",
                logoutAuthenticationToken.getPostLogoutRedirectUri(),
                config.getPostLogoutRedirectUri()));
            JwtError error = new JwtError(SERVER_ERROR,
                "Jwt Logout Request Parameter: " + POST_LOGOUT_REDIRECT_URI, JWT_ERROR_URI);
            throw new JwtAuthenticationException(error);
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtLogoutAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
