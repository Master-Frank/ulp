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
package cn.frank.ulp.audit.endpoint.pojo;

import java.util.Set;

import org.springdoc.core.annotations.ParameterObject;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 审计字典结果
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/27 22:35
 */
@Data
@Schema(description = "字典响应")
@ParameterObject
public class DictResult {

    @Parameter(description = "分组名")
    private String         name;
    @Parameter(description = "分组编码")
    private String         code;
    @Parameter(description = "类型")
    private Set<AuditType> types;

    @Data
    @Schema(description = "审计类型")
    public static class AuditType {
        /**
         * 名称
         */
        @Parameter(description = "名称")
        private String name;

        /**
         * CODE
         */
        @Parameter(description = "CODE")
        private String code;
    }

}
