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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.http.converter.OAuth2ErrorHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.ClientSecretAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.JwtClientAssertionAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.authentication.PublicClientAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.web.OAuth2ClientAuthenticationFilter;
import org.springframework.security.oauth2.server.authorization.web.authentication.*;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import cn.frank.ulp.common.constant.ProtocolConstants;
import cn.frank.ulp.protocol.code.EndpointMatcher;
import cn.frank.ulp.protocol.code.configurer.AbstractConfigurer;
import cn.frank.ulp.protocol.oidc.authorization.client.OidcConfigRegisteredClientRepositoryWrapper;
import cn.frank.ulp.support.security.crypto.password.NoOpPasswordEncoder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static cn.frank.ulp.protocol.oidc.constant.OidcProtocolConstants.OIDC_ERROR_URI;

/**
 * 客户端认证配置器
 *
 * @author Frank Zhang
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public final class OAuth2ClientAuthenticationConfigurer extends AbstractConfigurer {

    /**
     * RequestMatcher
     */
    private RequestMatcher                          requestMatcher;

    /**
     * 认证失败处理器
     */
    private final AuthenticationFailureHandler      authenticationFailureHandler = this::onAuthenticationFailure;

    /**
     * error Http 响应转换器
     */
    private final HttpMessageConverter<OAuth2Error> errorHttpResponseConverter   = new OAuth2ErrorHttpMessageConverter();

    /**
     * Restrict for internal use only.
     */
    OAuth2ClientAuthenticationConfigurer(ObjectPostProcessor<Object> objectPostProcessor) {
        super(objectPostProcessor);
    }

    @Override
    public void init(HttpSecurity httpSecurity) {
        this.requestMatcher = new OrRequestMatcher(
            //令牌端点
            PathPatternRequestMatcher.pathPattern(HttpMethod.POST,
                ProtocolConstants.OidcEndpointConstants.TOKEN_ENDPOINT),
            //令牌内省端点
            PathPatternRequestMatcher.pathPattern(HttpMethod.POST,
                ProtocolConstants.OidcEndpointConstants.TOKEN_INTROSPECTION_ENDPOINT),
            //令牌吊销端点
            PathPatternRequestMatcher.pathPattern(HttpMethod.POST,
                ProtocolConstants.OidcEndpointConstants.TOKEN_REVOCATION_ENDPOINT),
            //设备授权端点
            PathPatternRequestMatcher.pathPattern(HttpMethod.POST,
                ProtocolConstants.OidcEndpointConstants.DEVICE_AUTHORIZATION_ENDPOINT));

        List<AuthenticationProvider> authenticationProviders = createDefaultAuthenticationProviders(
            httpSecurity);
        authenticationProviders.forEach(authenticationProvider -> httpSecurity
            .authenticationProvider(postProcess(authenticationProvider)));
    }

    @Override
    public void configure(HttpSecurity httpSecurity) {
        AuthenticationManager authenticationManager = httpSecurity
            .getSharedObject(AuthenticationManager.class);
        OAuth2ClientAuthenticationFilter clientAuthenticationFilter = new OAuth2ClientAuthenticationFilter(
            authenticationManager, this.requestMatcher);
        clientAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        //认证转换器
        List<AuthenticationConverter> authenticationConverters = createDefaultAuthenticationConverters();
        clientAuthenticationFilter.setAuthenticationConverter(
            new DelegatingAuthenticationConverter(authenticationConverters));
        httpSecurity.addFilterAfter(postProcess(clientAuthenticationFilter),
            AbstractPreAuthenticatedProcessingFilter.class);
    }

    @Override
    public EndpointMatcher getEndpointMatcher() {
        return new EndpointMatcher(this.requestMatcher, false);
    }

    /**
     * 默认认证转换器
     *
     * @return {@link List}
     */
    private static List<AuthenticationConverter> createDefaultAuthenticationConverters() {
        List<AuthenticationConverter> authenticationConverters = new ArrayList<>();
        //Jwt 断言
        authenticationConverters.add(new JwtClientAssertionAuthenticationConverter());
        //Basic 认证
        authenticationConverters.add(new ClientSecretBasicAuthenticationConverter());
        //Post 认证
        authenticationConverters.add(new ClientSecretPostAuthenticationConverter());
        // PKCE
        authenticationConverters.add(new PublicClientAuthenticationConverter());
        return authenticationConverters;
    }

    /**
     * 默认身份提供商
     *
     * @param httpSecurity {@link HttpSecurity}
     * @return {@link List}
     */
    private static List<AuthenticationProvider> createDefaultAuthenticationProviders(HttpSecurity httpSecurity) {
        List<AuthenticationProvider> authenticationProviders = new ArrayList<>();

        RegisteredClientRepository registeredClientRepository = new OidcConfigRegisteredClientRepositoryWrapper(
            OAuth2ConfigurerUtils.getRegisteredClientRepository(httpSecurity));
        OAuth2AuthorizationService authorizationService = OAuth2ConfigurerUtils
            .getAuthorizationService(httpSecurity);

        //JWT 断言提供商
        JwtClientAssertionAuthenticationProvider jwtClientAssertionAuthenticationProvider = new JwtClientAssertionAuthenticationProvider(
            registeredClientRepository, authorizationService);
        authenticationProviders.add(jwtClientAssertionAuthenticationProvider);

        //客户端秘钥提供商
        ClientSecretAuthenticationProvider clientSecretAuthenticationProvider = new ClientSecretAuthenticationProvider(
            registeredClientRepository, authorizationService);

        clientSecretAuthenticationProvider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        authenticationProviders.add(clientSecretAuthenticationProvider);

        //PKCE提供商
        PublicClientAuthenticationProvider publicClientAuthenticationProvider = new PublicClientAuthenticationProvider(
            registeredClientRepository, authorizationService);
        authenticationProviders.add(publicClientAuthenticationProvider);

        return authenticationProviders;
    }

    private void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                         AuthenticationException exception) throws IOException {

        SecurityContextHolder.clearContext();

        // TODO
        // The authorization server MAY return an HTTP 401 (Unauthorized) status code
        // to indicate which HTTP authentication schemes are supported.
        // If the client attempted to authenticate via the "Authorization" request header field,
        // the authorization server MUST respond with an HTTP 401 (Unauthorized) status code and
        // include the "WWW-Authenticate" response header field
        // matching the authentication scheme used by the client.

        OAuth2Error error = ((OAuth2AuthenticationException) exception).getError();
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        if (OAuth2ErrorCodes.INVALID_CLIENT.equals(error.getErrorCode())) {
            httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
            error = new OAuth2Error(error.getErrorCode(),
                "Client authentication failed. Either the client or the client credentials are invalid.",
                OIDC_ERROR_URI);
        } else {
            httpResponse.setStatusCode(HttpStatus.BAD_REQUEST);
        }
        this.errorHttpResponseConverter.write(error, null, httpResponse);
    }

}
