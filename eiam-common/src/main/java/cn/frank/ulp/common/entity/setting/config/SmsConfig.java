/*
 * eiam-common - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
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
