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

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 邮件模板配置结果
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/17 23:05
 */
@Data
@Schema(description = "获取邮件模板")
public class EmailTemplateResult implements Serializable {
    @Serial
    private static final long serialVersionUID = 6499437680155500022L;
    /**
     * 名称
     */
    @Parameter(description = "名称")
    private String            name;
    /**
     * 发送人
     */
    @Parameter(description = "发送人")
    private String            sender;

    /**
     * 主题
     */
    @Parameter(description = "主题")
    private String            subject;

    /**
     * 内容
     */
    @Parameter(description = "内容")
    private String            content;

    /**
     * 描述
     */
    @Parameter(description = "描述")
    private String            desc;

    /**
     * 自定义
     */
    @Parameter(description = "自定义")
    private Boolean           custom;

}
