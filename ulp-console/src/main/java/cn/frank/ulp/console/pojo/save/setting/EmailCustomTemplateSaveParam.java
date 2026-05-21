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
package cn.frank.ulp.console.pojo.save.setting;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 邮件模板配置更新参数
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/17 23:20
 */
@Data
@Schema(description = "邮件模板配置保存入参")
public class EmailCustomTemplateSaveParam implements Serializable {
    @Serial
    private static final long serialVersionUID = -4437284094645205715L;

    /**
     * 发送人
     */
    @Schema(description = "发件人")
    @NotBlank(message = "发件人不能为空")
    private String            sender;

    /**
     * 主题
     */
    @Schema(description = "主题")
    @NotBlank(message = "邮件主题不能为空")
    private String            subject;

    /**
     * 内容
     */
    @Schema(description = "内容")
    @NotBlank(message = "邮件内容不能为空")
    private String            content;
}
