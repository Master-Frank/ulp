/*
 * eiam-authentication-alipay - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.authentication.alipay.filter;

import java.io.IOException;
import java.util.*;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cn.frank.ulp.authentication.alipay.AlipayIdentityProviderOAuth2Config;
import cn.frank.ulp.authentication.common.client.RegisteredIdentityProviderClient;
import cn.frank.ulp.authentication.common.client.RegisteredIdentityProviderClientRepository;
import cn.frank.ulp.support.trace.TraceUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.CLIENT_ID;

import static cn.frank.ulp.authentication.alipay.constant.AlipayAuthenticationConstants.*;
import static cn.frank.ulp.authentication.common.IdentityProviderType.ALIPAY_OAUTH;
import static cn.frank.ulp.authentication.common.constant.AuthenticationConstants.PROVIDER_CODE;

/**
 * 支付宝 登录请求重定向过滤器
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/8/19 17:56
 */
@SuppressWarnings("DuplicatedCode")
public class AlipayAuthorizationRequestRedirectFilter extends OncePerRequestFilter {
    /**
     * 重定向策略
     */
    private final RedirectStrategy                                           authorizationRedirectStrategy  = new DefaultRedirectStrategy();

    /**
     * 认证请求存储库
     */
    private final AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository = new HttpSessionOAuth2AuthorizationRequestRepository();

    private static final StringKeyGenerator                                  DEFAULT_STATE_GENERATOR        = new Base64StringKeyGenerator(
        Base64.getUrlEncoder());
    private final RegisteredIdentityProviderClientRepository                 registeredIdentityProviderClientRepository;

    public AlipayAuthorizationRequestRedirectFilter(RegisteredIdentityProviderClientRepository registeredIdentityProviderClientRepository) {
        this.registeredIdentityProviderClientRepository = registeredIdentityProviderClientRepository;
    }

    /**
     * AntPathRequestMatcher
     */
    public static final AntPathRequestMatcher ALIPAY_REQUEST_MATCHER = new AntPathRequestMatcher(
        ALIPAY_OAUTH.getAuthorizationPathPrefix() + "/" + "{" + PROVIDER_CODE + "}",
        HttpMethod.GET.name());

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException,
                                                                      IOException {
        RequestMatcher.MatchResult matcher = ALIPAY_REQUEST_MATCHER.matcher(request);
        if (!matcher.isMatch()) {
            filterChain.doFilter(request, response);
            return;
        }
        TraceUtils.put(UUID.randomUUID().toString());
        Map<String, String> variables = matcher.getVariables();
        String providerCode = variables.get(PROVIDER_CODE);
        Optional<RegisteredIdentityProviderClient<AlipayIdentityProviderOAuth2Config>> optional = registeredIdentityProviderClientRepository
            .findByCode(providerCode);
        if (optional.isEmpty()) {
            throw new NullPointerException("未查询到身份提供商信息");
        }
        RegisteredIdentityProviderClient<AlipayIdentityProviderOAuth2Config> entity = optional
            .get();
        AlipayIdentityProviderOAuth2Config config = entity.getConfig();
        Assert.notNull(config, "支付宝登录配置不能为空");
        //构建授权请求
        //@formatter:off
        HashMap<@Nullable String, @Nullable Object> attributes = Maps.newHashMap();
        OAuth2AuthorizationRequest.Builder builder = OAuth2AuthorizationRequest.authorizationCode()
                .clientId(config.getAppId())
                .scopes(Sets.newHashSet(USER_INFO_SCOPE))
                .authorizationUri(AUTHORIZATION_REQUEST)
                .redirectUri(AlipayLoginAuthenticationFilter.getLoginUrl(optional.get().getCode()))
                .state(DEFAULT_STATE_GENERATOR.generateKey())
                .attributes(attributes);
        builder.parameters(parameters -> {
            parameters.put(APP_ID, parameters.get(CLIENT_ID));
            parameters.remove(CLIENT_ID);
        });
        //@formatter:on
        this.sendRedirectForAuthorization(request, response, builder.build());
    }

    /**
     * getRequestMatcher
     *
     * @return {@link RequestMatcher}
     */
    public static RequestMatcher getRequestMatcher() {
        return ALIPAY_REQUEST_MATCHER;
    }

    private void sendRedirectForAuthorization(HttpServletRequest request,
                                              HttpServletResponse response,
                                              OAuth2AuthorizationRequest authorizationRequest) throws IOException {
        this.authorizationRequestRepository.saveAuthorizationRequest(authorizationRequest, request,
            response);
        this.authorizationRedirectStrategy.sendRedirect(request, response,
            authorizationRequest.getAuthorizationRequestUri());
    }
}
