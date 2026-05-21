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
package cn.frank.ulp.console.pojo.result.identitysource;

import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 身份源列表
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "身份源列表")
public class IdentitySourceListResult implements Serializable {
    /**
     * 唯一标识
     */
    @Parameter(description = "ID")
    private String  id;

    /**
     * 名称
     */
    @Parameter(description = "名称")
    private String  name;

    /**
     * ICON
     */
    @Parameter(description = "图标")
    private String  icon;

    /**
     * 描述
     */
    @Parameter(description = "描述")
    private String  desc;

    /**
     * 提供商
     */
    @Parameter(description = "提供商")
    private String  provider;

    /**
     * 备注
     */
    @Parameter(description = "备注")
    private String  remark;

    /**
     * 是否启用
     */
    @Parameter(description = "是否启用")
    private Boolean enabled;
}
