/*
 * ulp-support - ULP support library (replaces the former eiam-support private jar).
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
package cn.frank.ulp.support.enums;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SecretType implements BaseEnum, Serializable {

    LOGIN("login", "TOPIAM_LOGIN_SECRET", "登录密钥"),

    ENCRYPT("encrypt", "TOPIAM_ENCRYPT_SECRET", "加密密钥");

    @JsonValue
    private final String code;

    private final String key;

    private final String desc;

    public static SecretType getType(String code) {
        for (SecretType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new RuntimeException("未找到密钥类型");
    }

    @Override
    public String getCode() {
        return code;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
