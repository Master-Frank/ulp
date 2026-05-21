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
package cn.frank.ulp.portal.pojo.request;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/8/8 21:15
 */
@Data
@Schema(description = "准备更改电子邮件入参")
public class PrepareChangeEmailRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 5681761697876754485L;

    /**
     * 邮箱
     */
    @NotEmpty(message = "邮箱不能为空")
    @Parameter(description = "邮箱")
    private String            email;

    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空")
    @Parameter(description = "密码")
    private String            password;

}
