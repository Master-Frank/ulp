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

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import cn.frank.ulp.protocol.code.EndpointMatcher;
import cn.frank.ulp.protocol.code.configurer.AbstractConfigurer;
import cn.frank.ulp.protocol.oidc.endpoint.OidcProviderConfigurationEndpointFilter;
import static cn.frank.ulp.common.constant.ProtocolConstants.OidcEndpointConstants.WELL_KNOWN_OPENID_CONFIGURATION;

/**
 * OIDC 服务配置端点配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/6/27 21:09
 */
public final class OidcProviderConfigurationEndpointConfigurer extends AbstractConfigurer {

    private RequestMatcher requestMatcher;

    /**
     * Restrict for internal use only.
     */
    OidcProviderConfigurationEndpointConfigurer(ObjectPostProcessor<Object> objectPostProcessor) {
        super(objectPostProcessor);
    }

    @Override
    public void init(HttpSecurity httpSecurity) {
        this.requestMatcher = new AntPathRequestMatcher(WELL_KNOWN_OPENID_CONFIGURATION,
            HttpMethod.GET.name());
    }

    @Override
    public void configure(HttpSecurity httpSecurity) {
        OidcProviderConfigurationEndpointFilter oidcProviderConfigurationEndpointFilter = new OidcProviderConfigurationEndpointFilter(
            this.requestMatcher);
        httpSecurity.addFilterBefore(postProcess(oidcProviderConfigurationEndpointFilter),
            AbstractPreAuthenticatedProcessingFilter.class);
    }

    @Override
    public EndpointMatcher getEndpointMatcher() {
        return new EndpointMatcher(this.requestMatcher, false);
    }
}
