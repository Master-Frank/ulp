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
package cn.frank.ulp.console.pojo.query.authn;

import java.io.Serial;
import java.io.Serializable;

import org.springdoc.core.annotations.ParameterObject;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2022/3/21 21:52
 */
@Data
@Schema(description = "查询认证源列表入参")
@ParameterObject
public class IdentityProviderListQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1191998425971892318L;

    /**
     * 认证源ID
     */
    @Parameter(description = "认证源名称")
    private String            name;

    /**
     * 认证源类型
     */
    @Parameter(description = "认证源分类")
    @NotNull(message = "认证源分类不能为空")
    private String            category;

}
