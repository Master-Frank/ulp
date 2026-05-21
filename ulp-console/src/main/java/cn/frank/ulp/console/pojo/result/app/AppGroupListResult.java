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

import cn.frank.ulp.common.enums.app.AppGroupType;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 分组列表返回
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/8/31 10:29
 */
@Data
@Schema(description = "分组列表返回")
public class AppGroupListResult implements Serializable {

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
     * 应用数量
     */
    @Parameter(description = "应用数量")
    private Integer       appCount;

    /**
     * 分组类型
     */
    @Parameter(description = "分组类型")
    private AppGroupType  type;

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
