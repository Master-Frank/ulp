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
package cn.frank.ulp.console.pojo.result.identitysource;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import cn.frank.ulp.common.enums.identitysource.IdentitySourceProvider;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import static cn.frank.ulp.support.constant.EiamConstants.DEFAULT_DATE_TIME_FORMATTER_PATTERN;

/**
 * 身份源源详情
 *
 * @author TopIAM
 */
@Data
@Schema(description = "身份源源详情")
public class IdentitySourceGetResult implements Serializable {
    @Serial
    private static final long      serialVersionUID = -1440230086940289961L;
    /**
     * ID
     */
    @Parameter(description = "ID")
    private String                 id;

    /**
     * 名称
     */
    @Parameter(description = "名称")
    private String                 name;

    /**
     * 编码
     */
    @Parameter(description = "编码")
    private String                 code;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = DEFAULT_DATE_TIME_FORMATTER_PATTERN)
    @Parameter(description = "创建时间")
    private LocalDateTime          createTime;

    /**
     * 平台
     */
    @Parameter(description = "平台")
    private IdentitySourceProvider provider;

    /**
     * 是否启用
     */
    @Parameter(description = "是否启用")
    private Boolean                enabled;

    /**
     * 是否已配置
     */
    @Parameter(description = "是否已配置")
    private Boolean                configured;

    /**
     * 备注
     */
    @Parameter(description = "备注")
    private String                 remark;
}
