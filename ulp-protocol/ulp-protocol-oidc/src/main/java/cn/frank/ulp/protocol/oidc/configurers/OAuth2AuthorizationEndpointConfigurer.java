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
package cn.frank.ulp.protocol.oidc.configurers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import cn.frank.ulp.protocol.code.EndpointMatcher;
import cn.frank.ulp.protocol.code.configurer.AbstractConfigurer;
import cn.frank.ulp.protocol.oidc.authentication.OAuth2AuthorizationImplicitRequestAuthenticationProvider;
import cn.frank.ulp.protocol.oidc.authentication.OAuth2AuthorizationImplicitRequestAuthenticationToken;
import cn.frank.ulp.protocol.oidc.authorization.client.OidcConfigRegisteredClientRepositoryWrapper;
import cn.frank.ulp.protocol.oidc.endpoint.OAuth2AuthorizationEndpointFilter;
import static cn.frank.ulp.common.constant.ProtocolConstants.OidcEndpointConstants.AUTHORIZATION_ENDPOINT;
import static cn.frank.ulp.protocol.code.configurer.AuthenticationUtils.getAuthenticationDetailsSource;

/**
 * Configurer for the OAuth 2.0 Authorization Endpoint.
 *
 * @author Frank Zhang
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public final class OAuth2AuthorizationEndpointConfigurer extends AbstractConfigurer {

    private RequestMatcher                requestMatcher;

    private SessionAuthenticationStrategy sessionAuthenticationStrategy;

    /**
     * Restrict for internal use only.
     */
    OAuth2AuthorizationEndpointConfigurer(ObjectPostProcessor<Object> objectPostProcessor) {
        super(objectPostProcessor);
    }

    @Override
    public void init(HttpSecurity httpSecurity) {
        sessionAuthenticationStrategy = (authentication, request, response) -> {
            SessionRegistry sessionRegistry = httpSecurity.getSharedObject(SessionRegistry.class);
            //授权码请求
            if (authentication instanceof OAuth2AuthorizationCodeRequestAuthenticationToken token) {
                if (token.getScopes().contains(OidcScopes.OPENID)) {
                    if (sessionRegistry
                        .getSessionInformation(request.getSession().getId()) == null) {
                        sessionRegistry.registerNewSession(request.getSession().getId(),
                            ((Authentication) token.getPrincipal()).getPrincipal());
                    }
                }
            }
            //简化模式
            if (authentication instanceof OAuth2AuthorizationImplicitRequestAuthenticationToken token) {
                if (token.getScopes().contains(OidcScopes.OPENID)) {
                    if (sessionRegistry
                        .getSessionInformation(request.getSession().getId()) == null) {
                        sessionRegistry.registerNewSession(request.getSession().getId(),
                            ((Authentication) token.getPrincipal()).getPrincipal());
                    }
                }
            }
        };

        this.requestMatcher = new OrRequestMatcher(
            PathPatternRequestMatcher.pathPattern(HttpMethod.GET, AUTHORIZATION_ENDPOINT),
            PathPatternRequestMatcher.pathPattern(HttpMethod.POST, AUTHORIZATION_ENDPOINT));

        List<AuthenticationProvider> authenticationProviders = createDefaultAuthenticationProviders(
            httpSecurity);
        authenticationProviders.forEach(authenticationProvider -> httpSecurity
            .authenticationProvider(postProcess(authenticationProvider)));
    }

    @Override
    public void configure(HttpSecurity httpSecurity) {
        AuthenticationManager authenticationManager = httpSecurity
            .getSharedObject(AuthenticationManager.class);
        OAuth2AuthorizationService authorizationService = httpSecurity
            .getSharedObject(OAuth2AuthorizationService.class);
        OAuth2AuthorizationEndpointFilter authorizationEndpointFilter = new OAuth2AuthorizationEndpointFilter(
            authorizationService, authenticationManager, AUTHORIZATION_ENDPOINT);
        //会话认证策略
        authorizationEndpointFilter
            .setSessionAuthenticationStrategy(this.sessionAuthenticationStrategy);
        //认证详情源
        authorizationEndpointFilter
            .setAuthenticationDetailsSource(getAuthenticationDetailsSource(httpSecurity));

        httpSecurity.addFilterBefore(postProcess(authorizationEndpointFilter),
            AbstractPreAuthenticatedProcessingFilter.class);
    }

    private List<AuthenticationProvider> createDefaultAuthenticationProviders(HttpSecurity httpSecurity) {
        List<AuthenticationProvider> authenticationProviders = new ArrayList<>();
        OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator = OAuth2ConfigurerUtils
            .getTokenGenerator(httpSecurity);

        //授权码认证
        OAuth2AuthorizationCodeRequestAuthenticationProvider authorizationCodeRequestAuthenticationProvider = new OAuth2AuthorizationCodeRequestAuthenticationProvider(
            new OidcConfigRegisteredClientRepositoryWrapper(
                OAuth2ConfigurerUtils.getRegisteredClientRepository(httpSecurity)),
            OAuth2ConfigurerUtils.getAuthorizationService(httpSecurity),
            OAuth2ConfigurerUtils.getAuthorizationConsentService(httpSecurity));
        authenticationProviders.add(authorizationCodeRequestAuthenticationProvider);

        //隐式模式认证
        OAuth2AuthorizationImplicitRequestAuthenticationProvider authenticationImplicitRequestAuthenticationProvider = new OAuth2AuthorizationImplicitRequestAuthenticationProvider(
            new OidcConfigRegisteredClientRepositoryWrapper(
                OAuth2ConfigurerUtils.getRegisteredClientRepository(httpSecurity)),
            OAuth2ConfigurerUtils.getAuthorizationService(httpSecurity),
            OAuth2ConfigurerUtils.getAuthorizationConsentService(httpSecurity), tokenGenerator);
        authenticationProviders.add(authenticationImplicitRequestAuthenticationProvider);

        return authenticationProviders;
    }

    @Override
    public EndpointMatcher getEndpointMatcher() {
        return new EndpointMatcher(this.requestMatcher, true);
    }
}
