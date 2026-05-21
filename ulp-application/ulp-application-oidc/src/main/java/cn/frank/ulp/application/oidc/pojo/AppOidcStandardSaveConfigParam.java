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
package cn.frank.ulp.application.oidc.pojo;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.URL;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2022/7/10 01:45
 */
@Data
@Schema(description = "保存 OIDC 应用配置参数")
public class AppOidcStandardSaveConfigParam implements Serializable {

    @Serial
    private static final long                                                                serialVersionUID = 7257798528680745281L;

    /**
     * 授权类型
     */
    @NotNull(message = "授权类型不能为空")
    @Schema(description = "授权类型")
    private List<@NotBlank(message = "授权类型不能为空") String>                                     authGrantTypes;

    /**
     * 登录重定向URI
     */
    @NotNull(message = "登录重定向URI不能为空")
    @Schema(description = "登录重定向URI")
    private List<@NotBlank(message = "登录重定向URI不能为空") @URL(message = "登录重定向URI格式不正确") String> redirectUris;

    /**
     * 登出重定向URI
     */
    @Schema(description = "登出重定向URI")
    private List<@NotBlank(message = "登出重定向URI不能为空") @URL(message = "登出重定向URI格式不正确") String> postLogoutRedirectUris;

    /**
     * 登录发起登录URL
     */
    @Schema(description = "登录发起登录URL")
    private String                                                                           initLoginUrl;

    /**
     * 授予范围
     */
    @NotNull(message = "授予范围不能为空")
    @Schema(description = "授予范围")
    private List<String>                                                                     grantScopes;

    /**
     * 客户端身份验证方法
     */
    @Schema(description = "客户端身份验证方法")
    private List<String>                                                                     clientAuthMethods;

    /**
     * 是否需要PKCE
     */
    @Schema(description = "是否需要PKCE")
    private Boolean                                                                          requireProofKey  = false;

    /**
     * Access Token 生存时间
     */
    @NotNull(message = "Access Token 生存时间不能为空")
    @Schema(description = "Access Token 生存时间")
    private Long                                                                             accessTokenTimeToLive;

    /**
     * Access Token 格式
     */
    @Schema(description = "Access Token 格式")
    private String                                                                           accessTokenFormat;

    /**
     * Refresh Token 生存时间
     */
    @NotNull(message = "Refresh Token 生存时间不能为空")
    @Schema(description = "Refresh Token 生存时间")
    private Long                                                                             refreshTokenTimeToLive;

    /**
     * 授权码模式授权码生存时间
     */
    @NotNull(message = "授权码模式授权码生存时间不能为空")
    @Schema(description = "授权码模式授权码生存时间")
    private Long                                                                             authorizationCodeTimeToLive;

    /**
     * 设备模式授权码生存时间
     */
    @Schema(description = "设备模式授权码生存时间")
    private Long                                                                             deviceCodeTimeToLive;

    /**
     * Id Token 生存时间
     */
    @NotNull(message = "Id Token 生存时间不能为空")
    @Schema(description = "Id Token 生存时间")
    private Long                                                                             idTokenTimeToLive;

    /**
     * ID Token签名算法
     */
    @NotBlank(message = "ID Token签名算法不能为空")
    @Schema(description = "ID Token签名算法")
    private String                                                                           idTokenSignatureAlgorithm;

    /**
     * Token签名算法
     */
    @Schema(description = "令牌端点身份验证签名算法")
    private String                                                                           tokenEndpointAuthSigningAlgorithm;

    /**
     * 重用刷新令牌
     */
    @Schema(description = "重用刷新令牌")
    private Boolean                                                                          reuseRefreshToken;

    /**
     * 需要授权同意
     */
    @Schema(description = "需要授权同意")
    private Boolean                                                                          requireAuthConsent;

}
