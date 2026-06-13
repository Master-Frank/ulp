/*
 * ulp-authentication-qq - United Login Platform
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
package cn.frank.ulp.authentication.qq.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import cn.frank.ulp.authentication.common.IdentityProviderAuthenticationService;
import cn.frank.ulp.authentication.common.authentication.IdentityProviderUserDetails;
import cn.frank.ulp.authentication.common.client.RegisteredIdentityProviderClient;
import cn.frank.ulp.authentication.common.client.RegisteredIdentityProviderClientRepository;
import cn.frank.ulp.authentication.common.filter.AbstractIdentityProviderAuthenticationProcessingFilter;
import cn.frank.ulp.authentication.qq.QqIdentityProviderOAuth2Config;
import cn.frank.ulp.core.context.ContextService;
import cn.frank.ulp.support.exception.UlpException;
import cn.frank.ulp.support.trace.TraceUtils;
import cn.frank.ulp.support.util.HttpClientUtils;
import cn.frank.ulp.support.util.UrlUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static com.nimbusds.oauth2.sdk.GrantType.AUTHORIZATION_CODE;

import static cn.frank.ulp.authentication.common.IdentityProviderType.QQ_OAUTH;
import static cn.frank.ulp.authentication.common.constant.AuthenticationConstants.*;
import static cn.frank.ulp.authentication.qq.constant.QqAuthenticationConstants.URL_GET_ACCESS_TOKEN;
import static cn.frank.ulp.authentication.qq.constant.QqAuthenticationConstants.URL_GET_OPEN_ID;

/**
 * QQ登录
 *
 * @author Frank Zhang
 */
@SuppressWarnings({ "AlibabaClassNamingShouldBeCamel", "DuplicatedCode" })
public class QqOAuth2LoginAuthenticationFilter extends
                                               AbstractIdentityProviderAuthenticationProcessingFilter {
    final String                       ERROR_CODE                   = "error";
    public final static String         DEFAULT_FILTER_PROCESSES_URI = QQ_OAUTH.getLoginPathPrefix()
                                                                      + "/" + "{" + PROVIDER_CODE
                                                                      + "}";
    public static final RequestMatcher REQUEST_MATCHER              = PathPatternRequestMatcher
        .pathPattern(HttpMethod.GET, DEFAULT_FILTER_PROCESSES_URI);

    /**
     * Creates a new instance
     *
     * @param registeredIdentityProviderClientRepository the {@link RegisteredIdentityProviderClientRepository}
     * @param identityProviderAuthenticationService  {@link  IdentityProviderAuthenticationService}
     */
    public QqOAuth2LoginAuthenticationFilter(RegisteredIdentityProviderClientRepository registeredIdentityProviderClientRepository,
                                             IdentityProviderAuthenticationService identityProviderAuthenticationService) {
        super(REQUEST_MATCHER, identityProviderAuthenticationService,
            registeredIdentityProviderClientRepository);
    }

    /**
     * QQ认证
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
        TraceUtils.put(UUID.randomUUID().toString());
        RequestMatcher.MatchResult matcher = REQUEST_MATCHER.matcher(request);
        Map<String, String> variables = matcher.getVariables();
        String providerCode = variables.get(PROVIDER_CODE);
        String providerId = getIdentityProviderId(providerCode);
        //code
        String code = request.getParameter(OAuth2ParameterNames.CODE);
        if (StringUtils.isEmpty(code)) {
            OAuth2Error oauth2Error = new OAuth2Error(INVALID_CODE_PARAMETER_ERROR_CODE);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        // state
        String state = request.getParameter(OAuth2ParameterNames.STATE);
        if (StringUtils.isEmpty(state)) {
            OAuth2Error oauth2Error = new OAuth2Error(INVALID_STATE_PARAMETER_ERROR_CODE);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        if (!authorizationRequest.getState().equals(state)) {
            OAuth2Error oauth2Error = new OAuth2Error(INVALID_STATE_PARAMETER_ERROR_CODE);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        //获取身份提供商
        RegisteredIdentityProviderClient<QqIdentityProviderOAuth2Config> provider = getRegisteredIdentityProviderClient(
            providerCode);
        QqIdentityProviderOAuth2Config config = provider.getConfig();
        if (Objects.isNull(config)) {
            logger.error("未查询到QQ登录配置");
            //无效身份提供商
            OAuth2Error oauth2Error = new OAuth2Error(INVALID_IDP_CONFIG);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        //获取access token
        HashMap<String, String> param = new HashMap<>(16);
        param.put(OAuth2ParameterNames.GRANT_TYPE, AUTHORIZATION_CODE.getValue());
        param.put(OAuth2ParameterNames.CLIENT_ID, config.getAppId().trim());
        param.put(OAuth2ParameterNames.CLIENT_SECRET, config.getAppKey().trim());
        param.put(OAuth2ParameterNames.CODE, code.trim());
        param.put(OAuth2ParameterNames.REDIRECT_URI, getLoginUrl(provider.getCode()));
        param.put("fmt", "json");
        //注意：QQ不能使用编码后的get请求，否则会报 {"error_description":"redirect uri is illegal","error":100010}
        JSONObject result = JSON.parseObject(HttpClientUtils.doGet(URL_GET_ACCESS_TOKEN, param));
        if (!Objects.isNull(result.getString(ERROR_CODE))) {
            logger.error("获取access_token发生错误: {}" + result.toJSONString());
            throw new UlpException("获取access_token发生错误:  " + result.toJSONString());
        }
        // 获取openId信息
        param = new HashMap<>(16);
        param.put(OAuth2ParameterNames.ACCESS_TOKEN,
            result.getString(OAuth2ParameterNames.ACCESS_TOKEN));
        param.put("fmt", "json");
        result = JSON.parseObject(HttpClientUtils.doGet(URL_GET_OPEN_ID, param));
        if (!Objects.isNull(result.getString(ERROR_CODE))) {
            logger.error("获取QQ用户OpenID发生错误: {}" + result.toJSONString());
            throw new UlpException("获取QQ用户OpenID发生错误:  " + result.toJSONString());
        }
        // 返回
        String openId = result.getString(OidcScopes.OPENID);
        IdentityProviderUserDetails identityProviderUserDetails = IdentityProviderUserDetails
            .builder().openId(openId).providerType(QQ_OAUTH).providerCode(providerCode)
            .providerId(providerId).build();
        return attemptAuthentication(request, response, identityProviderUserDetails);

    }

    public static String getLoginUrl(String providerId) {
        String url = ContextService.getPortalPublicBaseUrl() + "/" + QQ_OAUTH.getLoginPathPrefix()
                     + "/" + providerId;
        return UrlUtils.format(url);
    }

    public static RequestMatcher getRequestMatcher() {
        return REQUEST_MATCHER;
    }
}
