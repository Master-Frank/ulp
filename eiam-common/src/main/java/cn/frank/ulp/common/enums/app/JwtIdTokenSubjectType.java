/*
 * eiam-common - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.common.enums.app;

import com.fasterxml.jackson.annotation.JsonValue;

import cn.frank.ulp.support.enums.BaseEnum;
import cn.frank.ulp.support.web.converter.EnumConvert;

/**
 * JWT id_token Subject 类型
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/02/15
 */
public enum JwtIdTokenSubjectType implements BaseEnum {
                                                       /**
                                                        * 用户名
                                                        */
                                                       USER_ID("user_id"),
                                                       /**
                                                        * 应用账户
                                                        */
                                                       APP_USER("app_user");

    /**
     * code
     */
    @JsonValue
    private final String code;

    JwtIdTokenSubjectType(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return null;
    }

    /**
     * 获取类型
     *
     * @param code {@link String}
     * @return {@link InitLoginType}
     */
    @EnumConvert
    public static JwtIdTokenSubjectType getType(String code) {
        JwtIdTokenSubjectType[] values = values();
        for (JwtIdTokenSubjectType status : values) {
            if (String.valueOf(status.getCode()).equals(code)) {
                return status;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
