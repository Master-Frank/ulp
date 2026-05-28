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

import java.time.LocalDateTime;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;

import cn.frank.ulp.common.entity.message.SmsSendRecordEntity;
import cn.frank.ulp.common.enums.MessageCategory;
import cn.frank.ulp.common.exception.SmsMessageSendException;
import cn.frank.ulp.common.message.sms.SendSmsRequest;
import cn.frank.ulp.common.message.sms.SmsProviderSend;
import cn.frank.ulp.common.message.sms.SmsResponse;
import cn.frank.ulp.common.repository.message.SmsSendRecordRepository;

/**
 * 短信消息通知事件
 *
 * @author Frank Zhang
 */
@Async
@Component
public class SmsMsgEventListener implements ApplicationListener<SmsMsgEvent> {
    /**
     * Logger
     */
    private final Logger logger = LoggerFactory.getLogger(SmsMsgEventListener.class);

    /**
     * 发送事件通知
     *
     * @param event {@link SmsMsgEvent}
     */
    @Override
    public void onApplicationEvent(@NonNull SmsMsgEvent event) {
        SendSmsRequest smsParam = new SendSmsRequest();
        try {
            //@formatter:off
            // 手机号
            smsParam.setPhone(event.getParameter().get(SmsMsgEventPublish.PHONE));
            // 模版编码
            smsParam.setTemplate(event.getParameter().get(SmsMsgEventPublish.TEMPLATE_CODE));
            // Content 记录参数值
            String content = event.getParameter().get(SmsMsgEventPublish.CONTENT);
            // 移除手机号，模版编码和Content
            event.getParameter().remove(SmsMsgEventPublish.PHONE);
            event.getParameter().remove(SmsMsgEventPublish.TEMPLATE_CODE);
            event.getParameter().remove(SmsMsgEventPublish.CONTENT);
            // 短信模版参数
            smsParam.setParameters(event.getParameter());

            //@formatter:on
            SmsResponse send = smsProviderSend.send(smsParam);
            // 保存发送记录
            if (!Objects.isNull(send)) {
                //@formatter:off
                SmsSendRecordEntity record = new SmsSendRecordEntity()
                        .setContent(content)
                        .setResult(send.getMessage())
                        .setSuccess(send.getSuccess())
                        .setSendTime(LocalDateTime.now())
                        .setProvider(send.getProvider())
                        .setCategory(MessageCategory.CODE)
                        .setType(event.getType())
                        .setPhone(smsParam.getPhone());
                record.setRemark(JSON.toJSONString(send));
                if (!send.getSuccess()) {
                    logger.error("发送短信失败: params: {}, response: {}", smsParam, send);
                }
                //@formatter:on
                smsSendLogRepository.save(record);
            } else {
                logger.error("发送短信失败,返回值为空: params: {}, ", smsParam);
            }
        } catch (Exception e) {
            logger.error("发送短信消息异常 params:{}, error: {}", smsParam, e.getMessage());
            throw new SmsMessageSendException(e);
        }
    }

    /**
     * SmsSend
     */
    private final SmsProviderSend         smsProviderSend;

    private final SmsSendRecordRepository smsSendLogRepository;

    public SmsMsgEventListener(SmsProviderSend smsProviderSend,
                               SmsSendRecordRepository smsSendLogRepository) {
        this.smsProviderSend = smsProviderSend;
        this.smsSendLogRepository = smsSendLogRepository;
    }
}
