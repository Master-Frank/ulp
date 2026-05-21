/*
 * ulp-portal - United Login Platform
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
package cn.frank.ulp.portal.pojo.query;

import java.io.Serializable;

import org.springdoc.core.annotations.ParameterObject;

import cn.frank.ulp.common.enums.app.AppGroupType;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 查询分组列表入参
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "查询分组列表入参")
@ParameterObject
public class GetAppGroupListQuery implements Serializable {

    /**
     * 分组名称
     */
    @Parameter(description = "分组名称")
    private String       name;

    /**
     * 应用名称
     */
    @Parameter(description = "应用名称")
    private String       appName;

    /**
     * 分组编码
     */
    @Parameter(description = "分组编码")
    private String       code;

    /**
     * 分组类型
     */
    @Parameter(description = "分组类型")
    private AppGroupType type;

}
