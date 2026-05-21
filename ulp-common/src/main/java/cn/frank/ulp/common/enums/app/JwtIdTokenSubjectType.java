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
