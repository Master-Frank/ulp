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
package cn.frank.ulp.console.pojo.query.identity;

import java.io.Serial;
import java.io.Serializable;

import org.springdoc.core.annotations.ParameterObject;

import cn.frank.ulp.common.enums.SyncStatus;
import cn.frank.ulp.common.enums.identitysource.IdentitySourceActionType;
import cn.frank.ulp.common.enums.identitysource.IdentitySourceObjectType;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 查询身份源事件记录列表入参
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "查询身份源事件记录列表入参")
@ParameterObject
public class IdentitySourceEventRecordListQuery implements Serializable {
    @Serial
    private static final long        serialVersionUID = -7110595216804896858L;

    /**
     * 身份源ID
     */
    @NotBlank(message = "身份源ID不能为空")
    @Parameter(description = "身份源ID")
    private String                   identitySourceId;

    /**
     * 动作类型
     */
    @Parameter(description = "动作类型")
    private IdentitySourceActionType actionType;

    /**
     * 对象类型
     */
    @Parameter(description = "对象类型")
    private IdentitySourceObjectType objectType;

    /**
     * 事件状态
     */
    @Parameter(description = "事件状态")
    private SyncStatus               status;
}
