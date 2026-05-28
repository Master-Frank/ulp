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
package cn.frank.ulp.console.pojo.update.authn;

import java.io.Serial;
import java.io.Serializable;

import com.alibaba.fastjson2.JSONObject;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 认证源修改参数入参
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "认证源修改参数")
public class IdpUpdateParam implements Serializable {
    @Serial
    private static final long serialVersionUID = -1440230086940289961L;
    /**
     * ID
     */
    @NotBlank(message = "ID不能为空")
    @Schema(description = "ID")
    private String            id;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    @Schema(description = "名称")
    private String            name;

    /**
     * 平台
     */
    @NotNull(message = "平台不能为空")
    @Schema(description = "平台")
    private String            type;

    /**
     * 配置
     */
    @NotNull(message = "配置JSON不能为空")
    @Schema(description = "配置JSON")
    private JSONObject        config;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String            remark;

    /**
     * 是否显示
     */
    @Schema(description = "是否显示")
    private Boolean           displayed;
}
