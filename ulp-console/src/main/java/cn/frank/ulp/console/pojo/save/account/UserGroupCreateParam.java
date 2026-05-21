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
package cn.frank.ulp.console.pojo.save.account;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 用户创建请求入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/11 23:16
 */
@Data
@Schema(description = "创建用户分组入参")
public class UserGroupCreateParam implements Serializable {
    @Serial
    private static final long serialVersionUID = -6044649488381303849L;
    /**
     * 用户组名称
     */
    @Schema(description = "用户组名称")
    @NotBlank(message = "用户组名称不能为空")
    private String            name;

    /**
     * 用户组编码
     */
    @Schema(description = "用户组编码")
    @NotBlank(message = "用户组编码不能为空")
    private String            code;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String            remark;
}
