/*
 * ulp-core - United Login Platform
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
package cn.frank.ulp.core.security.session;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import cn.frank.ulp.support.constant.EiamConstants;
import cn.frank.ulp.support.geo.GeoLocation;
import cn.frank.ulp.support.security.userdetails.UserType;
import cn.frank.ulp.support.web.useragent.UserAgent;

import lombok.Data;

/**
 *SessionDetails
 *
 * @author Frank Zhang
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class Session implements Serializable {

    @Serial
    private static final long serialVersionUID = 8850489178248613501L;

    /**
     * 会话ID
     */
    private String            sessionId;

    /**
     * 用户ID
     */
    private final String      id;

    /**
     * 用户名
     */
    private final String      username;

    /**
     * 地址位置相关
     */
    private GeoLocation       geoLocation;

    /**
     * userAgent
     */
    private UserAgent         userAgent;

    /**
     * 用户类型
     */
    private UserType          userType;

    /**
     * 认证类型
     */
    private String            authenticationProvider;

    /**
     * 登录时间
     */
    @JsonFormat(pattern = EiamConstants.DEFAULT_DATE_TIME_FORMATTER_PATTERN)
    private LocalDateTime     authenticationTime;

    /**
     * 最后请求时间
     */
    @JsonFormat(pattern = EiamConstants.DEFAULT_DATE_TIME_FORMATTER_PATTERN)
    private LocalDateTime     lastRequestTime;

}
