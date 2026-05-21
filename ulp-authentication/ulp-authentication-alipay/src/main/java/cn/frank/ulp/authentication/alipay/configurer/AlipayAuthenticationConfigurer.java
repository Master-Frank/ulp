/*
 * ulp-authentication-alipay - United Login Platform
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
package cn.frank.ulp.authentication.alipay.configurer;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import cn.frank.ulp.authentication.alipay.filter.AlipayAuthorizationRequestRedirectFilter;
import cn.frank.ulp.authentication.alipay.filter.AlipayLoginAuthenticationFilter;
import cn.frank.ulp.authentication.common.IdentityProviderAuthenticationService;
import cn.frank.ulp.authentication.common.client.RegisteredIdentityProviderClientRepository;

import lombok.NonNull;
import lombok.Setter;
import static cn.frank.ulp.support.security.util.HttpSecurityFilterOrderRegistrationUtils.putFilterBefore;

/**
 * 认证配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/8/19 15:52
 */
public class AlipayAuthenticationConfigurer extends
                                            AbstractAuthenticationFilterConfigurer<HttpSecurity, AlipayAuthenticationConfigurer, AlipayLoginAuthenticationFilter> {
    @Setter
    @NonNull
    private String                                           loginProcessingUrl = AlipayLoginAuthenticationFilter.DEFAULT_FILTER_PROCESSES_URI;

    private final RegisteredIdentityProviderClientRepository registeredIdentityProviderClientRepository;
    private final IdentityProviderAuthenticationService      identityProviderAuthenticationService;

    AlipayAuthenticationConfigurer(RegisteredIdentityProviderClientRepository registeredIdentityProviderClientRepository,
                                   IdentityProviderAuthenticationService identityProviderAuthenticationService) {
        Assert.notNull(registeredIdentityProviderClientRepository,
            "registeredIdentityProviderClientRepository must not be null");
        Assert.notNull(identityProviderAuthenticationService, "userIdpService must not be null");
        this.registeredIdentityProviderClientRepository = registeredIdentityProviderClientRepository;
        this.identityProviderAuthenticationService = identityProviderAuthenticationService;
    }

    @Override
    public void init(HttpSecurity http) throws Exception {
        //支付宝登录认证
        this.setAuthenticationFilter(new AlipayLoginAuthenticationFilter(
            identityProviderAuthenticationService, registeredIdentityProviderClientRepository));
        putFilterBefore(http, this.getAuthenticationFilter(),
            OAuth2LoginAuthenticationFilter.class);

        //支付宝请求重定向
        http.addFilterBefore(
            new AlipayAuthorizationRequestRedirectFilter(
                registeredIdentityProviderClientRepository),
            OAuth2AuthorizationRequestRedirectFilter.class);

        //登录处理地址
        super.loginProcessingUrl(this.loginProcessingUrl);
        super.init(http);
    }

    /**
     * Create the {@link RequestMatcher} given a loginProcessingUrl
     *
     * @param loginProcessingUrl creates the {@link RequestMatcher} based upon the
     *                           loginProcessingUrl
     * @return the {@link RequestMatcher} to use based upon the loginProcessingUrl
     */
    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, HttpMethod.GET.name());
    }

    public RequestMatcher getRequestMatcher() {
        return new OrRequestMatcher(AlipayAuthorizationRequestRedirectFilter.getRequestMatcher(),
            AlipayLoginAuthenticationFilter.getRequestMatcher());
    }

    public static AlipayAuthenticationConfigurer alipayOauth(RegisteredIdentityProviderClientRepository registeredIdentityProviderClientRepository,
                                                             IdentityProviderAuthenticationService identityProviderAuthenticationService) {
        return new AlipayAuthenticationConfigurer(registeredIdentityProviderClientRepository,
            identityProviderAuthenticationService);
    }
}
