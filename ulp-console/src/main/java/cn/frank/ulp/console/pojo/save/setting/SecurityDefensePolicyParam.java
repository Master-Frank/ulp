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
import jakarta.validation.constraints.NotBlank;

/**
 * 创建安全策略参数
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023-03-09
 */
@Data
@Schema(description = "创建安全策略入参数")
public class SecurityDefensePolicyParam implements Serializable {

    /**
     * 连续登录失败持续时间
     */
    @Parameter(description = "连续登录失败持续时间")
    private Integer loginFailureDuration;

    /**
     * 连续登录失败次数
     */
    @Parameter(description = "连续登录失败次数")
    private Integer loginFailureCount;

    /**
     * 自动解锁时间（分）
     */
    @Parameter(description = "自动解锁时间（分）")
    private Integer autoUnlockTime;

    /**
     * 内容安全策略
     */
    @Parameter(description = "内容安全策略")
    @NotBlank(message = "内容安全策略不能为空")
    private String  contentSecurityPolicy;
}
