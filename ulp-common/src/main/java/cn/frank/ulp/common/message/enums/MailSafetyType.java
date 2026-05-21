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

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonValue;

import cn.frank.ulp.support.web.converter.EnumConvert;

/**
 * 邮件安全方式
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/19 23:09
 */
public enum MailSafetyType implements Serializable {
                                                    /**
                                                     * 无
                                                     */
                                                    None("none", "无"),
                                                    /**
                                                     * SSL
                                                     */
                                                    SSL("ssl", "SSL");

    /**
     * code
     */
    @JsonValue
    private final String code;
    /**
     * 描述
     */
    private final String desc;

    /**
     * 构造
     *
     * @param code {@link String}
     * @param desc {@link String}
     */
    MailSafetyType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @EnumConvert
    public static MailSafetyType getType(String code) {
        MailSafetyType[] values = values();
        for (MailSafetyType status : values) {
            if (String.valueOf(status.getCode()).equals(code)) {
                return status;
            }
        }
        throw new NullPointerException("未找到该类型");
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return code;
    }
}
