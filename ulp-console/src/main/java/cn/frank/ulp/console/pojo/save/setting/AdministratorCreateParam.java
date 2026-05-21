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
 * 管理员创建参数
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "创建管理员入参")
public class AdministratorCreateParam implements Serializable {
    /**
     * 用户名
     */
    @Schema(description = "用户名")
    @NotEmpty(message = "用户名不能为空")
    private String username;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phone;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}
