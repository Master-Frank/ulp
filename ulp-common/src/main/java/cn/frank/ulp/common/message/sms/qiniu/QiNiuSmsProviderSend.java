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
package cn.frank.ulp.common.message.sms.qiniu;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.sms.SmsManager;
import com.qiniu.util.Auth;

import cn.frank.ulp.common.message.enums.SmsProvider;
import cn.frank.ulp.common.message.sms.SendSmsRequest;
import cn.frank.ulp.common.message.sms.SmsProviderSend;
import cn.frank.ulp.common.message.sms.SmsResponse;

/**
 * 七牛短信发送
 *
 * @author Frank Zhang
 */
public class QiNiuSmsProviderSend implements SmsProviderSend {
    private final SmsManager smsManager;

    public QiNiuSmsProviderSend(QiNiuSmsProviderConfig config) {
        Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
        // 实例化一个SmsManager对象
        this.smsManager = new SmsManager(auth);
    }

    @Override
    public SmsResponse send(SendSmsRequest sendSmsParam) {
        try {
            Response resp = smsManager.sendSingleMessage(sendSmsParam.getTemplate(),
                sendSmsParam.getPhone(), sendSmsParam.getParameters());
            JSONObject response = JSON.parseObject(resp.bodyString());
            if (response.containsKey("message_id")
                && StringUtils.hasText(response.getString("message_id"))) {
                return new SmsResponse("成功", Boolean.TRUE, SmsProvider.QINIU);
            }
        } catch (QiniuException e) {
            log.error("七牛发送短信异常: [{}]", e.getMessage());
        }
        return new SmsResponse("失败", Boolean.FALSE, SmsProvider.QINIU);
    }

    @Override
    public SmsProvider getProvider() {
        return SmsProvider.QINIU;
    }
}
