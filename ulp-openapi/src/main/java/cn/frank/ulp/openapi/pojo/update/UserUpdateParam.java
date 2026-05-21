/*
 * ulp-openapi - United Login Platform
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
package cn.frank.ulp.openapi.pojo.update;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

import cn.frank.ulp.common.enums.UserStatus;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 编辑用户入参
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "修改用户入参")
public class UserUpdateParam implements Serializable {
    @Serial
    private static final long serialVersionUID = -6616249172773611157L;
    /**
     * ID
     */
    @Schema(description = "用户ID")
    @NotBlank(message = "用户ID不能为空")
    private String            id;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String            email;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String            phone;

    /**
     * 头像URL
     */
    @Schema(description = "头像URL")
    private String            avatar;

    /**
     * 姓名
     */
    @Schema(description = "姓名")
    @NotBlank(message = "姓名不能为空")
    private String            fullName;

    /**
     * 昵称
     */
    @Schema(description = "昵称")
    private String            nickName;

    /**
     * 身份证号
     */
    @Schema(description = "身份证号")
    private String            idCard;

    /**
     * 地址
     */
    @Schema(description = "地址")
    private String            address;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String            remark;

    /**
     * 过期日期
     */
    @Schema(description = "过期日期")
    private LocalDate         expireDate;

    /**
     * status
     */
    @Schema(description = "状态")
    private UserStatus        status;
}
