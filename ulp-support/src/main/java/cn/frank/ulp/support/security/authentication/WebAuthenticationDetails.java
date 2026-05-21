/*
 * ulp-support - ULP support library (replaces the former eiam-support private jar).
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
package cn.frank.ulp.support.security.authentication;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import cn.frank.ulp.support.geo.GeoLocation;
import cn.frank.ulp.support.web.useragent.UserAgent;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Web认证详情类
 * 包含认证相关的详细信息
 */
public class WebAuthenticationDetails extends
                                      org.springframework.security.web.authentication.WebAuthenticationDetails {
    /**
    * 地理位置信息
    */
    private final GeoLocation      geoLocation;

    /**
    * 认证时间
    */
    private final LocalDateTime    authenticationTime;

    /**
    * 用户代理信息
    */
    private final UserAgent        userAgent;

    /**
    * 认证提供者
    */
    private AuthenticationProvider authenticationProvider;

    /**
    * 获取认证时间
    *
    * @return 认证时间
    */
    @JsonProperty("authenticationTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getAuthenticationTime() {
        return this.authenticationTime;
    }

    /**
    * 获取认证提供者
    *
    * @return 认证提供者
    */
    @JsonProperty("authenticationProvider")
    public AuthenticationProvider getAuthenticationProvider() {
        return this.authenticationProvider;
    }

    /**
    * 获取用户代理信息
    *
    * @return 用户代理信息
    */
    @JsonProperty("userAgent")
    public UserAgent getUserAgent() {
        return this.userAgent;
    }

    /**
    * 构造函数
    *
    * @param request HTTP请求
    * @param userAgent 用户代理
    * @param geoLocation 地理位置
    */
    public WebAuthenticationDetails(HttpServletRequest request, UserAgent userAgent,
                                    GeoLocation geoLocation) {
        super(request);
        this.userAgent = userAgent;
        this.geoLocation = geoLocation;
        this.authenticationTime = LocalDateTime.now();
    }

    /**
    * 获取地理位置信息
    *
    * @return 地理位置信息
    */
    @JsonProperty("geoLocation")
    public GeoLocation getGeoLocation() {
        return this.geoLocation;
    }

    /**
    * 构造函数
    *
    * @param remoteAddress 远程地址
    * @param sessionId 会话ID
    * @param userAgent 用户代理
    * @param geoLocation 地理位置
    * @param authenticationProvider 认证提供者
    * @param authenticationTime 认证时间
    */
    public WebAuthenticationDetails(String remoteAddress, String sessionId, UserAgent userAgent,
                                    GeoLocation geoLocation,
                                    AuthenticationProvider authenticationProvider,
                                    LocalDateTime authenticationTime) {
        super(remoteAddress, sessionId);
        this.userAgent = userAgent;
        this.geoLocation = geoLocation;
        this.authenticationProvider = authenticationProvider;
        this.authenticationTime = authenticationTime;
    }

    /**
    * 设置认证提供者
    *
    * @param authenticationProvider 认证提供者
    */
    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }
}
