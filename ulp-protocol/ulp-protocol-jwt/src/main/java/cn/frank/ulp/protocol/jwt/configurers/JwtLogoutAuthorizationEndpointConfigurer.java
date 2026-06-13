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

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import cn.frank.ulp.protocol.code.EndpointMatcher;
import cn.frank.ulp.protocol.code.configurer.AbstractConfigurer;
import cn.frank.ulp.protocol.code.configurer.AuthenticationUtils;
import cn.frank.ulp.protocol.jwt.authentication.JwtLogoutAuthenticationProvider;
import cn.frank.ulp.protocol.jwt.endpoint.JwtLogoutAuthenticationEndpointFilter;
import static cn.frank.ulp.common.constant.ProtocolConstants.JwtEndpointConstants.JWT_SLO_PATH;

/**
 *
 * @author Frank Zhang
 */
public class JwtLogoutAuthorizationEndpointConfigurer extends AbstractConfigurer {

    private RequestMatcher requestMatcher;

    public JwtLogoutAuthorizationEndpointConfigurer(ObjectPostProcessor<Object> objectPostProcessor) {
        super(objectPostProcessor);
    }

    /**
     * init
     *
     * @param httpSecurity {@link HttpSecurity}
     */
    @Override
    public void init(HttpSecurity httpSecurity) {
        requestMatcher = new OrRequestMatcher(
            PathPatternRequestMatcher.pathPattern(HttpMethod.GET, JWT_SLO_PATH),
            PathPatternRequestMatcher.pathPattern(HttpMethod.POST, JWT_SLO_PATH));
        httpSecurity.authenticationProvider(new JwtLogoutAuthenticationProvider());
    }

    /**
     * configure
     *
     * @param httpSecurity {@link HttpSecurity}
     */
    @Override
    public void configure(HttpSecurity httpSecurity) {
        AuthenticationManager authenticationManager = httpSecurity
            .getSharedObject(AuthenticationManager.class);
        JwtLogoutAuthenticationEndpointFilter logoutAuthenticationEndpointFilter = new JwtLogoutAuthenticationEndpointFilter(
            requestMatcher, authenticationManager);
        logoutAuthenticationEndpointFilter.setAuthenticationDetailsSource(
            AuthenticationUtils.getAuthenticationDetailsSource(httpSecurity));
        httpSecurity.addFilterBefore(postProcess(logoutAuthenticationEndpointFilter),
            LogoutFilter.class);
    }

    @Override
    public EndpointMatcher getEndpointMatcher() {
        return new EndpointMatcher(this.requestMatcher, false);
    }
}
