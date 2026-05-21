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
package cn.frank.ulp.common.enums.account;

import com.fasterxml.jackson.annotation.JsonValue;

import cn.frank.ulp.support.enums.BaseEnum;
import cn.frank.ulp.support.web.converter.EnumConvert;

/**
 * 组织机构类型
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/9 21:04
 */
public enum OrganizationType implements BaseEnum {
                                                  /**
                                                   * 集团
                                                   */
                                                  GROUP("group", "集团"),
                                                  /**
                                                   * 公司
                                                   */
                                                  COMPANY("company", "公司"),
                                                  /**
                                                   * 部门
                                                   */
                                                  DEPARTMENT("department", "部门"),
                                                  /**
                                                   * 单位
                                                   */
                                                  UNIT("unit", "单位");

    /**
     * code
     */
    @JsonValue
    private final String code;
    /**
     * desc
     */
    private final String desc;

    OrganizationType(String code, String desc) {
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
     * @return {@link OrganizationType}
     */
    @EnumConvert
    public static OrganizationType getType(String code) {
        OrganizationType[] values = values();
        for (OrganizationType status : values) {
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
