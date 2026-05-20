/*
 * eiam-protocol-oidc - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.eiam.protocol.oidc.configurers;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import cn.frank.ulp.protocol.code.EndpointMatcher;
import cn.frank.ulp.protocol.code.configurer.AbstractConfigurer;
import cn.topiam.eiam.protocol.oidc.endpoint.OidcProviderConfigurationEndpointFilter;
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
