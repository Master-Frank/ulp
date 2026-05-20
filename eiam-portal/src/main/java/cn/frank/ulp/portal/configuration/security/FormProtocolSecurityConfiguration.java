/*
 * eiam-portal - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
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
import cn.frank.ulp.protocol.form.FormAuthorizationService;
import cn.frank.ulp.protocol.form.RedisFormAuthorizationService;
import cn.frank.ulp.protocol.form.authentication.FormAuthenticationFailureEventListener;
import cn.frank.ulp.protocol.form.authentication.FormAuthenticationSuccessEventListener;
import cn.frank.ulp.protocol.form.configurers.FormAuthorizationServerConfigurer;
import cn.frank.ulp.support.web.useragent.UserAgentParser;
import static org.springframework.security.config.http.SessionCreationPolicy.NEVER;

import static cn.frank.ulp.common.constant.ConfigBeanNameConstants.FORM_PROTOCOL_SECURITY_FILTER_CHAIN;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/3 21:15
 */
@AutoConfigureBefore(PortalSecurityConfiguration.class)
@Configuration(proxyBeanMethods = false)
public class FormProtocolSecurityConfiguration extends AbstractSecurityConfiguration {

    /**
     * FormProtocolSecurityFilterChain
     *
     * @param httpSecurity {@link HttpSecurity}
     * @return {@link SecurityFilterChain}
     * @throws Exception Exception
     */
    @Bean(value = FORM_PROTOCOL_SECURITY_FILTER_CHAIN)
    @RefreshScope
    public SecurityFilterChain formProtocolSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //@formatter:off
        httpSecurity.getSharedObject(AuthenticationManagerBuilder.class).parentAuthenticationManager(null);
        //Form IDP 配置
        FormAuthorizationServerConfigurer serverConfigurer = new FormAuthorizationServerConfigurer(userAgentParser);
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
                .with(serverConfigurer,configurer->{});
        return httpSecurity.build();
        //@formatter:on
    }

    /**
     * Form 成功监听
     *
     * @param auditEventPublish {@link AuditEventPublish}
     * @return {@link AuthenticationSuccessEvent}
     */
    @Bean
    public ApplicationListener<AuthenticationSuccessEvent> formAuthenticationSuccessEventListener(AuditEventPublish auditEventPublish) {
        return new FormAuthenticationSuccessEventListener(auditEventPublish);
    }

    /**
     * Form 失败监听
     *
     * @param auditEventPublish {@link AuditEventPublish}
     * @return {@link FormAuthenticationFailureEventListener}
     */
    @Bean
    public ApplicationListener<AbstractAuthenticationFailureEvent> formAuthenticationFailureEventListener(AuditEventPublish auditEventPublish) {
        return new FormAuthenticationFailureEventListener(auditEventPublish);
    }

    @Bean
    public FormAuthorizationService formAuthorizationService(RedisConnectionFactory redisConnectionFactory,
                                                             CacheProperties cacheProperties,
                                                             AutowireCapableBeanFactory beanFactory,
                                                             ApplicationServiceLoader applicationServiceLoader) {
        RedisTemplate<String, String> redisTemplate = getStringRedisTemplate(redisConnectionFactory,
            cacheProperties);
        return new RedisFormAuthorizationService(redisTemplate, beanFactory,
            applicationServiceLoader);
    }

    public FormProtocolSecurityConfiguration(SettingRepository settingRepository,
                                             UserAgentParser userAgentParser) {
        super(userAgentParser, settingRepository);
        this.userAgentParser = userAgentParser;
    }

    private final UserAgentParser userAgentParser;

}
