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
package cn.frank.ulp.console.pojo.save.setting;

import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

/**
 * 角色创建参数
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "创建角色入参")
public class RoleCreateParam implements Serializable {
    /**
     * 名称
     */
    @Schema(description = "名称")
    @NotEmpty(message = "名称不能为空")
    private String name;

    /**
     * 编码
     */
    @NotEmpty(message = "编码不能为空")
    @Schema(description = "编码")
    private String code;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

}
