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
package cn.frank.ulp.console.pojo.save.app;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * AppAccountCreateParam 应用账户新增入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/5/24 22:13
 */
@Data
@Schema(description = "应用账户新增入参")
public class AppAccountCreateParam {

    /**
     * 应用ID
     */
    @Schema(description = "应用ID")
    @NotNull(message = "应用ID不能为空")
    private String  appId;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @NotNull(message = "用户ID不能为空")
    private String  userId;

    /**
     * 账户名称
     */
    @Schema(description = "账户名称")
    @NotBlank(message = "账户名称不能为空")
    private String  account;

    /**
     * 账户密码
     */
    @Schema(description = "账户密码")
    private String  password;

    /**
     * 是否默认
     */
    @Schema(description = "是否默认")
    private Boolean defaulted;
}
