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
import java.util.List;

import com.alibaba.fastjson2.JSONObject;

import cn.frank.ulp.common.entity.setting.config.SmsConfig;
import cn.frank.ulp.common.enums.Language;
import cn.frank.ulp.common.message.enums.SmsProvider;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 保存短信服务商创建请求入参
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "保存短信验证服务入参")
public class SmsProviderSaveParam implements Serializable {
    @Serial
    private static final long              serialVersionUID = 4125843198392920166L;

    /**
     * 平台
     */
    @Schema(description = "提供商")
    @NotNull(message = "短信提供商不能为空")
    private SmsProvider                    provider;

    /**
     * 配置JSON串
     */
    @Schema(description = "配置JSON串")
    @NotNull(message = "配置不能为空")
    private JSONObject                     config;

    /**
     * 场景语言
     */
    @Schema(description = "场景语言")
    @NotNull(message = "场景语言不能为空")
    private Language                       language;

    /**
     * 短信模板配置
     */
    @Schema(description = "短信模板配置")
    @NotNull(message = "短信模板配置不能为空")
    private List<SmsConfig.TemplateConfig> templates;
}
