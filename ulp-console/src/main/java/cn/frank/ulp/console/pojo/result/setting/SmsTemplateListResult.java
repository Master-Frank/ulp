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

import java.io.Serial;
import java.io.Serializable;

import cn.frank.ulp.common.enums.Language;
import cn.frank.ulp.common.enums.MessageCategory;
import cn.frank.ulp.common.enums.SmsType;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 短信配置结果
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "获取短信模板列表")
public class SmsTemplateListResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 5983857137670090984L;

    /**
     * 名称
     */
    @Schema(description = "名称")
    private String            name;

    /**
     * 类型
     */
    @Schema(description = "类型")
    private SmsType           type;

    /**
     * 模板类型
     */
    @Schema(description = "模板类型")
    private MessageCategory   category;

    /**
     * 内容
     */
    @Schema(description = "内容")
    private String            content;

    /**
     * Language
     */
    @Schema(description = "Language")
    private Language          language;
}
