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
package cn.frank.ulp.protocol.oidc.endpoint.authentication;

import java.util.*;

import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import cn.frank.ulp.protocol.oidc.authentication.OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken;

import jakarta.servlet.http.HttpServletRequest;
import static org.springframework.http.HttpMethod.GET;

import static cn.frank.ulp.protocol.oidc.endpoint.OAuth2EndpointUtils.throwError;
import static cn.frank.ulp.support.util.HttpRequestUtils.*;

/**
 * 密码模式认证转换器
 *
 * @author Frank Zhang
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public final class OAuth2AuthorizationResourceOwnerPasswordAuthenticationConverter implements
                                                                                   AuthenticationConverter {
    static final String ACCESS_TOKEN_REQUEST_ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

    @Nullable
    @Override
    public Authentication convert(HttpServletRequest request) {
        // grant_type (必填)
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken.PASSWORD.getValue()
            .equals(grantType)) {
            return null;
        }
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
        //获取参数
        MultiValueMap<String, String> parameters = GET.name().equals(request.getMethod())
            ? getQueryParameters(request)
            : getFormParameters(request);
        // username (必填)
        String username = parameters.getFirst("username");
        if (!StringUtils.hasText(username) || parameters.get("username").size() != 1) {
            throwError(OAuth2ErrorCodes.INVALID_REQUEST, "username",
                ACCESS_TOKEN_REQUEST_ERROR_URI);
        }
        // password (必填)
        String password = parameters.getFirst("password");
        if (!StringUtils.hasText(password) || parameters.get("password").size() != 1) {
            throwError(OAuth2ErrorCodes.INVALID_REQUEST, "password",
                ACCESS_TOKEN_REQUEST_ERROR_URI);
        }
        // scope (OPTIONAL)
        String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
        if (StringUtils.hasText(scope) && parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
            throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.SCOPE,
                ACCESS_TOKEN_REQUEST_ERROR_URI);
        }
        Set<String> requestedScopes = null;
        if (StringUtils.hasText(scope)) {
            requestedScopes = new HashSet<>(
                Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
        }
        //额外参数
        Map<String, Object> additionalParameters = new HashMap<>(16);
        parameters.forEach((key, value) -> {
            if (!key.equals(OAuth2ParameterNames.GRANT_TYPE)
                && !key.equals(OAuth2ParameterNames.CLIENT_ID) && !key.equals("username")
                && !key.equals("password")) {
                additionalParameters.put(key,
                    (value.size() == 1) ? value.get(0) : value.toArray(new String[0]));
            }
        });
        return new OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken(username, password,
            requestedScopes, clientPrincipal, additionalParameters);
    }

}
