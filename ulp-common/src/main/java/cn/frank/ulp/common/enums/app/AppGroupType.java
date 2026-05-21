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
 * 应用分组类型
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/9/9 16:22
 */
public enum AppGroupType implements BaseEnum {
                                              /**
                                               * 默认分组
                                               */
                                              DEFAULT("default", "默认分组"),
                                              /**
                                               * 自定义分组
                                               */
                                              CUSTOM("custom", "自定义分组");

    @JsonValue
    private final String code;
    private final String desc;

    AppGroupType(String code, String desc) {
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
     * @return {@link AppGroupType}
     */
    @EnumConvert
    public static AppGroupType getType(String code) {
        AppGroupType[] values = values();
        for (AppGroupType status : values) {
            if (String.valueOf(status.getCode()).equals(code)) {
                return status;
            }
        }
        return null;
    }
}
