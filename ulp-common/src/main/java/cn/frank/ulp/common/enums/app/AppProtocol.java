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
package cn.frank.ulp.common.enums.app;

import com.fasterxml.jackson.annotation.JsonValue;

import cn.frank.ulp.support.enums.BaseEnum;
import cn.frank.ulp.support.web.converter.EnumConvert;

/**
 * 应用模板
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/11/29 22:27
 */
public enum AppProtocol implements BaseEnum {
                                             /**
                                              * OIDC
                                              */
                                             OIDC("OIDC", "OIDC"),

                                             /**
                                              * JWT
                                              */
                                             JWT("JWT", "JWT"),

                                             /**
                                              * FORM表单
                                              */
                                             FORM("FORM", "表单代填");

    @JsonValue
    private final String code;
    /**
     * name
     */
    private final String desc;

    AppProtocol(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @EnumConvert
    public static AppProtocol getType(String code) {
        AppProtocol[] values = values();
        for (AppProtocol source : values) {
            if (String.valueOf(source.getCode()).equals(code)) {
                return source;
            }
        }
        return null;
    }
}
