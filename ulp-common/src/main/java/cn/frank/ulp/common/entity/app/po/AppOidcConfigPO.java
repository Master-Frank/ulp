/*
 * ulp-common - United Login Platform
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
package cn.frank.ulp.common.entity.app.po;

import cn.frank.ulp.common.entity.app.AppOidcConfigEntity;
import cn.frank.ulp.common.enums.app.AuthorizationType;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Frank Zhang
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AppOidcConfigPO extends AppOidcConfigEntity {

    /**
     * 应用编码
     */
    private String            appCode;

    /**
     * 应用名称
     */
    private String            appName;

    /**
     * 模版
     */
    private String            appTemplate;

    /**
     * 客户端ID
     */
    private String            clientId;

    /**
     * 客户端秘钥
     */
    private String            clientSecret;

    /**
     * SSO 登录链接
     */
    private String            initLoginUrl;

    /**
     * 授权范围
     */
    private AuthorizationType authorizationType;

    /**
     * 应用是否启用
     */
    private Boolean           enabled;

    /**
     * 是否配置
     */
    private Boolean           configured;

    public AppOidcConfigPO(AppOidcConfigEntity config, String appCode, String appName,
                           String appTemplate, String clientId, String clientSecret,
                           String initLoginUrl, AuthorizationType authorizationType,
                           Boolean enabled, Boolean configured) {
        //DTO 字段
        this.appCode = appCode;
        this.appName = appName;
        this.appTemplate = appTemplate;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.initLoginUrl = initLoginUrl;
        this.authorizationType = authorizationType;
        this.enabled = enabled;
        this.configured = configured;
        // OIDC 配置字段
        super.setAppId(config.getAppId());
        super.setClientAuthMethods(config.getClientAuthMethods());
        super.setAuthGrantTypes(config.getAuthGrantTypes());
        super.setResponseTypes(config.getResponseTypes());
        super.setRedirectUris(config.getRedirectUris());
        super.setPostLogoutRedirectUris(config.getPostLogoutRedirectUris());
        super.setGrantScopes(config.getGrantScopes());
        super.setRequireAuthConsent(config.getRequireAuthConsent());
        super.setRequireProofKey(config.getRequireProofKey());
        super.setTokenEndpointAuthSigningAlgorithm(config.getTokenEndpointAuthSigningAlgorithm());
        super.setRefreshTokenTimeToLive(config.getRefreshTokenTimeToLive());
        super.setAuthorizationCodeTimeToLive(config.getAuthorizationCodeTimeToLive());
        super.setDeviceCodeTimeToLive(config.getDeviceCodeTimeToLive());
        super.setIdTokenTimeToLive(config.getIdTokenTimeToLive());
        super.setIdTokenSignatureAlgorithm(config.getIdTokenSignatureAlgorithm());
        super.setAccessTokenFormat(config.getAccessTokenFormat());
        super.setAccessTokenTimeToLive(config.getAccessTokenTimeToLive());
        super.setReuseRefreshToken(config.getReuseRefreshToken());
        super.setCreateBy(config.getCreateBy());
        super.setCreateTime(config.getCreateTime());
        super.setUpdateBy(config.getUpdateBy());
        super.setUpdateTime(config.getUpdateTime());
        super.setRemark(config.getRemark());
    }
}
