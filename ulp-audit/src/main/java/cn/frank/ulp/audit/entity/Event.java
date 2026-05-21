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
package cn.frank.ulp.audit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import cn.frank.ulp.audit.enums.EventStatus;
import cn.frank.ulp.audit.event.type.EventType;

import lombok.Builder;
import lombok.Data;

/**
 * Event
 *
 * @author Frank Zhang
 */
@Data
@Builder
public class Event implements Serializable {

    @Serial
    private static final long  serialVersionUID = -1144169992714000310L;

    public static final String EVENT_TYPE       = "event.type.keyword";

    public static final String EVENT_TIME       = "event.time";

    public static final String EVENT_STATUS     = "event.status.keyword";

    /**
     * 审计事件类型
     */
    private EventType          type;

    /**
     * 参数
     */
    private String             param;

    /**
     * 事件内容
     */
    private String             content;

    /**
     * 事件结果
     */
    private String             result;

    /**
     * 事件时间
     */
    private LocalDateTime      time;

    /**
     * 事件状态
     */
    private EventStatus        status;

}
