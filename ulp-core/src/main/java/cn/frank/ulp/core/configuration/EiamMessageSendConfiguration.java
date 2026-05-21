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
package cn.frank.ulp.core.configuration;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

import cn.frank.ulp.common.entity.setting.config.SmsConfig;
import cn.frank.ulp.common.message.mail.DefaultMailProviderSendImpl;
import cn.frank.ulp.common.message.mail.MailProviderConfig;
import cn.frank.ulp.common.message.mail.MailProviderSend;
import cn.frank.ulp.common.message.sms.SmsNoneProviderSend;
import cn.frank.ulp.common.message.sms.SmsProviderSend;
import cn.frank.ulp.common.message.sms.SmsSendProviderFactory;
import static org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME;

import static cn.frank.ulp.common.constant.ConfigBeanNameConstants.MAIL_PROVIDER_SEND;
import static cn.frank.ulp.common.constant.ConfigBeanNameConstants.SMS_PROVIDER_SEND;
import static cn.frank.ulp.core.context.ContextService.getMailProviderConfig;
import static cn.frank.ulp.core.context.ContextService.getSmsProviderConfig;

/**
 * 消息发送配置
 *
 * @author Frank Zhang
 */
@Configuration
public class EiamMessageSendConfiguration {

    /**
     * 短信发送
     *
     * @return {@link SmsProviderSend}
     */
    @Bean(SMS_PROVIDER_SEND)
    @RefreshScope
    public SmsProviderSend smsProviderSend() {
        //查询当前启用的短信提供商
        SmsConfig config = getSmsProviderConfig();
        if (Objects.isNull(config.getConfig())) {
            return new SmsNoneProviderSend();
        }
        return SmsSendProviderFactory.newInstance(config.getConfig());
    }

    /**
     * 邮件发送
     *
     * @param taskExecutor {@link TaskExecutor}
     * @return {@link MailProviderSend}
     */
    @Bean(MAIL_PROVIDER_SEND)
    @RefreshScope
    public MailProviderSend mailProviderSend(@Qualifier(value = APPLICATION_TASK_EXECUTOR_BEAN_NAME) TaskExecutor taskExecutor) {

        MailProviderConfig config = getMailProviderConfig();
        if (Objects.isNull(config)) {
            return null;
        }
        return new DefaultMailProviderSendImpl(config, taskExecutor);
    }
}
