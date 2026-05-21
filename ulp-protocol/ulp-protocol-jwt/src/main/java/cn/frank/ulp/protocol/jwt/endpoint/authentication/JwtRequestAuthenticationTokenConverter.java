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

import org.apache.commons.collections4.MapUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;

import cn.frank.ulp.application.context.ApplicationContext;
import cn.frank.ulp.application.context.ApplicationContextHolder;
import cn.frank.ulp.application.jwt.model.JwtProtocolConfig;
import cn.frank.ulp.protocol.jwt.authentication.JwtLoginAuthenticationToken;
import cn.frank.ulp.protocol.jwt.exception.JwtAuthenticationException;
import cn.frank.ulp.protocol.jwt.exception.JwtError;
import cn.frank.ulp.protocol.jwt.exception.JwtErrorCodes;

import jakarta.servlet.http.HttpServletRequest;
import static org.springframework.http.HttpMethod.GET;

import static cn.frank.ulp.protocol.jwt.constant.JwtProtocolConstants.JWT_ERROR_URI;
import static cn.frank.ulp.protocol.jwt.constant.JwtProtocolConstants.TARGET_URL;
import static cn.frank.ulp.support.util.HttpRequestUtils.getFormParameters;
import static cn.frank.ulp.support.util.HttpRequestUtils.getQueryParameters;

/**
 * @author Frank Zhang
 */
public final class JwtRequestAuthenticationTokenConverter implements AuthenticationConverter {
    private static final Authentication ANONYMOUS_AUTHENTICATION = new AnonymousAuthenticationToken(
        "anonymous", "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));

    @Override
    public Authentication convert(HttpServletRequest request) {
        MultiValueMap<String, String> parameters = GET.name().equals(request.getMethod())
            ? getQueryParameters(request)
            : getFormParameters(request);
        String targetUrl = parameters.getFirst(TARGET_URL);

        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        if (principal == null) {
            principal = ANONYMOUS_AUTHENTICATION;
        }
        ApplicationContext applicationContext = ApplicationContextHolder.getApplicationContext();
        if (MapUtils.isEmpty(applicationContext.getConfig())
            | !applicationContext.getConfig().containsKey(JwtProtocolConfig.class.getName())) {
            JwtError error = new JwtError(JwtErrorCodes.SERVER_ERROR, null, JWT_ERROR_URI);
            throw new JwtAuthenticationException(error);
        }
        JwtProtocolConfig config = (JwtProtocolConfig) applicationContext.getConfig()
            .get(JwtProtocolConfig.class.getName());
        return new JwtLoginAuthenticationToken(principal, targetUrl, config);
    }
}
