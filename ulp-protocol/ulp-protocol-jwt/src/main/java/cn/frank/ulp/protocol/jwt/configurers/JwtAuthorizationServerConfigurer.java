/*
 * ulp-protocol-jwt - United Login Platform
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
package cn.frank.ulp.protocol.jwt.configurers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import cn.frank.ulp.application.ApplicationServiceLoader;
import cn.frank.ulp.protocol.code.EndpointMatcher;
import cn.frank.ulp.protocol.code.UnauthorizedAuthenticationEntryPoint;
import cn.frank.ulp.protocol.code.configurer.AbstractConfigurer;
import cn.frank.ulp.protocol.jwt.context.JwtAuthorizationServerContextFilter;
import cn.frank.ulp.support.web.useragent.UserAgentParser;
import static cn.frank.ulp.protocol.code.configurer.AuthenticationUtils.getApplicationServiceLoader;

/**
 * 认证配置
 *
 * @author Frank Zhang
 */
public final class JwtAuthorizationServerConfigurer extends
                                                    AbstractHttpConfigurer<JwtAuthorizationServerConfigurer, HttpSecurity> {
    private final Map<Class<? extends AbstractConfigurer>, AbstractConfigurer> configurers      = createConfigurers();

    /**
     * 端点匹配器
     */
    private final List<EndpointMatcher>                                        endpointMatchers = new ArrayList<>();

    public RequestMatcher getEndpointsMatcher() {
        // Return a deferred RequestMatcher
        // since endpointsMatcher is constructed in init(HttpSecurity).
        return request -> new OrRequestMatcher(
            endpointMatchers.stream().map(EndpointMatcher::getRequestMatcher).toList())
            .matches(request);
    }

    @Override
    public void init(HttpSecurity httpSecurity) {
        this.configurers.values().forEach(configurer -> {
            configurer.init(httpSecurity);
            endpointMatchers.add(configurer.getEndpointMatcher());
        });
        httpSecurity.exceptionHandling(exceptionHandling -> {
            if (exceptionHandling != null) {
                //身份验证入口点
                exceptionHandling.authenticationEntryPoint(
                    new UnauthorizedAuthenticationEntryPoint(userAgentParser));
            }
        });

    }

    @Override
    public void configure(HttpSecurity httpSecurity) {
        //@formatter:off
        //Authorization server context filter
        ApplicationServiceLoader applicationServiceLoader = getApplicationServiceLoader(httpSecurity);
        JwtAuthorizationServerContextFilter authorizationServerContextFilter = new JwtAuthorizationServerContextFilter(this.endpointMatchers, applicationServiceLoader);
        httpSecurity.addFilterAfter(postProcess(authorizationServerContextFilter), SecurityContextHolderFilter.class);
        this.configurers.values().forEach(configurer -> configurer.configure(httpSecurity));
        //@formatter:on
    }

    /**
     * createConfigurers
     *
     * @return {@link AbstractConfigurer}
     */
    private Map<Class<? extends AbstractConfigurer>, AbstractConfigurer> createConfigurers() {
        //@formatter:off
        Map<Class<? extends AbstractConfigurer>, AbstractConfigurer> configurers = new LinkedHashMap<>();
        configurers.put(JwtLoginAuthorizationEndpointConfigurer.class, new JwtLoginAuthorizationEndpointConfigurer(this::postProcess));
        configurers.put(JwtLogoutAuthorizationEndpointConfigurer.class, new JwtLogoutAuthorizationEndpointConfigurer(this::postProcess));
        //@formatter:on
        return configurers;
    }

    private final UserAgentParser userAgentParser;

    public JwtAuthorizationServerConfigurer(UserAgentParser userAgentParser) {
        this.userAgentParser = userAgentParser;
    }
}
