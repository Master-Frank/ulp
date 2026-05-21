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
package cn.frank.ulp.console.pojo.result.analysis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 热点认证方式结果
 *
 * @author Frank Zhang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "热点认证方式响应")
public class AuthnHotProviderResult {

    /**
     * 认证方式
     */
    @Schema(description = "认证方式")
    private String name;

    /**
     * 使用次数
     */
    @Schema(description = "使用次数")
    private Long   count;
}
