/*
 * ulp-protocol-form - United Login Platform
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
package cn.frank.ulp.protocol.form.configurers;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import cn.frank.ulp.protocol.code.EndpointMatcher;
import cn.frank.ulp.protocol.code.configurer.AbstractConfigurer;
import cn.frank.ulp.protocol.code.configurer.AuthenticationUtils;
import cn.frank.ulp.protocol.form.FormAuthorizationService;
import cn.frank.ulp.protocol.form.authentication.FormAuthenticationTokenProvider;
import cn.frank.ulp.protocol.form.endpoint.FormAuthenticationEndpointFilter;
import static cn.frank.ulp.common.constant.ProtocolConstants.FormEndpointConstants.FORM_SSO_PATH;
import static cn.frank.ulp.common.constant.ProtocolConstants.FormEndpointConstants.IDP_FORM_SSO_INITIATOR;

/**
 *
 * @author Frank Zhang
 */
public class FormAuthenticationEndpointConfigurer extends AbstractConfigurer {
    private RequestMatcher requestMatcher;

    public FormAuthenticationEndpointConfigurer(ObjectPostProcessor<Object> objectPostProcessor) {
        super(objectPostProcessor);
    }

    /**
     * init
     *
     * @param httpSecurity {@link HttpSecurity}
     */
    @Override
    public void init(HttpSecurity httpSecurity) {
        FormAuthorizationService authorizationService = FormAuthenticationUtils
            .getAuthorizationService(httpSecurity);
        requestMatcher = new OrRequestMatcher(
            new AntPathRequestMatcher(IDP_FORM_SSO_INITIATOR, HttpMethod.POST.name()),
            new AntPathRequestMatcher(IDP_FORM_SSO_INITIATOR, HttpMethod.GET.name()),
            new AntPathRequestMatcher(FORM_SSO_PATH, HttpMethod.POST.name()),
            new AntPathRequestMatcher(FORM_SSO_PATH, HttpMethod.GET.name()));
        httpSecurity
            .authenticationProvider(new FormAuthenticationTokenProvider(authorizationService));
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
        FormAuthenticationEndpointFilter singleSignOnEndpointFilter = new FormAuthenticationEndpointFilter(
            requestMatcher, authenticationManager);
        singleSignOnEndpointFilter.setAuthenticationDetailsSource(
            AuthenticationUtils.getAuthenticationDetailsSource(httpSecurity));
        httpSecurity.addFilterBefore(postProcess(singleSignOnEndpointFilter),
            AuthorizationFilter.class);
    }

    @Override
    public EndpointMatcher getEndpointMatcher() {
        return new EndpointMatcher(this.requestMatcher, true);
    }
}
