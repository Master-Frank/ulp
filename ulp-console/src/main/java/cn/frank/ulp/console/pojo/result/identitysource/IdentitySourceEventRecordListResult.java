/*
 * ulp-console - United Login Platform
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
package cn.frank.ulp.console.pojo.result.identitysource;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.frank.ulp.common.enums.SyncStatus;
import cn.frank.ulp.common.enums.identitysource.IdentitySourceActionType;
import cn.frank.ulp.common.enums.identitysource.IdentitySourceObjectType;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import static cn.frank.ulp.support.constant.EiamConstants.DEFAULT_DATE_TIME_FORMATTER_PATTERN;

/**
 * 身份源事件记录列表
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/2/25 23:05
 */
@Data
@Schema(description = "身份源事件记录列表")
public class IdentitySourceEventRecordListResult implements Serializable {
    /**
     * 唯一标识
     */
    @Parameter(description = "ID")
    private String                   id;

    /**
     * 动作类型
     */
    @Parameter(description = "动作类型")
    private IdentitySourceActionType actionType;

    /**
     * 对象ID
     */
    @Parameter(description = "对象ID")
    private String                   objectId;

    /**
     * 对象名称
     */
    @Parameter(description = "对象名称")
    private String                   objectName;

    /**
     * 对象类型
     */
    @Parameter(description = "对象类型")
    private IdentitySourceObjectType objectType;

    /**
     * 事件时间
     */
    @Parameter(description = "事件时间")
    @JsonFormat(pattern = DEFAULT_DATE_TIME_FORMATTER_PATTERN)
    private LocalDateTime            eventTime;

    /**
     * 事件状态
     */
    @Parameter(description = "事件状态")
    private SyncStatus               status;
}
