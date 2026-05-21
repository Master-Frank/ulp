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
package cn.frank.ulp.openapi.pojo.save;

import java.io.Serial;
import java.io.Serializable;

import cn.frank.ulp.common.enums.account.OrganizationType;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * 创建组织架构入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/11 23:16
 */
@Data
@Schema(description = "创建组织架构入参")
public class OrganizationCreateParam implements Serializable {
    @Serial
    private static final long serialVersionUID = 3118058164024117164L;

    /**
     * code
     */
    @Schema(description = "编码")
    private String            code;

    /**
     * 上级部门
     */
    @NotEmpty(message = "请选择上级组织")
    @Schema(description = "上级组织")
    private String            parentId;

    /**
     * 名称
     */
    @Schema(description = "架构名称")
    @NotBlank(message = "名称不能为空")
    private String            name;

    /**
     * 类型
     */
    @Schema(description = "架构类型")
    @NotNull(message = "类型不能为空")
    private OrganizationType  type;

    /**
     * 外部ID
     */
    @Schema(description = "外部ID")
    private String            externalId;

    /**
     * 区域
     */
    @Schema(description = "所在区域")
    private String            area;

    /**
     * 描述
     */
    @Schema(description = "架构描述")
    private String            desc;

    /**
     * 排序
     */
    @Schema(description = "架构排序")
    private String            order;

    /**
     * 是否启用
     */
    @Schema(description = "是否启用")
    private Boolean           enabled;
}
