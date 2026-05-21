/*
 * ulp-common - United Login Platform
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
package cn.frank.ulp.common.entity.setting.config;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import cn.frank.ulp.common.enums.Language;
import cn.frank.ulp.common.enums.SmsType;
import cn.frank.ulp.common.message.enums.SmsProvider;
import cn.frank.ulp.common.message.sms.SmsProviderConfig;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;

/**
 * 短信配置
 *
 * @author TopIAM
 */
@Data
public class SmsConfig implements Serializable {

    @Serial
    private static final long    serialVersionUID = 5293005308937620292L;

    /**
     * 提供商
     */
    private SmsProvider          provider;

    /**
     * 语言
     */
    private Language             language;

    /**
     * 配置
     */
    private SmsProviderConfig    config;

    /**
     * 模版配置
     */
    private List<TemplateConfig> templates;

    public SmsConfig() {
    }

    @Data
    public static class TemplateConfig implements Serializable {

        @Serial
        private static final long serialVersionUID = 2801844583775238689L;

        @Parameter(description = "短信类型")
        private SmsType           type;
        @Parameter(description = "模板ID/CODE")
        private String            code;

        public TemplateConfig() {
        }
    }
}
