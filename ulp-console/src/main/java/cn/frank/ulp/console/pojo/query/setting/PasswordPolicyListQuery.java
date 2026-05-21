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
package cn.frank.ulp.console.pojo.query.setting;

import org.springdoc.core.annotations.ParameterObject;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 密码策略
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/5/5 22:32
 */
@Data
@Schema(description = "登录策略列表查询")
@ParameterObject
public class PasswordPolicyListQuery {

    /**
     * 名称
     */
    @Parameter(description = "名称")
    private String  name;

    /**
     * 是否启用
     */
    @Parameter(description = "是否启用")
    private Boolean enabled;
}
