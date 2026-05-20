/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.support.enums;

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
