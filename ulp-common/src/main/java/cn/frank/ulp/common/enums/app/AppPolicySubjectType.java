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
 * 权限策略主体类型
 * 用户、角色、组织机构
 *
 * @author Frank Zhang
 */
public enum AppPolicySubjectType implements BaseEnum {
                                                      /**
                                                       * 角色
                                                       */
                                                      ROLE("ROLE", "角色"),
                                                      /**
                                                       * 用户
                                                       */
                                                      USER("USER", "用户"),
                                                      /**
                                                       * 组织机构
                                                       */
                                                      ORGANIZATION("ORGANIZATION", "组织机构"),
                                                      /**
                                                       * 分组
                                                       */
                                                      USER_GROUP("USER_GROUP", "分组"),
                                                      /**
                                                       * TODO 客户端
                                                       */
                                                      CLIENT("CLIENT", "客户端");

    /**
     * code
     */
    @JsonValue
    private final String code;
    /**
     * desc
     */
    private final String desc;

    AppPolicySubjectType(String code, String desc) {
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

    /**
     * 获取类型
     *
     * @param code {@link String}
     * @return {@link AppPolicySubjectType}
     */
    @EnumConvert
    public static AppPolicySubjectType getType(String code) {
        AppPolicySubjectType[] values = values();
        for (AppPolicySubjectType status : values) {
            if (String.valueOf(status.getCode()).equals(code)) {
                return status;
            }
        }
        return null;
    }
}
