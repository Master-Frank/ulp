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

import java.util.Set;
import java.util.function.Consumer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.log.LogMessage;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import static cn.frank.ulp.protocol.oidc.authentication.OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken.PASSWORD;

/**
 * 验证器
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/11/9 23:54
 */
@SuppressWarnings({ "AlibabaClassNamingShouldBeCamel", "AlibabaUndefineMagicConstant" })
public final class OAuth2AuthorizationResourceOwnerPasswordAuthenticationValidator implements
                                                                                   Consumer<OAuth2AuthorizationResourceOwnerPasswordAuthenticationContext> {

    private static final Log                                                                    LOGGER                       = LogFactory
        .getLog(OAuth2AuthorizationResourceOwnerPasswordAuthenticationValidator.class);
    private static final String                                                                 ERROR_URI                    = "https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.2.1";

    /**
     * The default validator for {@link OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken#getScopes()}.
     */
    public static final Consumer<OAuth2AuthorizationResourceOwnerPasswordAuthenticationContext> DEFAULT_SCOPE_VALIDATOR      = OAuth2AuthorizationResourceOwnerPasswordAuthenticationValidator::validateScope;

    public static final Consumer<OAuth2AuthorizationResourceOwnerPasswordAuthenticationContext> DEFAULT_GRANT_TYPE_VALIDATOR = OAuth2AuthorizationResourceOwnerPasswordAuthenticationValidator::validateGrantType;

    private final Consumer<OAuth2AuthorizationResourceOwnerPasswordAuthenticationContext>       authenticationValidator      = DEFAULT_SCOPE_VALIDATOR
        .andThen(DEFAULT_GRANT_TYPE_VALIDATOR);

    /**
     * 验证 Scope
     *
     * @param authenticationContext {@link OAuth2AuthorizationResourceOwnerPasswordAuthenticationContext}
     */
    private static void validateScope(OAuth2AuthorizationResourceOwnerPasswordAuthenticationContext authenticationContext) {
        RegisteredClient registeredClient = authenticationContext.getRegisteredClient();
        OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken authorizationPasswordAuthenticationToken = authenticationContext
            .getAuthentication();
        Set<String> requestedScopes = authorizationPasswordAuthenticationToken.getScopes();
        Set<String> allowedScopes = registeredClient.getScopes();
        if (!requestedScopes.isEmpty() && !allowedScopes.containsAll(requestedScopes)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(
                    LogMessage.format("Invalid request: requested scope is not allowed"
                                      + " for registered client '%s'",
                        registeredClient.getId()));
            }
            throwError(OAuth2ErrorCodes.INVALID_SCOPE, OAuth2ParameterNames.SCOPE,
                authorizationPasswordAuthenticationToken);
        }
    }

    /**
     * 验证授权类型
     *
     * @param authenticationContext {@link OAuth2AuthorizationResourceOwnerPasswordAuthenticationContext}
     */
    private static void validateGrantType(OAuth2AuthorizationResourceOwnerPasswordAuthenticationContext authenticationContext) {
        OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken authorizationCodeRequestAuthentication = authenticationContext
            .getAuthentication();
        RegisteredClient registeredClient = authenticationContext.getRegisteredClient();
        //校验 grant type
        if (!registeredClient.getAuthorizationGrantTypes().contains(PASSWORD)) {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.warn(LogMessage.format(
                    "Invalid request: requested grant_type is not allowed for registered client '%s'",
                    registeredClient.getId()));
            }
            throwError(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT, OAuth2ParameterNames.CLIENT_ID,
                authorizationCodeRequestAuthentication);
        }
    }

    private static void throwError(String errorCode, String parameterName,
                                   OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken authorizationCodeRequestAuthentication) {
        OAuth2Error error = new OAuth2Error(errorCode, "OAuth 2.0 Parameter: " + parameterName,
            ERROR_URI);
        throwError(error, authorizationCodeRequestAuthentication);
    }

    private static void throwError(OAuth2Error error,
                                   OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken authorizationPasswordAuthenticationToken) {
        authorizationPasswordAuthenticationToken.setAuthenticated(true);
        throw new OAuth2AuthorizationResourceOwnerPasswordAuthenticationException(error,
            authorizationPasswordAuthenticationToken);
    }

    @Override
    public void accept(OAuth2AuthorizationResourceOwnerPasswordAuthenticationContext authenticationContext) {
        this.authenticationValidator.accept(authenticationContext);
    }

}
