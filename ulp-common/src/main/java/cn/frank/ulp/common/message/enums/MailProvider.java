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
package cn.frank.ulp.common.message.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import cn.frank.ulp.support.enums.BaseEnum;
import cn.frank.ulp.support.web.converter.EnumConvert;

/**
 * 邮件提供商配置
 *
 * @author Frank Zhang
 */
public enum MailProvider implements BaseEnum {

                                              /**
                                               * 自定义
                                               */
                                              CUSTOMIZE("customize", "自定义", null, null, null),
                                              /**
                                               * 腾讯企业邮
                                               */
                                              TENCENT("tencent", "腾讯企业邮", "smtp.exmail.qq.com", 25,
                                                      465),
                                              /**
                                               * 阿里企业邮
                                               */
                                              ALIYUN("aliyun", "阿里企业邮", "smtp.mxhichina.com", 25,
                                                     465),
                                              /**
                                               * 网易
                                               */
                                              NETEASE("netease", "网易企业邮", "smtp.163.com", 25, 465);

    /**
     * code
     */
    @JsonValue
    private final String  code;
    /**
     * 名称
     */
    private final String  name;
    /**
     * smtp 地址
     */
    private final String  smtpUrl;
    /**
     * 端口
     */
    private final Integer port;
    /**
     * SSL 端口
     */
    private final Integer sslPort;

    MailProvider(String code, String name, String smtpUrl, Integer port, Integer sslPort) {
        this.code = code;
        this.name = name;
        this.smtpUrl = smtpUrl;
        this.port = port;
        this.sslPort = sslPort;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return null;
    }

    public String getName() {
        return name;
    }

    @EnumConvert
    public static MailProvider getType(String code) {
        MailProvider[] values = values();
        for (MailProvider status : values) {
            if (String.valueOf(status.getCode()).equals(code)) {
                return status;
            }
        }
        throw new NullPointerException("未找到该平台");
    }

    public String getSmtpUrl() {
        return smtpUrl;
    }

    public Integer getPort() {
        return port;
    }

    public Integer getSslPort() {
        return sslPort;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
