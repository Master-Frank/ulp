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
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import cn.frank.ulp.application.ApplicationServiceLoader;
import cn.frank.ulp.audit.event.AuditEventPublish;
import cn.frank.ulp.common.repository.setting.SettingRepository;
import cn.frank.ulp.protocol.jwt.JwtAuthorizationService;
import cn.frank.ulp.protocol.jwt.RedisJwtAuthorizationService;
import cn.frank.ulp.protocol.jwt.authentication.JwtAuthenticationFailureEventListener;
import cn.frank.ulp.protocol.jwt.authentication.JwtAuthenticationSuccessEventListener;
import cn.frank.ulp.protocol.jwt.configurers.JwtAuthorizationServerConfigurer;
import cn.frank.ulp.support.web.useragent.UserAgentParser;
import static org.springframework.security.config.http.SessionCreationPolicy.NEVER;

import static cn.frank.ulp.common.constant.ConfigBeanNameConstants.JWT_PROTOCOL_SECURITY_FILTER_CHAIN;

/**
 *
 * @author Frank Zhang
 */
@AutoConfigureBefore(PortalSecurityConfiguration.class)
@Configuration(proxyBeanMethods = false)
public class JwtProtocolSecurityConfiguration extends AbstractSecurityConfiguration {

    /**
     * JwtProtocolSecurityFilterChain
     *
     * @param httpSecurity {@link HttpSecurity}
     * @return {@link SecurityFilterChain}
     * @throws Exception Exception
     */
    @Bean(value = JWT_PROTOCOL_SECURITY_FILTER_CHAIN)
    @RefreshScope
    public SecurityFilterChain jwtProtocolSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //@formatter:off
        httpSecurity.getSharedObject(AuthenticationManagerBuilder.class).parentAuthenticationManager(null);
        //Jwt IDP 配置
        JwtAuthorizationServerConfigurer serverConfigurer = new JwtAuthorizationServerConfigurer(userAgentParser);
        RequestMatcher endpointsMatcher = serverConfigurer.getEndpointsMatcher();
        httpSecurity.securityMatcher(endpointsMatcher)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                //安全上下文
                .securityContext(securityContext())
                //CSRF
                .csrf(withCsrfConfigurerDefaults(endpointsMatcher))
                //headers
                .headers(withHeadersConfigurerDefaults())
                //cors
                .cors(withCorsConfigurerDefaults())
                //会话管理器
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(NEVER))
                .with(serverConfigurer,configurer-> {});
        return httpSecurity.build();
        //@formatter:on
    }

    /**
     * JWT 成功监听
     *
     * @param auditEventPublish {@link AuditEventPublish}
     * @return {@link AuthenticationSuccessEvent}
     */
    @Bean
    public ApplicationListener<AuthenticationSuccessEvent> jwtAuthenticationSuccessEventListener(AuditEventPublish auditEventPublish) {
        return new JwtAuthenticationSuccessEventListener(auditEventPublish);
    }

    /**
     * Jwt 失败监听
     *
     * @param auditEventPublish {@link AuditEventPublish}
     * @return {@link JwtAuthenticationFailureEventListener}
     */
    @Bean
    public ApplicationListener<AbstractAuthenticationFailureEvent> jwtAuthenticationFailureEventListener(AuditEventPublish auditEventPublish) {
        return new JwtAuthenticationFailureEventListener(auditEventPublish);
    }

    @Bean
    public JwtAuthorizationService jwtAuthorizationService(RedisConnectionFactory redisConnectionFactory,
                                                           CacheProperties cacheProperties,
                                                           AutowireCapableBeanFactory beanFactory,
                                                           ApplicationServiceLoader applicationServiceLoader) {
        RedisTemplate<String, String> redisTemplate = getStringRedisTemplate(redisConnectionFactory,
            cacheProperties);
        return new RedisJwtAuthorizationService(redisTemplate, beanFactory,
            applicationServiceLoader);
    }

    private final UserAgentParser userAgentParser;

    public JwtProtocolSecurityConfiguration(SettingRepository settingRepository,
                                            UserAgentParser userAgentParser) {
        super(userAgentParser, settingRepository);
        this.userAgentParser = userAgentParser;
    }

}
