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
import java.util.List;

import cn.frank.ulp.common.entity.setting.config.SmsConfig;
import cn.frank.ulp.common.message.enums.SmsProvider;
import cn.frank.ulp.common.message.sms.SmsProviderConfig;

import lombok.Builder;
import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 短信服务商配置查询结果
 *
 * @author Frank Zhang
 */
@Data
@Builder
@Schema(description = "短信服务商配置查询响应")
public class SmsProviderConfigResult implements Serializable {

    @Serial
    private static final long              serialVersionUID = -2667374916357438335L;
    /**
     * 平台
     */
    @Parameter(description = "提供商")
    private SmsProvider                    provider;
    /**
     * 配置
     */
    @Parameter(description = "参数配置")
    private SmsProviderConfig              config;

    /**
     * 配置
     */
    @Parameter(description = "模板配置")
    private List<SmsConfig.TemplateConfig> templates;
    /**
     * 描述
     */
    @Parameter(description = "描述")
    private String                         desc;

    /**
     * 是否启用
     */
    @Parameter(description = "是否启用")
    private Boolean                        enabled;

    /**
     * 语言
     */
    @Parameter(description = "语言")
    private String                         language;
}
