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
import java.util.List;

import cn.frank.ulp.common.enums.app.AuthorizationType;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 应用修改入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/7/18 23:26
 */
@Data
@Schema(description = "应用修改入参")
public class AppUpdateParam implements Serializable {

    /**
     * id
     */
    @Schema(description = "应用id")
    @NotNull(message = "ID不能为空")
    private String            id;

    /**
     * 应用名称
     */
    @Schema(description = "应用名称")
    private String            name;

    /**
     * 应用图标
     */
    @Schema(description = "应用图标")
    private String            icon;

    /**
     * SSO 授权类型
     */
    @Schema(description = "授权类型")
    private AuthorizationType authorizationType;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String            remark;

    /**
     * 应用分组ID
     */
    @Schema(description = "应用分组ID")
    private List<String>      groupIds;
}
