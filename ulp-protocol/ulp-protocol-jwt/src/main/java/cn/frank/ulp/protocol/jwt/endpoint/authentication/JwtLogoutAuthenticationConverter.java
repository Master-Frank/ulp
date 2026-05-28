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
package cn.frank.ulp.protocol.jwt.endpoint.authentication;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.web.authentication.AuthenticationConverter;

import cn.frank.ulp.application.context.ApplicationContext;
import cn.frank.ulp.application.context.ApplicationContextHolder;
import cn.frank.ulp.application.jwt.model.JwtProtocolConfig;
import cn.frank.ulp.protocol.jwt.authentication.JwtLogoutAuthenticationToken;
import cn.frank.ulp.protocol.jwt.exception.JwtError;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import static cn.frank.ulp.protocol.jwt.constant.JwtProtocolConstants.POST_LOGOUT_REDIRECT_URI;
import static cn.frank.ulp.protocol.jwt.endpoint.JwtAuthenticationEndpointUtils.throwError;

/**
 *
 * @author Frank Zhang
 */
public final class JwtLogoutAuthenticationConverter implements AuthenticationConverter {
    private static final Authentication ANONYMOUS_AUTHENTICATION = new AnonymousAuthenticationToken(
        "anonymous", "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));

    @Override
    public Authentication convert(HttpServletRequest request) {

        if (request.getParameterValues(POST_LOGOUT_REDIRECT_URI).length != 1) {
            throwError(new JwtError(OAuth2ErrorCodes.INVALID_REQUEST,
                "JWT Logout Request Parameter: " + POST_LOGOUT_REDIRECT_URI));
        }
        ApplicationContext context = ApplicationContextHolder.getApplicationContext();
        JwtProtocolConfig config = (JwtProtocolConfig) context.getConfig()
            .get(JwtProtocolConfig.class.getName());
        String postLogoutRedirectUri = request.getParameter(POST_LOGOUT_REDIRECT_URI);
        String sessionId = null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            sessionId = session.getId();
        }

        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        if (principal == null) {
            principal = ANONYMOUS_AUTHENTICATION;
        }
        return new JwtLogoutAuthenticationToken(principal, config, postLogoutRedirectUri,
            sessionId);
    }

}
