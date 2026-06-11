/*
 * ulp-console - United Login Platform
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
package cn.frank.ulp.console.configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.ObjectPostProcessor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;

import cn.frank.ulp.audit.event.AuditEventPublish;
import cn.frank.ulp.authentication.common.jackjson.AuthenticationJacksonModule;
import cn.frank.ulp.common.entity.setting.SettingEntity;
import cn.frank.ulp.common.repository.setting.AdministratorRepository;
import cn.frank.ulp.common.repository.setting.SettingRepository;
import cn.frank.ulp.console.authentication.*;
import cn.frank.ulp.support.geo.GeoLocationParser;
import cn.frank.ulp.support.jackjson.SupportJackson2Module;
import cn.frank.ulp.support.security.authentication.WebAuthenticationDetailsSource;
import cn.frank.ulp.support.security.configurer.FormLoginConfigurer;
import cn.frank.ulp.support.security.csrf.SpaCsrfTokenRequestHandler;
import cn.frank.ulp.support.web.useragent.UserAgentParser;

import lombok.RequiredArgsConstructor;

import tools.jackson.databind.ObjectMapper;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.header.writers.XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK;

import static cn.frank.ulp.common.constant.ConfigBeanNameConstants.DEFAULT_SECURITY_FILTER_CHAIN;
import static cn.frank.ulp.common.constant.SessionConstants.CURRENT_STATUS;
import static cn.frank.ulp.common.constant.SynchronizerConstants.EVENT_RECEIVE_PATH;
import static cn.frank.ulp.core.security.PublicSecretEndpoint.PUBLIC_SECRET_PATH;
import static cn.frank.ulp.core.setting.SecuritySettingConstants.*;
import static cn.frank.ulp.support.constant.UlpConstants.*;
import static cn.frank.ulp.support.security.constant.SecurityConstants.LOGOUT_PATH;
import static cn.frank.ulp.support.security.constant.SecurityConstants.RESET_PASSWORD_PATH;

/**
 * ConsoleSecurityConfiguration
 *
 * @author Frank Zhang
 */
@EnableMethodSecurity
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class ConsoleSecurityConfiguration implements BeanClassLoaderAware {

    /**
     * webSecurityCustomizer
     *
     * @return {@link WebSecurityCustomizer} WebSecurityCustomizer
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
        };
    }

    /**
     * SecurityFilterChain
     *
     * @param httpSecurity {@link  HttpSecurity}
     * @return {@link  SecurityFilterChain}
     * @throws Exception Exception
     */
    @RefreshScope
    @Bean(name = DEFAULT_SECURITY_FILTER_CHAIN)
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // @formatter:off
        // 系统配置
        httpSecurity
                .securityMatcher(API_PATH+"/**")
                //认证请求
                .authorizeHttpRequests(authorizeHttpRequests())
                //安全上下文
                .securityContext(securityContext())
                //x509
                .x509(withDefaults())
                //异常处理
                .exceptionHandling(withExceptionConfigurerDefaults())
                //记住我
                .rememberMe(withRememberMeConfigurerDefaults(settingRepository))
                //CSRF
                .csrf(withCsrfConfigurerDefaults(
                    PathPatternRequestMatcher.pathPattern(HttpMethod.OPTIONS, EVENT_RECEIVE_PATH+"/{code}"),
                    PathPatternRequestMatcher.pathPattern(HttpMethod.GET, EVENT_RECEIVE_PATH+"/{code}")))
                //headers
                .headers(withHeadersConfigurerDefaults(settingRepository))
                //cors
                .cors(withCorsConfigurerDefaults())
                //退出配置
                .logout(withLogoutConfigurerDefaults())
                //会话管理器
                .sessionManagement(withSessionManagementConfigurerDefaults(settingRepository))
                .with(withFormLoginConfigurer(),configurer-> {});
        // @formatter:on
        return httpSecurity.build();
    }

    /**
     * 认证请求
     *
     * @return {@link AuthorizeHttpRequestsConfigurer}
     */
    public Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorizeHttpRequests() {
        //@formatter:off
        return registry -> {
            registry.requestMatchers(PathPatternRequestMatcher.pathPattern(EVENT_RECEIVE_PATH+"/{code}")).permitAll();
            registry.requestMatchers(PathPatternRequestMatcher.pathPattern(CURRENT_STATUS)).permitAll();
            registry.requestMatchers(PathPatternRequestMatcher.pathPattern(HttpMethod.GET, PUBLIC_SECRET_PATH)).permitAll();
            registry.requestMatchers(PathPatternRequestMatcher.pathPattern(HttpMethod.POST, RESET_PASSWORD_PATH)).permitAll();
            registry.anyRequest().authenticated();
        };
        //@formatter:on
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

    /**
     * session 管理器
     *
     * @return {@link SessionManagementConfigurer}
     */
    public Customizer<SessionManagementConfigurer<HttpSecurity>> withSessionManagementConfigurerDefaults(SettingRepository settingRepository) {
        SettingEntity setting = settingRepository.findByName(SECURITY_SESSION_MAXIMUM);
        return configurer -> {
            configurer.sessionFixation().changeSessionId();
            //用户并发
            String defaultSessionMaximum = SECURITY_BASIC_DEFAULT_SETTINGS
                .get(SECURITY_SESSION_MAXIMUM);
            String sessionMaximum = Objects.isNull(setting) ? defaultSessionMaximum
                : "0".equals(setting.getValue()) ? defaultSessionMaximum : setting.getValue();
            configurer.maximumSessions(Integer.parseInt(sessionMaximum))
                .expiredSessionStrategy(new ConsoleSessionInformationExpiredStrategy());
        };
    }

    /**
     * session 退出过滤器
     *
     * @return {@link LogoutConfigurer}
     */
    public Customizer<LogoutConfigurer<HttpSecurity>> withLogoutConfigurerDefaults() {
        return configurer -> {
            configurer.logoutUrl(LOGOUT_PATH)
                .logoutSuccessHandler(new ConsoleLogoutSuccessHandler()).permitAll();
        };
    }

    /**
     * headers 过滤器
     *
     * @param settingRepository {@link SettingRepository}
     * @return {@link HeadersConfigurer}
     */
    public Customizer<HeadersConfigurer<HttpSecurity>> withHeadersConfigurerDefaults(SettingRepository settingRepository) {
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
     * CORS filter.
     * <p>
     * Reads cross-origin allow-list from {@code ulp.security.cors.allowed-origins}.
     * If the list is empty (the default for same-origin deployments where the
     * bundled SPA is served from the same host as the API), CORS is disabled —
     * same-origin requests bypass CORS checks entirely.
     * <p>
     * When non-empty, only the explicitly listed origins are allowed; wildcards
     * are rejected. Credentials are permitted because the SPA relies on cookies
     * for the session and CSRF token. Wildcard origins together with credentials
     * would let any website on the internet read authenticated responses, so
     * that combination is forbidden here.
     *
     * @return CORS configurer customizer
     */
    public Customizer<CorsConfigurer<HttpSecurity>> withCorsConfigurerDefaults() {
        List<String> origins = sanitizedAllowedOrigins();
        if (origins.isEmpty()) {
            return CorsConfigurer::disable;
        }
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(origins);
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Lists.newArrayList("Content-Type", "Accept", "Origin",
            DEFAULT_CSRF_HEADER_NAME, "X-Requested-With"));
        configuration.setAllowedMethods(Lists.newArrayList(HttpMethod.GET.name(),
            HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name()));
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return configurer -> configurer.configurationSource(source);
    }

    private List<String> sanitizedAllowedOrigins() {
        if (corsAllowedOrigins == null || corsAllowedOrigins.length == 0) {
            return List.of();
        }
        return Arrays.stream(corsAllowedOrigins)
            .filter(s -> s != null && !s.isBlank() && !"*".equals(s.trim())).map(String::trim)
            .toList();
    }

    /**
     * 异常处理器
     *
     * @return {@link ExceptionHandlingConfigurer}
     */
    public Customizer<ExceptionHandlingConfigurer<HttpSecurity>> withExceptionConfigurerDefaults() {
        return configurer -> {
            configurer
                .authenticationEntryPoint(new ConsoleAuthenticationEntryPoint(userAgentParser));
            configurer.accessDeniedHandler(new ConsoleAccessDeniedHandler());
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
    public Customizer<RememberMeConfigurer<HttpSecurity>> withRememberMeConfigurerDefaults(SettingRepository settingRepository) {
        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
        rememberMeServices.setAlwaysRemember(false);
        SettingEntity setting = settingRepository.findByName(SECURITY_BASIC_REMEMBER_ME_VALID_TIME);
        String rememberMeValiditySeconds = Objects.isNull(setting)
            ? SECURITY_BASIC_DEFAULT_SETTINGS.get(SECURITY_BASIC_REMEMBER_ME_VALID_TIME)
            : setting.getValue();
        rememberMeServices.setValiditySeconds(Integer.parseInt(rememberMeValiditySeconds));
        return configurer -> configurer.rememberMeServices(rememberMeServices);
    }

    /**
     * csrf
     *
     * @return {@link CsrfConfigurer}
     */
    public Customizer<CsrfConfigurer<HttpSecurity>> withCsrfConfigurerDefaults(RequestMatcher... ignoringRequestMatchers) {
        return csrf -> {
            CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
            repository.setCookieName(DEFAULT_CSRF_COOKIE_NAME);
            repository.setHeaderName(DEFAULT_CSRF_HEADER_NAME);
            csrf.csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler());
            csrf.ignoringRequestMatchers(ignoringRequestMatchers).csrfTokenRepository(repository);
        };
    }

    /**
     * 表单登录
     *
     * @return {@link FormLoginConfigurer}
     */
    public FormLoginConfigurer<HttpSecurity> withFormLoginConfigurer() {
        // @formatter:off
        AuthenticationSuccessHandler successHandler = new ConsoleAuthenticationSuccessHandler(administratorRepository,  auditEventPublish );
        FormLoginConfigurer<HttpSecurity> configurer=new FormLoginConfigurer<>();
        configurer.successHandler(successHandler);
        configurer.failureHandler(new ConsoleAuthenticationFailureHandler());
        return configurer;
        // @formatter:on
    }

    /**
     * 身份验证成功事件监听器
     *
     * @return {@link  ConsoleAuthenticationSuccessEventListener}
     */
    @Bean
    @ConditionalOnMissingBean
    public ConsoleAuthenticationSuccessEventListener authenticationSuccessEventListener() {
        return new ConsoleAuthenticationSuccessEventListener();
    }

    /**
     * 身份验证失败事件监听器
     *
     * @return {@link  ConsoleAuthenticationFailureEventListener}
     */
    @Bean
    @ConditionalOnMissingBean
    public ConsoleAuthenticationFailureEventListener authenticationFailureEventListener() {
        return new ConsoleAuthenticationFailureEventListener();
    }

    /**
     * 退出成功事件监听器
     *
     * @return {@link  ConsoleLogoutSuccessEventListener}
     */
    @Bean
    @ConditionalOnMissingBean
    public ConsoleLogoutSuccessEventListener logoutSuccessEventListener() {
        return new ConsoleLogoutSuccessEventListener();
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        ObjectMapper mapper = SupportJackson2Module.objectMapperBuilder(this.loader)
            .addModule(new AuthenticationJacksonModule())
            .changeDefaultPropertyInclusion(v -> v.withValueInclusion(JsonInclude.Include.NON_NULL))
            .build();
        return new GenericJacksonJsonRedisSerializer(mapper);
    }

    /**
     * WebAuthenticationDetailsSource
     *
     * @param geoLocationParser {@link GeoLocationParser}
     * @return {@link WebAuthenticationDetailsSource}
     */
    @Bean
    public WebAuthenticationDetailsSource authenticationDetailsSource(GeoLocationParser geoLocationParser,
                                                                      UserAgentParser userAgentParser) {
        return new WebAuthenticationDetailsSource(geoLocationParser, userAgentParser);
    }

    private ClassLoader loader;

    @Override
    public void setBeanClassLoader(@NonNull ClassLoader classLoader) {
        this.loader = classLoader;
    }

    /**
     * AdministratorRepository
     */
    private final AdministratorRepository administratorRepository;

    /**
     * SettingRepository
     */
    private final SettingRepository       settingRepository;

    /**
     * AuditEventPublish
     */
    private final AuditEventPublish       auditEventPublish;

    /**
     * UserAgentParser
     */
    private final UserAgentParser         userAgentParser;

    /**
     * CORS allow-list. Empty (default) disables CORS — appropriate for
     * same-origin deployments where the SPA is bundled with the API.
     */
    @Value("${ulp.security.cors.allowed-origins:}")
    private String[]                      corsAllowedOrigins;

}
