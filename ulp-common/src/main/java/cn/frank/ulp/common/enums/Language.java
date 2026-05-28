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
package cn.frank.ulp.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import cn.frank.ulp.support.enums.BaseEnum;
import cn.frank.ulp.support.web.converter.EnumConvert;

/**
 * 语言
 *
 * @author Frank Zhang
 */
public enum Language implements BaseEnum {
                                          /**
                                           * 英语
                                           */
                                          EN("en", "英语"),
                                          /**
                                           * 中文
                                           */
                                          ZH("zh", "中文");

    /**
     * code
     */
    @JsonValue
    private final String locale;
    /**
     * desc
     */
    private final String desc;

    Language(String locale, String desc) {
        this.locale = locale;
        this.desc = desc;
    }

    public String getLocale() {
        return locale;
    }

    @Override
    public String getCode() {
        return locale;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    /**
     * 获取类型
     *
     * @param code {@link String}
     * @return {@link Language}
     */
    @EnumConvert
    public static Language getType(String code) {
        Language[] values = values();
        for (Language status : values) {
            if (String.valueOf(status.getLocale()).equals(code)) {
                return status;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.getLocale();
    }

}
