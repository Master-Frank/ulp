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
package cn.frank.ulp.console.pojo.update.app;

import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 分组修改入参
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "分组修改入参")
public class AppGroupUpdateParam implements Serializable {

    /**
     * id
     */
    @Schema(description = "分组id")
    @NotNull(message = "ID不能为空")
    private String id;

    /**
     * 分组名称
     */
    @Schema(description = "分组名称")
    private String name;

    /**
     * 分组排序
     */
    @Schema(description = "分组编码")
    private String code;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}
