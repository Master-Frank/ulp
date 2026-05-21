/*
 * ulp-portal - United Login Platform
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
package cn.frank.ulp.portal.pojo.result;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 三方登录提供商
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "三方登录提供商")
public class IdentityProviderResult implements Serializable {
    @Serial
    private static final long serialVersionUID = -6482651783349719888L;

    /**
     * CODE
     */
    @Schema(description = "CODE")
    private String            code;

    /**
     * name
     */
    @Schema(description = "名称")
    private String            name;

    /**
     * 提供商
     */
    @Schema(description = "提供商")
    private String            type;

    /**
     * 提供商类型
     */
    @Schema(description = "提供商类型")
    private String            category;

    /**
     * 认证地址
     */
    @Schema(description = "认证地址")
    private String            authorizationUri;
}
