/*
 * ulp-openapi - United Login Platform
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
package cn.frank.ulp.openapi.pojo.result;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 组织用户关系
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "组织用户关系")
public class OrganizationMember implements Serializable {

    @Serial
    private static final long serialVersionUID = 5599721546299698344L;

    /**
     * 主键ID
     */
    @Schema(description = "ID")
    private String            id;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private String            userId;

    /**
     * 组织ID
     */
    @Schema(description = "组织ID")
    private String            orgId;
}
