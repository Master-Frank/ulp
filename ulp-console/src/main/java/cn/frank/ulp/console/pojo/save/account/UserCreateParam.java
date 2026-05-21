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
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import cn.frank.ulp.common.enums.MessageNoticeChannel;
import cn.frank.ulp.support.enums.ListEnumDeserializer;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * 用户创建请求入参
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "创建用户入参")
public class UserCreateParam implements Serializable {
    @Serial
    private static final long        serialVersionUID = -6044649488381303849L;
    /**
     * 组织机构
     */
    @Schema(description = "组织机构")
    @NotBlank(message = "组织机构不能为空")
    private String                   organizationId;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String                   username;

    /**
     * 密码
     */
    @Schema(description = "登录密码")
    @NotBlank(message = "登录密码不能为空")
    private String                   password;

    /**
     * 邮箱
     */
    @Email
    @Schema(description = "邮箱")
    private String                   email;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String                   phone;

    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    @Schema(description = "姓名")
    private String                   fullName;

    /**
     * 昵称
     */
    @Schema(description = "昵称")
    private String                   nickName;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String                   remark;

    /**
     * 过期日期
     */
    @Schema(description = "过期日期")
    private LocalDate                expireDate;

    /**
     * 密码初始化配置
     */
    @Schema(description = "密码初始化配置")
    private PasswordInitializeConfig passwordInitializeConfig;

    @Data
    public static class PasswordInitializeConfig implements Serializable {
        /**
         * 启用通知
         */
        @Schema(description = "启用通知")
        private Boolean                    enableNotice;

        /**
         * 消息类型
         */
        @Schema(description = "消息类型")
        @JsonDeserialize(using = ListEnumDeserializer.class)
        private List<MessageNoticeChannel> noticeChannels;
    }
}
