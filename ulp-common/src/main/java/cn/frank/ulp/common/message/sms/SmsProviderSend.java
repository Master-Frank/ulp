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
package cn.frank.ulp.common.message.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.frank.ulp.common.message.enums.SmsProvider;

/**
 * 短信发送
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/27 21:03
 */
public interface SmsProviderSend {

    Logger log = LoggerFactory.getLogger(SmsProviderSend.class);

    /**
     * 发送短信验证码
     * <p>
     * 如果是腾讯云，params要用LinkedHashMap，保证顺序
     *
     * @param request {@link SendSmsRequest}
     * @return {@link SmsResponse}
     */
    SmsResponse send(SendSmsRequest request);

    /**
     * 服务商类型
     *
     * @return {@link SmsProvider}
     */
    SmsProvider getProvider();
}
