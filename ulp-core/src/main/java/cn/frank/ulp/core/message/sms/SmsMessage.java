/*
 * ulp-core - United Login Platform
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
package cn.frank.ulp.core.message.sms;

import java.io.Serializable;
import java.util.Map;

import cn.frank.ulp.common.enums.SmsType;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短信消息事件
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/25 21:07
 */
@Data
@NoArgsConstructor
public class SmsMessage implements Serializable {
    /**
     * 消息类型
     */
    private SmsType             type;
    /**
     * 参数
     */
    private Map<String, String> parameter;

    public SmsMessage(SmsType type, Map<String, String> parameter) {
        this.type = type;
        this.parameter = parameter;
    }
}
