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

import cn.frank.ulp.common.message.enums.SmsProvider;
import cn.frank.ulp.common.message.sms.aliyun.AliyunSmsProviderConfig;
import cn.frank.ulp.common.message.sms.aliyun.AliyunSmsProviderSend;
import cn.frank.ulp.common.message.sms.qiniu.QiNiuSmsProviderConfig;
import cn.frank.ulp.common.message.sms.qiniu.QiNiuSmsProviderSend;
import cn.frank.ulp.common.message.sms.tencent.TencentSmsProviderConfig;
import cn.frank.ulp.common.message.sms.tencent.TencentSmsProviderSend;

/**
 *
 * @author Frank Zhang
 */
public class SmsSendProviderFactory {

    private SmsSendProviderFactory() {
    }

    /**
     * 获取实例化
     *
     * @param config {@link SmsProviderConfig}
     * @return {@link SmsProviderSend}
     */
    public static SmsProviderSend newInstance(SmsProviderConfig config) {
        SmsProvider provider = config.getProvider();
        //阿里云
        if (SmsProvider.ALIYUN.equals(provider)) {
            return new AliyunSmsProviderSend((AliyunSmsProviderConfig) config);
        }
        //腾讯
        if (SmsProvider.TENCENT.equals(provider)) {
            return new TencentSmsProviderSend((TencentSmsProviderConfig) config);
        }
        //七牛
        if (SmsProvider.QINIU.equals(provider)) {
            return new QiNiuSmsProviderSend((QiNiuSmsProviderConfig) config);
        }
        throw new IllegalArgumentException("暂未支持该短信提供商类型");
    }
}
