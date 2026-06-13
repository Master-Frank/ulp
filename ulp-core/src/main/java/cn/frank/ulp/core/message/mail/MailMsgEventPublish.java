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
package cn.frank.ulp.core.message.mail;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import cn.frank.ulp.common.enums.MailType;
import cn.frank.ulp.common.enums.MessageCategory;
import cn.frank.ulp.common.message.mail.MailProviderConfig;
import cn.frank.ulp.support.context.ApplicationContextService;
import cn.frank.ulp.support.exception.UlpException;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import tools.jackson.databind.ObjectMapper;
import static cn.frank.ulp.core.context.ContextService.getCodeValidTime;
import static cn.frank.ulp.core.context.ContextService.getMailProviderConfig;
import static cn.frank.ulp.core.message.MsgVariable.*;
import static cn.frank.ulp.support.constant.UlpConstants.DEFAULT_DATE_TIME_FORMATTER;

/**
 * 邮件消息发送
 *
 * @author Frank Zhang
 */
@Component
@Slf4j
@AllArgsConstructor
public class MailMsgEventPublish {

    /**
     * ApplicationEventPublisher
     */
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 发布验证代码
     *
     * @param receiver   {@link MessageCategory} 收件人
     * @param type       {@link MailType} 邮件类型
     * @param verifyCode {@link MessageCategory} 验证码
     */
    public void publishVerifyCode(String receiver, MailType type, String verifyCode) {
        // publish event
        HashMap<String, Object> parameter = new HashMap<>(16);
        parameter.put(VERIFY_CODE, verifyCode);
        parameter.put(EXPIRE_TIME_KEY, getCodeValidTime());
        publish(type, receiver, parameter);
    }

    /**
     * 发布 邮件通知事件
     *
     * @param type     {@link MailType} 邮件类型
     * @param receiver {@link String} 接受者
     */
    @SneakyThrows
    public void publish(MailType type, String receiver, Map<String, Object> parameter) {
        MailProviderConfig config = getMailProviderConfig();
        if (Objects.isNull(config)) {
            throw new UlpException("未配置邮件服务");
        }

        if (StringUtils.isBlank(receiver)) {
            log.warn("发送邮件通知失败, 接受者为空, type: {}", type);
            return;
        }
        // 时间点
        parameter.put(TIME, LocalDateTime.now().format(DEFAULT_DATE_TIME_FORMATTER));
        // 客户端名称
        parameter.put(CLIENT_NAME, "ULP 统一登录平台");
        // 客户端描述
        parameter.put(CLIENT_DESCRIPTION,
            "ULP（United Login Platform）统一登录平台，用于管理企业内账号、权限、身份认证与应用访问，整合本地与云端的内部办公系统、业务系统及三方 SaaS 系统的身份，实现一个账号打通所有应用的服务。");
        // 收件人
        parameter.put(USER_EMAIL, receiver);
        // publish event
        ObjectMapper objectMapper = ApplicationContextService.getBean(ObjectMapper.class);
        applicationEventPublisher.publishEvent(new MailMsgEvent(type, receiver, parameter));

    }
}
