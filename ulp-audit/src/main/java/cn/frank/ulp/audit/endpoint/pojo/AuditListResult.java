/*
 * ulp-audit - United Login Platform
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
package cn.frank.ulp.audit.endpoint.pojo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.frank.ulp.audit.entity.GeoLocation;
import cn.frank.ulp.audit.entity.Target;
import cn.frank.ulp.audit.entity.UserAgent;
import cn.frank.ulp.audit.enums.EventStatus;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import static cn.frank.ulp.support.constant.UlpConstants.DEFAULT_DATE_TIME_FORMATTER_PATTERN;

/**
 * 审计日志列表结果
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "审计日志列表响应")
public class AuditListResult implements Serializable {
    /**
     * ID
     */
    @Schema(description = "ID")
    private String        id;

    /**
     * 用户
     */
    @Schema(description = "用户名")
    private String        username;

    /**
     * 用户 ID
     */
    @Schema(description = "用户ID")
    private String        userId;

    /**
     * 用户类型
     */
    @Schema(description = "用户类型")
    private String        userType;

    /**
     * 用户代理
     */
    @Schema(description = "用户代理")
    private UserAgent     userAgent;

    /**
     * 地理IP
     */
    @Schema(description = "地理位置")
    private GeoLocation   geoLocation;

    /**
     * 事件类型
     */
    @Schema(description = "事件类型")
    private String        eventType;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = DEFAULT_DATE_TIME_FORMATTER_PATTERN)
    @Schema(description = "事件时间")
    private LocalDateTime eventTime;

    /**
     * 事件状态
     */
    @Schema(description = "事件状态")
    private EventStatus   eventStatus;

    /**
     * 目标
     */
    @Schema(description = "目标")
    private List<Target>  targets;
}
