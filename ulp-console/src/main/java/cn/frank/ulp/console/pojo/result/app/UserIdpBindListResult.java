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
package cn.frank.ulp.console.pojo.result.app;

import java.time.LocalDateTime;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户身份提供商绑定列表查询结果
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "用户身份提供商绑定列表查询响应")
public class UserIdpBindListResult {

    /**
     * id
     */
    @Schema(description = "id")
    private String        id;

    /**
     * open id
     */
    @Schema(description = "open id")
    private String        openId;

    /**
     * 提供商名称
     */
    @Schema(description = "提供商名称")
    private String        idpName;

    /**
     * 提供商类型
     */
    @Schema(description = "提供商类型")
    private String        idpType;

    /**
     * 绑定时间
     */
    @Schema(description = "绑定时间")
    private LocalDateTime bindTime;
}
