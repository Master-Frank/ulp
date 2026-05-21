/*
 * ulp-openapi - United Login Platform
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
package cn.frank.ulp.openapi.pojo.result;

import java.io.Serial;
import java.io.Serializable;

import cn.frank.ulp.common.enums.account.OrganizationType;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 获取组织
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "获取组织")
public class OrganizationResult implements Serializable {
    @Serial
    private static final long serialVersionUID = -150631305460653395L;
    /**
     * 主键ID
     */
    @Schema(description = "ID")
    private String            id;
    /**
     * key
     */
    @Schema(description = "名称")
    private String            name;

    /**
     * 显示路径
     */
    @Schema(description = "显示路径")
    private String            displayPath;
    /**
     * 编码
     */
    @Schema(description = "编码")
    private String            code;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private String            order;

    /**
     * 组织机构类型
     */
    @Schema(description = "机构类型")
    private OrganizationType  type;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String            remark;
}
