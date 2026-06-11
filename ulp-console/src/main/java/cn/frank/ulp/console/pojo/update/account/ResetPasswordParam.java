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
package cn.frank.ulp.console.pojo.update.account;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import tools.jackson.databind.annotation.JsonDeserialize;

import cn.frank.ulp.common.enums.MessageNoticeChannel;
import cn.frank.ulp.support.enums.ListEnumDeserializer;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 重置密码入参
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "重置密码入参")
public class ResetPasswordParam implements Serializable {
    @Serial
    private static final long   serialVersionUID = -6616249172773611157L;
    /**
     * ID
     */
    @Schema(description = "用户ID")
    @NotBlank(message = "用户ID不能为空")
    private String              id;

    /**
     * 密码
     */
    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空")
    private String              password;

    /**
     * 重置密码配置
     */
    @Schema(description = "重置密码配置")
    private PasswordResetConfig passwordResetConfig;

    @Data
    public static class PasswordResetConfig implements Serializable {
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
