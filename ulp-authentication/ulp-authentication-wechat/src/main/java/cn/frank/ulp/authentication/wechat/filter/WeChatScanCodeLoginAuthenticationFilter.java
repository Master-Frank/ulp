/*
 * ulp-authentication-wechat - United Login Platform
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
package cn.frank.ulp.authentication.wechat.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import cn.frank.ulp.authentication.common.IdentityProviderAuthenticationService;
import cn.frank.ulp.authentication.common.authentication.IdentityProviderUserDetails;
import cn.frank.ulp.authentication.common.client.RegisteredIdentityProviderClient;
import cn.frank.ulp.authentication.common.client.RegisteredIdentityProviderClientRepository;
import cn.frank.ulp.authentication.common.filter.AbstractIdentityProviderAuthenticationProcessingFilter;
import cn.frank.ulp.authentication.wechat.WeChatIdentityProviderOAuth2Config;
import cn.frank.ulp.core.context.ContextService;
import cn.frank.ulp.support.exception.UlpException;
import cn.frank.ulp.support.util.HttpClientUtils;
import cn.frank.ulp.support.util.UrlUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static org.springframework.security.oauth2.core.AuthorizationGrantType.AUTHORIZATION_CODE;

import static cn.frank.ulp.authentication.common.IdentityProviderType.WECHAT;
import static cn.frank.ulp.authentication.common.constant.AuthenticationConstants.*;
import static cn.frank.ulp.authentication.wechat.constant.WeChatAuthenticationConstants.QrConnect.*;

/**
 * 微信扫码登录过滤器
 *
 * @author Frank Zhang
 */
@SuppressWarnings("DuplicatedCode")
public class WeChatScanCodeLoginAuthenticationFilter extends
                                                     AbstractIdentityProviderAuthenticationProcessingFilter {

    public final static String                DEFAULT_FILTER_PROCESSES_URI = WECHAT
        .getLoginPathPrefix() + "/" + "{" + PROVIDER_CODE + "}";
    public static final AntPathRequestMatcher REQUEST_MATCHER              = new AntPathRequestMatcher(
        DEFAULT_FILTER_PROCESSES_URI, HttpMethod.GET.name());

    /**
     * Creates a new instance
     *
     * @param registeredIdentityProviderClientRepository the {@link RegisteredIdentityProviderClientRepository}
     * @param identityProviderAuthenticationService  {@link  IdentityProviderAuthenticationService}
     */
    public WeChatScanCodeLoginAuthenticationFilter(RegisteredIdentityProviderClientRepository registeredIdentityProviderClientRepository,
                                                   IdentityProviderAuthenticationService identityProviderAuthenticationService) {
        super(REQUEST_MATCHER, identityProviderAuthenticationService,
            registeredIdentityProviderClientRepository);
    }

    /**
     * 微信认证
     *
     * @param request  {@link  HttpServletRequest}
     * @param response {@link  HttpServletRequest}
     * @return {@link  HttpServletRequest}
     * @throws AuthenticationException {@link  AuthenticationException} AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException,
                                                                              IOException {
        OAuth2AuthorizationRequest authorizationRequest = getOauth2AuthorizationRequest(request,
            response);
        RequestMatcher.MatchResult matcher = REQUEST_MATCHER.matcher(request);
        Map<String, String> variables = matcher.getVariables();
        String providerCode = variables.get(PROVIDER_CODE);
        String providerId = getIdentityProviderId(providerCode);
        //code
        String code = request.getParameter(OAuth2ParameterNames.CODE);
        if (StringUtils.isEmpty(code)) {
            logger.error("微信开放平台扫码登录 code 参数不存在，认证失败");
            OAuth2Error oauth2Error = new OAuth2Error(INVALID_CODE_PARAMETER_ERROR_CODE);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        // state
        String state = request.getParameter(OAuth2ParameterNames.STATE);
        if (StringUtils.isEmpty(state)) {
            logger.error("微信开放平台扫码登录 state 参数不存在，认证失败");
            OAuth2Error oauth2Error = new OAuth2Error(INVALID_STATE_PARAMETER_ERROR_CODE);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        if (!authorizationRequest.getState().equals(state)) {
            logger.error("微信开放平台扫码登录 state 匹配不一致，认证失败");
            OAuth2Error oauth2Error = new OAuth2Error(INVALID_STATE_PARAMETER_ERROR_CODE);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        //获取身份提供商
        RegisteredIdentityProviderClient<WeChatIdentityProviderOAuth2Config> provider = getRegisteredIdentityProviderClient(
            providerCode);
        WeChatIdentityProviderOAuth2Config config = provider.getConfig();
        if (Objects.isNull(config)) {
            logger.error("未查询到微信扫码登录配置");
            //无效身份提供商
            OAuth2Error oauth2Error = new OAuth2Error(
                AbstractIdentityProviderAuthenticationProcessingFilter.INVALID_IDP_CONFIG);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        //获取access token
        HashMap<String, String> param = new HashMap<>(16);
        param.put(APP_ID, config.getAppId());
        param.put(SECRET, config.getAppSecret());
        param.put(OAuth2ParameterNames.CODE, code);
        param.put(OAuth2ParameterNames.GRANT_TYPE, AUTHORIZATION_CODE.getValue());
        JSONObject result = JSON.parseObject(HttpClientUtils.get(ACCESS_TOKEN, param));
        if (result.containsKey(ERROR_CODE)) {
            logger.error("获取access_token发生错误:  " + result.toJSONString());
            throw new UlpException("获取access_token发生错误:  " + result.toJSONString());
        }
        // 获取user信息
        param = new HashMap<>(16);
        param.put(OAuth2ParameterNames.ACCESS_TOKEN,
            result.getString(OAuth2ParameterNames.ACCESS_TOKEN));
        param.put(OidcScopes.OPENID, result.getString(OidcScopes.OPENID));
        param.put(LANG, "zh_CN");
        result = JSON.parseObject(HttpClientUtils.get(USER_INFO, param));
        if (result.containsKey(ERROR_CODE)) {
            logger.error("获取微信用户个人信息发生错误:  " + result.toJSONString());
            throw new UlpException("获取微信用户个人信息发生错误:  " + result.toJSONString());
        }
        // 返回
        IdentityProviderUserDetails identityProviderUserDetails = IdentityProviderUserDetails
            .builder().openId(param.get(OidcScopes.OPENID)).unionId(result.getString(UNION_ID))
            .avatarUrl(result.getString(HEADIMGURL)).providerCode(providerCode)
            .nickName(result.getString(NICK_NAME)).providerId(providerId).providerType(WECHAT)
            .build();
        return attemptAuthentication(request, response, identityProviderUserDetails);
    }

    public static String getLoginUrl(String providerId) {
        String url = ContextService.getPortalPublicBaseUrl() + WECHAT.getLoginPathPrefix() + "/"
                     + providerId;
        return UrlUtils.format(url);
    }

    public static RequestMatcher getRequestMatcher() {
        return REQUEST_MATCHER;
    }
}
