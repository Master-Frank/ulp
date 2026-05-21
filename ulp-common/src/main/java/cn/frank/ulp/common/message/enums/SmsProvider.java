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
package cn.frank.ulp.common.message.enums;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonValue;

import cn.frank.ulp.support.web.converter.EnumConvert;

/**
 * 短信平台
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/19
 */
public enum SmsProvider implements Serializable {

                                                 /**
                                                  * 阿里云
                                                  */
                                                 ALIYUN("aliyun", "阿里云"),
                                                 /**
                                                  * 腾讯云
                                                  */
                                                 TENCENT("tencent", "腾讯云"),
                                                 /**
                                                  * 七牛
                                                  */
                                                 QINIU("qiniu", "七牛");

    /**
     * code
     */
    @JsonValue
    private final String code;
    /**
     * desc
     */
    private final String desc;

    SmsProvider(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    @EnumConvert
    public static SmsProvider getType(String code) {
        SmsProvider[] values = values();
        for (SmsProvider status : values) {
            if (String.valueOf(status.getCode()).equals(code)) {
                return status;
            }
        }
        throw new NullPointerException("未找到该平台");
    }

    @Override
    public String toString() {
        return this.code;
    }
}
