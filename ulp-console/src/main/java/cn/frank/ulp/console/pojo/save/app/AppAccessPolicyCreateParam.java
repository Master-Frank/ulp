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

import java.util.List;

import cn.frank.ulp.common.enums.app.AppPolicySubjectType;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 应用访问授权策略添加参数
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/9/27 21:29
 */
@Data
@Schema(description = "应用访问授权策略添加参数")
public class AppAccessPolicyCreateParam {

    /**
     * 应用ID
     */
    @Schema(description = "应用ID")
    @NotNull(message = "应用ID不能为空")
    private String               appId;

    /**
     * 主体ID（用户、分组、组织机构）
     */
    @Schema(description = "主体")
    @NotNull(message = "主体不能为空")
    private List<String>         subjectIds;

    /**
     * 主体类型（用户、分组、组织机构）
     */
    @Schema(description = "主体类型")
    @NotNull(message = "主体类型不能为空")
    private AppPolicySubjectType subjectType;
}
