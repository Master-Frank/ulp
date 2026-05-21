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
package cn.frank.ulp.console.pojo.result.app;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 获取分组返回
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "获取分组返回响应")
public class AppGroupGetResult implements Serializable {
    /**
     * ID
     */
    @Parameter(description = "ID")
    private String        id;
    /**
     * 分组名称
     */
    @Parameter(description = "分组名称")
    private String        name;

    /**
     * 分组编码
     */
    @Parameter(description = "分组编码")
    private String        code;

    /**
     * 创建时间
     */
    @Parameter(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 备注
     */
    @Parameter(description = "备注")
    private String        remark;
}
