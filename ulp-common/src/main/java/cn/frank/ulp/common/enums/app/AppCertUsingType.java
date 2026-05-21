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
 * 证书使用类型
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/5/31 22:28
 */
public enum AppCertUsingType implements BaseEnum {
                                                  /**
                                                   * OIDC JWK
                                                   */
                                                  OIDC_JWK("oidc_jwk", "OIDC JWK"),

                                                  /**
                                                   * JWT 加密
                                                   */
                                                  JWT_ENCRYPT("jwt_encrypt", "JWT 加密");

    @JsonValue
    private final String code;
    /**
     * name
     */
    private final String desc;

    AppCertUsingType(String code, String desc) {
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
    public static AppCertUsingType getType(String code) {
        AppCertUsingType[] values = values();
        for (AppCertUsingType type : values) {
            if (String.valueOf(type.getCode()).equals(code)) {
                return type;
            }
        }
        return null;
    }
}
