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
 * 用户状态
 *
 * @author Frank Zhang
 */
public enum UserStatus implements BaseEnum {
                                            /**
                                             * 已启用
                                             */
                                            ENABLED("enabled", "启用"),

                                            /**
                                            * 已禁用
                                            */
                                            DISABLED("disabled", "禁用"),

                                            /**
                                             * 登录失败锁定
                                             */
                                            LOCKED("locked", "锁定"),

                                            /**
                                             * 过期锁定
                                             */
                                            EXPIRED_LOCKED("expired_locked", "过期锁定"),

                                            /**
                                            * 密码过期锁定
                                            */
                                            PASSWORD_EXPIRED_LOCKED("password_expired_locked",
                                                                    "密码过期锁定");

    /**
     * code
     */
    @JsonValue
    private final String code;
    /**
     * desc
     */
    private final String desc;

    UserStatus(String code, String desc) {
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
    public static UserStatus getStatus(String code) {
        UserStatus[] values = values();
        for (UserStatus status : values) {
            if (String.valueOf(status.getCode()).equals(code)) {
                return status;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return code;
    }

}
