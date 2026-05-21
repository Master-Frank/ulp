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

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 安全高级保存参数
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "安全高级保存入参")
public class SecurityBasicSaveParam implements Serializable {
    /**
     * 会话有效时间
     */
    @Parameter(description = "会话有效时间（秒）")
    private Integer sessionValidTime;

    /**
     * 验证码有效时间
     */
    @Parameter(description = "验证码有效时间（秒）")
    private Integer verifyCodeValidTime;

    /**
     * 记住我有效时间
     */
    @Schema(description = "记住我有效时间（秒）")
    private Integer rememberMeValidTime;

    /**
     * 用户并发数
     */
    @Parameter(description = "用户并发数")
    private Integer sessionMaximum;
}
