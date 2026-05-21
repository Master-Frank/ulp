/*
 * ulp-authentication-feishu - United Login Platform
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
package cn.frank.ulp.authentication.feishu.configurer;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import cn.frank.ulp.authentication.common.IdentityProviderAuthenticationService;
import cn.frank.ulp.authentication.common.client.RegisteredIdentityProviderClientRepository;
import cn.frank.ulp.authentication.feishu.filter.FeiShuAuthorizationRequestRedirectFilter;
import cn.frank.ulp.authentication.feishu.filter.FeiShuLoginAuthenticationFilter;

import lombok.NonNull;
import lombok.Setter;
import static cn.frank.ulp.support.security.util.HttpSecurityFilterOrderRegistrationUtils.putFilterBefore;

/**
 * 认证配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/12/19 23:58
 */
public final class FeiShuAuthenticationConfigurer extends
                                                  AbstractAuthenticationFilterConfigurer<HttpSecurity, FeiShuAuthenticationConfigurer, FeiShuLoginAuthenticationFilter> {
    @Setter
    @NonNull
    private String                                           loginProcessingUrl = FeiShuLoginAuthenticationFilter.DEFAULT_FILTER_PROCESSES_URI;
    private final RegisteredIdentityProviderClientRepository registeredIdentityProviderClientRepository;
    private final IdentityProviderAuthenticationService      identityProviderAuthenticationService;

    public FeiShuAuthenticationConfigurer(RegisteredIdentityProviderClientRepository registeredIdentityProviderClientRepository,
                                          IdentityProviderAuthenticationService identityProviderAuthenticationService) {
        Assert.notNull(registeredIdentityProviderClientRepository,
            "registeredIdentityProviderClientRepository must not be null");
        Assert.notNull(identityProviderAuthenticationService, "userIdpService must not be null");
        this.registeredIdentityProviderClientRepository = registeredIdentityProviderClientRepository;
        this.identityProviderAuthenticationService = identityProviderAuthenticationService;
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

    @Override
    public void init(HttpSecurity http) throws Exception {
        //飞书登录认证
        this.setAuthenticationFilter(new FeiShuLoginAuthenticationFilter(
            registeredIdentityProviderClientRepository, identityProviderAuthenticationService));
        putFilterBefore(http, this.getAuthenticationFilter(),
            OAuth2LoginAuthenticationFilter.class);

        //飞书请求重定向
        http.addFilterBefore(
            new FeiShuAuthorizationRequestRedirectFilter(
                registeredIdentityProviderClientRepository),
            OAuth2AuthorizationRequestRedirectFilter.class);

        //登录处理网址
        super.loginProcessingUrl(this.loginProcessingUrl);
        super.init(http);
    }

    public RequestMatcher getRequestMatcher() {
        return new OrRequestMatcher(FeiShuAuthorizationRequestRedirectFilter.getRequestMatcher(),
            FeiShuLoginAuthenticationFilter.getRequestMatcher());
    }

    public static FeiShuAuthenticationConfigurer feiShuOAuth2(RegisteredIdentityProviderClientRepository registeredIdentityProviderClientRepository,
                                                              IdentityProviderAuthenticationService identityProviderAuthenticationService) {
        return new FeiShuAuthenticationConfigurer(registeredIdentityProviderClientRepository,
            identityProviderAuthenticationService);
    }
}
