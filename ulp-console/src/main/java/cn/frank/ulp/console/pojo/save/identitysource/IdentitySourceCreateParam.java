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
package cn.frank.ulp.console.pojo.save.identitysource;

import java.io.Serial;
import java.io.Serializable;

import cn.frank.ulp.common.enums.identitysource.IdentitySourceProvider;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * 身份源保存入参
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "身份源保存入参")
public class IdentitySourceCreateParam implements Serializable {

    @Serial
    private static final long      serialVersionUID = 4217715726184249167L;
    /**
     * 名称
     */
    @Parameter(description = "名称")
    @NotEmpty(message = "名称不能为空")
    private String                 name;

    /**
     * 身份源提供商
     */
    @Parameter(description = "身份源提供商")
    @NotNull(message = "身份源提供商不能为空")
    private IdentitySourceProvider provider;

    /**
     * 备注
     */
    @Parameter(description = "备注")
    private String                 remark;

}
