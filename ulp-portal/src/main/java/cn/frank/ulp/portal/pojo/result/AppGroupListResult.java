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
package cn.frank.ulp.portal.pojo.result;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 获取应用列表
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "获取分组应用列表")
public class AppGroupListResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 1263170640092199401L;

    /**
     * 应用分组ID
     */
    @Schema(description = "应用分组ID")
    private String            id;

    /**
     * 应用分组名称
     */
    @Schema(description = "应用分组名称")
    private String            name;

    /**
     * APP数量
     */
    @Schema(description = "APP数量")
    private Integer           appCount;

}
