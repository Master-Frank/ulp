/*
 * ulp-authentication-dingtalk - United Login Platform
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
package cn.frank.ulp.authentication.dingtalk.filter;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import cn.frank.ulp.authentication.common.client.RegisteredIdentityProviderClient;
import cn.frank.ulp.authentication.common.client.RegisteredIdentityProviderClientRepository;
import cn.frank.ulp.authentication.dingtalk.DingTalkIdentityProviderOAuth2Config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.RESPONSE_TYPE;

import static cn.frank.ulp.authentication.common.IdentityProviderType.DINGTALK_OAUTH;
import static cn.frank.ulp.authentication.common.constant.AuthenticationConstants.PROVIDER_CODE;
import static cn.frank.ulp.authentication.dingtalk.constant.DingTalkAuthenticationConstants.CORP_ID;
import static cn.frank.ulp.authentication.dingtalk.constant.DingTalkAuthenticationConstants.URL_AUTHORIZE;
import static cn.frank.ulp.authentication.dingtalk.filter.DingtalkOAuth2AuthenticationFilter.getLoginUrl;

/**
 * 微信扫码登录请求重定向过滤器
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/6/20 21:22
 */
@SuppressWarnings("ALL")
public class DingtalkOAuth2AuthorizationRequestRedirectFilter extends OncePerRequestFilter {

    private final Logger                                                     logger                          = LoggerFactory
        .getLogger(DingtalkOAuth2AuthorizationRequestRedirectFilter.class);

    /**
     * AntPathRequestMatcher
     */
    public static final AntPathRequestMatcher                                DINGTALK_OAUTH2_REQUEST_MATCHER = new AntPathRequestMatcher(
        DINGTALK_OAUTH.getAuthorizationPathPrefix() + "/" + "{" + PROVIDER_CODE + "}",
        HttpMethod.GET.name());

    /**
     * 重定向策略
     */
    private final RedirectStrategy                                           authorizationRedirectStrategy   = new DefaultRedirectStrategy();

    /**
     * 认证请求存储库
     */
    private final AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository  = new HttpSessionOAuth2AuthorizationRequestRepository();

    private static final StringKeyGenerator                                  DEFAULT_STATE_GENERATOR         = new Base64StringKeyGenerator(
        Base64.getUrlEncoder());
    private final RegisteredIdentityProviderClientRepository                 registeredIdentityProviderClientRepository;

    public DingtalkOAuth2AuthorizationRequestRedirectFilter(RegisteredIdentityProviderClientRepository registeredIdentityProviderClientRepository) {
        this.registeredIdentityProviderClientRepository = registeredIdentityProviderClientRepository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws IOException,
                                                                      ServletException {
        RequestMatcher.MatchResult matcher = DINGTALK_OAUTH2_REQUEST_MATCHER.matcher(request);
        if (!matcher.isMatch()) {
            filterChain.doFilter(request, response);
            return;
        }
        Map<String, String> variables = matcher.getVariables();
        String providerCode = variables.get(PROVIDER_CODE);
        Optional<RegisteredIdentityProviderClient<DingTalkIdentityProviderOAuth2Config>> optional = registeredIdentityProviderClientRepository
            .findByCode(providerCode);
        if (optional.isEmpty()) {
            throw new NullPointerException("未查询到身份提供商信息");
        }
        RegisteredIdentityProviderClient<DingTalkIdentityProviderOAuth2Config> entity = optional
            .get();
        DingTalkIdentityProviderOAuth2Config config = entity.getConfig();
        Assert.notNull(config, "钉钉登录配置不能为空");
        //构建授权请求
        OAuth2AuthorizationRequest.Builder builder = OAuth2AuthorizationRequest.authorizationCode()
            .clientId(config.getAppKey()).authorizationUri(URL_AUTHORIZE)
            .redirectUri(getLoginUrl(optional.get().getCode()))
            .state(DEFAULT_STATE_GENERATOR.generateKey());
        //添加CorpId
        if (StringUtils.isNotBlank(config.getCorpId())) {
            builder.scope(OidcScopes.OPENID + " corpid");
        } else {
            builder.scope(OidcScopes.OPENID);
        }
        builder.parameters(parameters -> {
            parameters.put(RESPONSE_TYPE, OAuth2ParameterNames.CODE);
            parameters.put("prompt", "consent");
            //添加CorpId
            if (StringUtils.isNotBlank(config.getCorpId())) {
                parameters.put(CORP_ID, config.getCorpId());
            }
        });
        this.sendRedirectForAuthorization(request, response, builder.build());
    }

    private void sendRedirectForAuthorization(HttpServletRequest request,
                                              HttpServletResponse response,
                                              OAuth2AuthorizationRequest authorizationRequest) throws IOException {
        this.authorizationRequestRepository.saveAuthorizationRequest(authorizationRequest, request,
            response);
        this.authorizationRedirectStrategy.sendRedirect(request, response,
            authorizationRequest.getAuthorizationRequestUri());
    }

    public static RequestMatcher getRequestMatcher() {
        return DINGTALK_OAUTH2_REQUEST_MATCHER;
    }
}
