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
package cn.frank.ulp.console.pojo.result.authn;

import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 社交认证源平台列表，带有元素字段，避免前端重复画页面，基本都是input
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/26 21:57
 */
@Data
@Schema(description = "社交认证源平台列表")
public class IdentityProviderListResult implements Serializable {

    /**
     * ID
     */
    @Parameter(description = "ID")
    private String  id;

    /**
     * name
     */
    @Parameter(description = "名称")
    private String  name;

    /**
     * 提供商
     */
    @Parameter(description = "提供商")
    private String  type;

    /**
     * 是否启用
     */
    @Parameter(description = "是否启用")
    private Boolean enabled;

    /**
     * 描述
     */
    @Parameter(description = "描述")
    private String  desc;

    /**
     * 备注
     */
    @Parameter(description = "备注")
    private String  remark;
}
