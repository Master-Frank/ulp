/*
 * ulp-openapi - United Login Platform
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
package cn.frank.ulp.openapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import cn.frank.ulp.openapi.authorization.AccessTokenAuthenticationEntryPoint;
import cn.frank.ulp.openapi.authorization.AccessTokenAuthenticationFilter;
import cn.frank.ulp.openapi.authorization.AccessTokenAuthenticationProvider;
import cn.frank.ulp.openapi.authorization.store.AccessTokenStore;
import cn.frank.ulp.openapi.authorization.store.RedisAccessTokenStore;
import cn.frank.ulp.support.web.useragent.UserAgentParser;
import static cn.frank.ulp.common.constant.ConfigBeanNameConstants.DEFAULT_SECURITY_FILTER_CHAIN;
import static cn.frank.ulp.openapi.constant.OpenApiV1Constants.AUTH_PATH;
import static cn.frank.ulp.openapi.constant.OpenApiV1Constants.OPEN_API_V1_PATH;

/**
 * ConsoleSecurityConfiguration
 *
 * @author Frank Zhang
 */
@EnableMethodSecurity
@Configuration
public class OpenApiSecurityConfiguration {

    /**
     * securityFilterChain
     *
     * @param http {@link HttpSecurity}
     * @param accessTokenStore {@link AccessTokenStore}
     * @return {@link SecurityFilterChain}
     */
    @Bean(name = DEFAULT_SECURITY_FILTER_CHAIN)
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AccessTokenStore accessTokenStore) throws Exception {
        ProviderManager providerManager = new ProviderManager(
            new AccessTokenAuthenticationProvider(accessTokenStore));
        http.securityMatcher(OPEN_API_V1_PATH + "/**");
        http.authorizeHttpRequests(registry -> {
            registry.requestMatchers(new AntPathRequestMatcher(AUTH_PATH + "/access_token"))
                .permitAll();
            registry.anyRequest().authenticated();
        });
        //@formatter:off
        //关闭 csrf
        http.csrf(AbstractHttpConfigurer::disable);
        //关闭 cors
        http.cors(AbstractHttpConfigurer::disable);
        //关闭 session
        http.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //异常处理器
        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(new AccessTokenAuthenticationEntryPoint(userAgentParser)));
        http.addFilterBefore(new AccessTokenAuthenticationFilter(providerManager),BasicAuthenticationFilter.class);
        return http.build();
        //@formatter:on
    }

    /**
     * TokenStore
     *
     * @param redisTemplate {@link RedisTemplate}
     * @return {@link AccessTokenStore}
     */
    @Bean
    public AccessTokenStore tokenStore(RedisTemplate<Object, Object> redisTemplate) {
        return new RedisAccessTokenStore(redisTemplate);
    }

    private final UserAgentParser userAgentParser;

    public OpenApiSecurityConfiguration(UserAgentParser userAgentParser) {
        this.userAgentParser = userAgentParser;
    }

}
