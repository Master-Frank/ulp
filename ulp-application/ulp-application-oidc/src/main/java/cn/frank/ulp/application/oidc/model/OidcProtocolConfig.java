/*
 * ulp-application-oidc - United Login Platform
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
package cn.frank.ulp.application.oidc.model;

import java.io.Serial;
import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import com.nimbusds.jose.jwk.JWK;

import cn.frank.ulp.application.AbstractProtocolConfig;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * Oidc 协议配置
 *
 * @author Frank Zhang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class OidcProtocolConfig extends AbstractProtocolConfig {

    @Serial
    private static final long serialVersionUID = -3671812647788723766L;

    /**
     * 客户端认证方式
     */
    private Set<String>       clientAuthMethods;

    /**
     * 授权类型
     */
    private Set<String>       authGrantTypes;

    /**
     * 响应类型
     */
    private Set<String>       responseTypes;

    /**
     * 重定向URIs
     */
    private Set<String>       redirectUris;

    /**
     * 登出重定向URIs
     */
    private Set<String>       postLogoutRedirectUris;

    /**
     * scopes
     */
    private Set<String>       grantScopes;

    /**
     * 是否需要授权同意
     */
    private Boolean           requireAuthConsent;

    /**
     * 需要PKCE
     */
    private Boolean           requireProofKey;

    /**
     * 令牌 Endpoint 身份验证签名算法
     */
    private String            tokenEndpointAuthSigningAlgorithm;

    /**
     * 授权码模式授权码生存时间
     */
    private Duration          authorizationCodeTimeToLive;

    /**
     * 设备模式授权码生存时间
     */
    private Duration          deviceCodeTimeToLive;

    /**
     * 刷新 Token生存时间（分钟）
     */
    private Duration          refreshTokenTimeToLive;

    /**
     * ID Token生存时间（分钟）
     */
    private Duration          idTokenTimeToLive;

    /**
     * 访问 Token生存时间（分钟）
     */
    private Duration          accessTokenTimeToLive;

    /**
     * Id Token 签名算法
     */
    private String            idTokenSignatureAlgorithm;

    /**
     * Access Token 格式
     */
    private String            accessTokenFormat;

    /**
     * 是否重用刷新令牌
     */
    private Boolean           reuseRefreshToken;

    /**
     * jwks
     */
    private List<JWK>         jwks;

    /**
     * 是否配置
     */
    private Boolean           configured;

    public Set<String> getRedirectUris() {
        return CollectionUtils.isEmpty(redirectUris) ? Set.of() : redirectUris;
    }

    public Set<String> getPostLogoutRedirectUris() {
        return CollectionUtils.isEmpty(postLogoutRedirectUris) ? Set.of() : postLogoutRedirectUris;
    }

    public Set<String> getGrantScopes() {
        return CollectionUtils.isEmpty(grantScopes) ? Set.of() : grantScopes;
    }

    public Set<String> getClientAuthMethods() {
        return CollectionUtils.isEmpty(clientAuthMethods) ? Set.of() : clientAuthMethods;
    }

    public Set<String> getResponseTypes() {
        return CollectionUtils.isEmpty(responseTypes) ? Set.of() : responseTypes;
    }

    public List<JWK> getJwks() {
        return CollectionUtils.isEmpty(jwks) ? List.of() : jwks;
    }
}
