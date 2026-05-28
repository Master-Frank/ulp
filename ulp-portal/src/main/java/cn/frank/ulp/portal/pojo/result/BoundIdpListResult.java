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

import lombok.Data;
import lombok.EqualsAndHashCode;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 账号绑定
 *
 * @author Frank Zhang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "账号绑定")
public class BoundIdpListResult extends IdentityProviderResult {

    /**
     * ID
     */
    @Schema(description = "ID")
    private String  id;

    /**
     * IDP id
     */
    @Schema(description = "IDP ID")
    private String  idpId;

    /**
     * 是否已绑定
     */
    @Schema(description = "是否已绑定")
    private Boolean bound;
}
