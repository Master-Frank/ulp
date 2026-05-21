/*
 * ulp-portal - United Login Platform
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
package cn.frank.ulp.portal.configuration.security;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import cn.frank.ulp.audit.event.AuditEventPublish;
import cn.frank.ulp.authentication.common.jackjson.AuthenticationJacksonModule;
import cn.frank.ulp.common.repository.app.AppOidcConfigRepository;
import cn.frank.ulp.common.repository.setting.SettingRepository;
import cn.frank.ulp.protocol.oidc.OidcOpenApiCustomizer;
import cn.frank.ulp.protocol.oidc.authentication.*;
import cn.frank.ulp.protocol.oidc.authorization.client.OidcConfigRegisteredClientRepository;
import cn.frank.ulp.protocol.oidc.authorization.token.OAuth2TokenCustomizer;
import cn.frank.ulp.protocol.oidc.configurers.ClientJwkSource;
import cn.frank.ulp.protocol.oidc.configurers.OAuth2AuthorizationServerConfigurer;
import cn.frank.ulp.protocol.oidc.jackson.OidcProtocolJackson2Module;
import cn.frank.ulp.protocol.oidc.token.OpaqueTokenIntrospector;
import cn.frank.ulp.support.jackjson.SupportJackson2Module;
import cn.frank.ulp.support.redis.KeyStringRedisSerializer;
import cn.frank.ulp.support.web.useragent.UserAgentParser;
import static org.springframework.security.config.http.SessionCreationPolicy.NEVER;

import static cn.frank.ulp.common.constant.ConfigBeanNameConstants.OIDC_PROTOCOL_SECURITY_FILTER_CHAIN;

/**
 * OIDC 协议配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/2 21:16
 */
@AutoConfigureBefore(PortalSecurityConfiguration.class)
@Configuration(proxyBeanMethods = false)
public class OidcProtocolSecurityConfiguration extends AbstractSecurityConfiguration {

    /**
     * OIDC 协议
     *
     * @return {@link SecurityFilterChain}
     * @throws Exception Exception
     */
    @Bean(value = OIDC_PROTOCOL_SECURITY_FILTER_CHAIN)
    @RefreshScope
    public SecurityFilterChain oidcProtocolSecurityFilterChain(HttpSecurity httpSecurity,
                                                               AccessTokenAuthenticationManagerResolver authenticationManagerResolver) throws Exception {
        //@formatter:off
        httpSecurity.getSharedObject(AuthenticationManagerBuilder.class).parentAuthenticationManager(null);
        OAuth2AuthorizationServerConfigurer serverConfigurer = new OAuth2AuthorizationServerConfigurer(userAgentParser);
        RequestMatcher endpointsMatcher = serverConfigurer.getEndpointsMatcher();
        OrRequestMatcher requestMatcher = new OrRequestMatcher(endpointsMatcher);
        httpSecurity
                .securityMatcher(endpointsMatcher)
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                //安全上下文
                .securityContext(securityContext())
                //CSRF
                .csrf(withCsrfConfigurerDefaults(requestMatcher))
                //headers
                .headers(withHeadersConfigurerDefaults())
                //cors
                .cors(withCorsConfigurerDefaults())
                //会话管理器
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(NEVER))
                .with(serverConfigurer,configurer-> {})
                //资源服务器配置放到最后
                .oauth2ResourceServer(configurer -> configurer.authenticationManagerResolver(authenticationManagerResolver));
        return httpSecurity.build();
        //@formatter:on
    }

    /**
     * 令牌定制器
     *
     * @return {@link OAuth2TokenCustomizer}
     */
    @Bean
    public org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return new OAuth2TokenCustomizer();
    }

    /**
     * Token 解析器
     *
     * @param jwtDecoder {@link JwtDecoder}
     * @param authorizationService {@link OAuth2AuthorizationService}
     * @return {@link AccessTokenAuthenticationManagerResolver}
     */
    @Bean
    public AccessTokenAuthenticationManagerResolver authenticationManagerResolver(JwtDecoder jwtDecoder,
                                                                                  OAuth2AuthorizationService authorizationService) {
        return new AccessTokenAuthenticationManagerResolver(
            new JwtAuthenticationProvider(jwtDecoder), new OpaqueTokenAuthenticationProvider(
                new OpaqueTokenIntrospector(authorizationService)));
    }

    /**
     * 认证服务
     *
     * @param redisConnectionFactory {@link RedisConnectionFactory}
     * @param cacheProperties {@link CacheProperties}
     * @param clientRepository {@link RedisConnectionFactory}
     * @return {@link AutowireCapableBeanFactory}
     */
    @Bean
    public OAuth2AuthorizationService authorizationService(RedisConnectionFactory redisConnectionFactory,
                                                           CacheProperties cacheProperties,
                                                           RegisteredClientRepository clientRepository) {
        RedisTemplate<String, String> redisTemplate = getStringRedisTemplate(redisConnectionFactory,
            cacheProperties);
        ClassLoader classLoader = this.getClass().getClassLoader();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModules(SupportJackson2Module.getModules(classLoader));
        objectMapper.registerModules(OidcProtocolJackson2Module.getModules());
        objectMapper.registerModules(new AuthenticationJacksonModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        RedisOAuth2AuthorizationServiceWrapper service = new RedisOAuth2AuthorizationServiceWrapper(
            redisTemplate, clientRepository);
        service.setObjectMapper(objectMapper);
        return service;
    }

    /**
     * JWT 解码器
     *
     * @return {@link JwtDecoder}
     */
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfigurer.jwtDecoder(jwkSource);
    }

    /**
     * JWKSource
     *
     * @return {@link JWKSource}
     */
    @Bean
    JWKSource<SecurityContext> jwkSource() {
        return new ClientJwkSource();
    }

    /**
     * 客户端Repository
     *
     * @return {@link RegisteredClientRepository}
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(AppOidcConfigRepository appOidcConfigRepository) {
        return new OidcConfigRegisteredClientRepository(appOidcConfigRepository);
    }

    /**
     * 授权同意service
     *
     * @return {@link OAuth2AuthorizationConsentService}
     */
    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(RedisConnectionFactory redisConnectionFactory,
                                                                         CacheProperties cacheProperties) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        KeyStringRedisSerializer keyStringRedisSerializer = new KeyStringRedisSerializer(
            cacheProperties.getRedis().getKeyPrefix());
        redisTemplate.setKeySerializer(keyStringRedisSerializer);
        redisTemplate.setValueSerializer(StringRedisSerializer.UTF_8);
        redisTemplate.afterPropertiesSet();
        return new RedisOAuth2AuthorizationConsentService(redisTemplate);
    }

    /**
     * OAuth2 登录成功监听器
     *
     * @return {@link ApplicationListener}
     */
    @Bean
    public ApplicationListener<AuthenticationSuccessEvent> oauth2ProtocolAuthenticationSuccessEventListener(AuditEventPublish auditEventPublish,
                                                                                                            OAuth2AuthorizationService authorizationService) {
        return new OAuth2AuthenticationSuccessEventListener(auditEventPublish,
            authorizationService);
    }

    /**
     * OAuth2 失败监听
     *
     * @param auditEventPublish {@link AuditEventPublish}
     * @return {@link OAuth2AuthenticationFailureEventListener}
     */
    @Bean
    public ApplicationListener<AbstractAuthenticationFailureEvent> oauth2AuthenticationFailureEventListener(AuditEventPublish auditEventPublish) {
        return new OAuth2AuthenticationFailureEventListener(auditEventPublish);
    }

    /**
     * OIDC openapi 文档
     *
     * @return {@link OidcOpenApiCustomizer}
     */
    @Bean
    public OidcOpenApiCustomizer oidcOpenApiCustomizer() {
        return new OidcOpenApiCustomizer();
    }

    public OidcProtocolSecurityConfiguration(SettingRepository settingRepository,
                                             UserAgentParser userAgentParser) {
        super(userAgentParser, settingRepository);
        this.userAgentParser = userAgentParser;
    }

    private final UserAgentParser userAgentParser;
}
