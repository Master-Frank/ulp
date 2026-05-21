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
package cn.frank.ulp.openapi.pojo.update;

import java.io.Serial;
import java.io.Serializable;

import cn.frank.ulp.common.enums.account.OrganizationType;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 编辑组织架构入参
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "修改组织机构入参")
public class OrganizationUpdateParam implements Serializable {
    @Serial
    private static final long serialVersionUID = 4570955457331971748L;

    /**
     * id
     */
    @Schema(description = "组织ID")
    @NotBlank(message = "ID不能为空")
    private String            id;

    /**
     * 名称
     */
    @Schema(description = "组织名称")
    @NotBlank(message = "组织名称不能为空")
    private String            name;

    /**
     * 类型
     */
    @Schema(description = "组织类型")
    @NotNull(message = "组织类型不能为空")
    private OrganizationType  type;

    /**
     * 描述
     */
    @Schema(description = "组织描述")
    private String            desc;

    /**
     * 排序
     */
    @Schema(description = "组织排序")
    private String            order;

    /**
     * 是否启用
     */
    @Schema(description = "是否启用")
    private Boolean           enabled;

}
