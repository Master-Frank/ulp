/*
 * ulp-audit - United Login Platform
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
package cn.frank.ulp.audit.repository.result;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 登录区域结果
 *
 * @author Frank Zhang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登录区域响应")
public class AuthnZoneResult implements Serializable {

    /**
     * 省份code
     */
    @Schema(description = "省份code")
    private String name;

    /**
     * 登录次数
     */
    @Schema(description = "登录次数")
    private Long   count;
}
