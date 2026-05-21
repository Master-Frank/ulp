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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;
import org.springframework.util.Assert;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.google.common.collect.Lists;

import cn.frank.ulp.common.entity.setting.SettingEntity;
import cn.frank.ulp.common.repository.setting.SettingRepository;
import cn.frank.ulp.core.setting.SecuritySettingConstants;
import cn.frank.ulp.portal.authentication.*;
import cn.frank.ulp.support.redis.KeyStringRedisSerializer;
import cn.frank.ulp.support.security.csrf.SpaCsrfTokenRequestHandler;
import cn.frank.ulp.support.web.useragent.UserAgentParser;
import static org.springframework.security.web.header.writers.XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK;
import static org.springframework.web.cors.CorsConfiguration.ALL;

import static cn.frank.ulp.core.setting.SecuritySettingConstants.*;
import static cn.frank.ulp.support.constant.EiamConstants.DEFAULT_CSRF_COOKIE_NAME;
import static cn.frank.ulp.support.constant.EiamConstants.DEFAULT_CSRF_HEADER_NAME;
import static cn.frank.ulp.support.security.constant.SecurityConstants.LOGOUT_PATH;

/**
 * AbstractConfiguration
 *
 * @author Frank Zhang
 */
public class AbstractSecurityConfiguration {

    /**
     * Cors 过滤器
     *
     * @return {@link HeadersConfigurer}
     */
    public Customizer<CorsConfigurer<HttpSecurity>> withCorsConfigurerDefaults() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Lists.newArrayList(ALL));
        configuration.applyPermitDefaultValues();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin(ALL);
        configuration.addAllowedHeader(ALL);
        configuration.addAllowedMethod(HttpMethod.PUT);
        configuration.addAllowedMethod(HttpMethod.GET);
        configuration.addAllowedMethod(HttpMethod.POST);
        configuration.addAllowedMethod(HttpMethod.DELETE);
        configuration.addAllowedMethod(HttpMethod.OPTIONS);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return configurer -> configurer.configurationSource(source);
    }

    /**
     * session 管理器
     *
     * @return {@link SessionManagementConfigurer}
     */
    public Customizer<SessionManagementConfigurer<HttpSecurity>> withSessionManagementConfigurerDefaults() {
        //@formatter:off
        SettingEntity setting = settingRepository.findByName(SECURITY_SESSION_MAXIMUM);
        return configurer -> {
            configurer.sessionFixation().changeSessionId();
            //用户并发
            String defaultSessionMaximum = SecuritySettingConstants.SECURITY_BASIC_DEFAULT_SETTINGS
                .get(SECURITY_SESSION_MAXIMUM);
            String sessionMaximum = Objects.isNull(setting) ? defaultSessionMaximum
                : "0".equals(setting.getValue()) ? defaultSessionMaximum : setting.getValue();
            configurer.maximumSessions(Integer.parseInt(sessionMaximum))
                .expiredSessionStrategy(new PortalSessionInformationExpiredStrategy());
        };
        //@formatter:on
    }

    /**
     * session 退出过滤器
     *
     * @return {@link LogoutConfigurer}
     */
    public Customizer<LogoutConfigurer<HttpSecurity>> withLogoutConfigurerDefaults() {
        return configurer -> {
            configurer.logoutUrl(LOGOUT_PATH);
            configurer.logoutSuccessHandler(new PortalLogoutSuccessHandler());
            configurer.permitAll();
        };
    }

    /**
     * headers 过滤器
     *
     * @return {@link HeadersConfigurer}
     */
    public Customizer<HeadersConfigurer<HttpSecurity>> withHeadersConfigurerDefaults() {
        List<SettingEntity> list = settingRepository.findByNameIn(SECURITY_DEFENSE_POLICY_KEY);
        // 转MAP
        Map<String, String> map = list.stream().collect(Collectors.toMap(SettingEntity::getName,
            SettingEntity::getValue, (key1, key2) -> key2));
        //内容安全策略
        String contentSecurityPolicy = map
            .containsKey(SECURITY_DEFENSE_POLICY_CONTENT_SECURITY_POLICY)
                ? map.get(SECURITY_DEFENSE_POLICY_CONTENT_SECURITY_POLICY).replace("\n", "")
                    .replace("\r\n", "")
                : SECURITY_DEFENSE_POLICY_DEFAULT_SETTINGS
                    .get(SECURITY_DEFENSE_POLICY_CONTENT_SECURITY_POLICY);

        //@formatter:off
        return configurer -> {
            configurer.xssProtection(xssProtection -> xssProtection.headerValue(ENABLED_MODE_BLOCK))
                    .contentSecurityPolicy(config-> config.policyDirectives(contentSecurityPolicy))
                    .referrerPolicy(referrerPolicyConfig -> referrerPolicyConfig.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                    .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
                    .contentTypeOptions(contentTypeOptionsConfig-> {})
                    .permissionsPolicy(permissionsPolicyConfig -> permissionsPolicyConfig.policy("camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()"));
        };
        //@formatter:on
    }

    /**
     * 异常处理器
     *
     * @return {@link ExceptionHandlingConfigurer}
     */
    public Customizer<ExceptionHandlingConfigurer<HttpSecurity>> withExceptionConfigurerDefaults() {
        return configurer -> {
            configurer
                .authenticationEntryPoint(new PortalAuthenticationEntryPoint(userAgentParser));
            configurer.accessDeniedHandler(new PortalAccessDeniedHandler());
            configurer
                .withObjectPostProcessor(new ObjectPostProcessor<ExceptionTranslationFilter>() {
                    @Override
                    public <O extends ExceptionTranslationFilter> O postProcess(O filter) {
                        filter
                            .setAuthenticationTrustResolver(new AuthenticationTrustResolverImpl());
                        return filter;
                    }
                });
        };
    }

    /**
     * withRememberMeConfigurerDefaults
     *
     * @return {@link RememberMeConfigurer}
     */
    public Customizer<RememberMeConfigurer<HttpSecurity>> withRememberMeConfigurerDefaults() {
        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
        rememberMeServices.setAlwaysRemember(false);
        SettingEntity setting = settingRepository.findByName(SECURITY_BASIC_REMEMBER_ME_VALID_TIME);
        String rememberMeValiditySeconds = Objects.isNull(setting)
            ? SecuritySettingConstants.SECURITY_BASIC_DEFAULT_SETTINGS
                .get(SECURITY_BASIC_REMEMBER_ME_VALID_TIME)
            : setting.getValue();
        rememberMeServices.setValiditySeconds(Integer.parseInt(rememberMeValiditySeconds));
        return configurer -> configurer.rememberMeServices(rememberMeServices);
    }

    /**
     * csrf
     *
     * @return {@link CsrfConfigurer}
     */
    public Customizer<CsrfConfigurer<HttpSecurity>> withCsrfConfigurerDefaults(RequestMatcher... requestMatcher) {
        return csrf -> {
            CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
            repository.setCookieName(DEFAULT_CSRF_COOKIE_NAME);
            repository.setHeaderName(DEFAULT_CSRF_HEADER_NAME);
            csrf.csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler());
            csrf.csrfTokenRepository(repository);
            csrf.ignoringRequestMatchers(requestMatcher);
        };
    }

    protected RedisTemplate<String, String> getStringRedisTemplate(@Nullable RedisConnectionFactory connectionFactory,
                                                                   CacheProperties cacheProperties) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        KeyStringRedisSerializer keyStringRedisSerializer = new KeyStringRedisSerializer(
            cacheProperties.getRedis().getKeyPrefix());
        redisTemplate.setKeySerializer(keyStringRedisSerializer);
        redisTemplate.setValueSerializer(StringRedisSerializer.UTF_8);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     *  安全上下文
     *
     * @return {@link SecurityContextConfigurer}
     */
    public Customizer<SecurityContextConfigurer<HttpSecurity>> securityContext() {
        return configurer -> {
        };
    }

    private final UserAgentParser   userAgentParser;
    private final SettingRepository settingRepository;

    public AbstractSecurityConfiguration(UserAgentParser userAgentParser,
                                         SettingRepository settingRepository) {
        Assert.notNull(settingRepository, "The userAgentParser cannot be null");
        Assert.notNull(settingRepository, "The settingRepository cannot be null");
        this.settingRepository = settingRepository;
        this.userAgentParser = userAgentParser;
    }

}
