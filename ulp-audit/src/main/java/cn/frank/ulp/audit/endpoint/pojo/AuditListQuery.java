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

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.format.annotation.DateTimeFormat;

import cn.frank.ulp.audit.enums.EventStatus;
import cn.frank.ulp.audit.event.type.EventType;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import static cn.frank.ulp.support.constant.EiamConstants.DEFAULT_DATE_TIME_FORMATTER_PATTERN;

/**
 * 查询审计日志列表入参
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "查询审计日志列表入参")
@ParameterObject
public class AuditListQuery implements Serializable {

    /**
     * 用户名
     */
    @Parameter(description = "用户名")
    private String          username;

    /**
     * 审计类型
     */
    @Parameter(description = "审计类型")
    private List<EventType> eventType;

    /**
     * 用户类型
     */
    @NotNull(message = "用户类型不能为空")
    @Parameter(description = "用户类型")
    private String          userType;

    /**
     * 事件状态
     */
    @Parameter(description = "事件状态")
    private EventStatus     eventStatus;

    /**
     * 事件开始时间
     */
    @Parameter(description = "事件开始时间")
    @DateTimeFormat(pattern = DEFAULT_DATE_TIME_FORMATTER_PATTERN)
    private LocalDateTime   startEventTime;

    /**
     * 事件结束时间
     */
    @Parameter(description = "事件结束时间")
    @DateTimeFormat(pattern = DEFAULT_DATE_TIME_FORMATTER_PATTERN)
    private LocalDateTime   endEventTime;
}
