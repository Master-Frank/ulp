/*
 * ulp-audit - United Login Platform
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
package cn.frank.ulp.audit.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import cn.frank.ulp.support.web.converter.EnumConvert;

import lombok.Getter;

/**
 * 事件状态
 *
 * @author Frank Zhang
 */
@Getter
public enum EventStatus {
                         /**
                          * 成功
                          */
                         SUCCESS("success", "成功"),
                         /**
                          * 失败
                          */
                         FAIL("fail", "失败");

    @JsonValue
    private final String code;
    private final String desc;

    EventStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 获取类型
     *
     * @param code {@link String}
     * @return {@link EventStatus}
     */
    @EnumConvert
    public static EventStatus getType(String code) {
        EventStatus[] values = values();
        for (EventStatus status : values) {
            if (String.valueOf(status.getCode()).equals(code)) {
                return status;
            }
        }
        return null;
    }
}
