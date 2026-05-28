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
package cn.frank.ulp.console.pojo.result.setting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.frank.ulp.support.enums.BaseEnum;

import lombok.Builder;
import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 获取管理员权限策略
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "获取管理员权限点列表响应")
public class AdministratorPermissionActionListResult implements Serializable {

    /**
     * 模块编码
     */
    @Parameter(description = "模块编码")
    private String         code;

    /**
     * 模块名称
     */
    @Parameter(description = "模块名称")
    private String         name;

    /**
     * 模块功能
     */
    @Parameter(description = "功能列表")
    private List<Function> functions;

    public AdministratorPermissionActionListResult(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Data
    @Schema(description = "功能项")
    public static class Function {
        /**
         * 功能编码
         */
        @Parameter(description = "功能编码")
        private String       code;

        /**
         * 功能名称
         */
        @Parameter(description = "功能名称")
        private String       name;

        /**
         * 操作项
         */
        @Parameter(description = "操作项")
        private List<Action> actions = new ArrayList<>();

        public Function(String code, String name, Object[] actions) {
            this.code = code;
            this.name = name;
            for (Object object : actions) {
                BaseEnum baseEnum = (BaseEnum) object;
                this.actions.add(new Action(baseEnum.getCode(), baseEnum.getDesc()));
            }
        }
    }

    @Data
    @Builder
    public static class Action {
        /**
         * 功能编码
         */
        @Parameter(description = "功能编码")
        private String code;

        /**
         * 功能名称
         */
        @Parameter(description = "功能名称")
        private String name;

        public Action(String code, String name) {
            this.code = code;
            this.name = name;
        }
    }
}
