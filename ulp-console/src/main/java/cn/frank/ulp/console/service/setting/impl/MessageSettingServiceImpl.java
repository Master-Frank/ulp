/*
 * ulp-console - United Login Platform
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
package cn.frank.ulp.console.service.setting.impl;

import org.springframework.stereotype.Service;

import cn.frank.ulp.common.entity.setting.SettingEntity;
import cn.frank.ulp.common.repository.setting.SettingRepository;
import cn.frank.ulp.console.converter.setting.MessageSettingConverter;
import cn.frank.ulp.console.pojo.result.setting.EmailProviderConfigResult;
import cn.frank.ulp.console.pojo.result.setting.SmsProviderConfigResult;
import cn.frank.ulp.console.pojo.save.setting.MailProviderSaveParam;
import cn.frank.ulp.console.pojo.save.setting.SmsProviderSaveParam;
import cn.frank.ulp.console.service.setting.MessageSettingService;
import cn.frank.ulp.support.context.ApplicationContextService;
import static cn.frank.ulp.common.constant.ConfigBeanNameConstants.MAIL_PROVIDER_SEND;
import static cn.frank.ulp.common.constant.ConfigBeanNameConstants.SMS_PROVIDER_SEND;
import static cn.frank.ulp.core.setting.MessageSettingConstants.MESSAGE_PROVIDER_EMAIL;
import static cn.frank.ulp.core.setting.MessageSettingConstants.MESSAGE_SMS_PROVIDER;

/**
 * 消息设置
 *
 * @author Frank Zhang
 */
@Service
public class MessageSettingServiceImpl extends SettingServiceImpl implements MessageSettingService {

    /**
     * 保存配置
     *
     * @param param {@link SettingEntity}
     * @return {@link Boolean}
     */
    @Override
    public Boolean saveMailProviderConfig(MailProviderSaveParam param) {
        SettingEntity entity = messageSettingConverter.mailProviderConfigToEntity(param);
        Boolean setting = saveSetting(entity);
        ApplicationContextService.refresh(MAIL_PROVIDER_SEND);
        return setting;
    }

    /**
     * 保存邮件验证配置
     *
     * @param param {@link SmsProviderSaveParam}
     * @return {@link Boolean}
     */
    @Override
    public Boolean saveSmsProviderConfig(SmsProviderSaveParam param) {
        SettingEntity entity = messageSettingConverter.smsProviderConfigToEntity(param);
        Boolean setting = saveSetting(entity);
        ApplicationContextService.refresh(SMS_PROVIDER_SEND);
        return setting;
    }

    /**
     * 禁用短信验证服务
     *
     * @return {@link Boolean}
     */
    @Override
    public Boolean disableSmsProvider() {
        Boolean setting = removeSetting(MESSAGE_SMS_PROVIDER);
        // refresh
        ApplicationContextService.refresh(SMS_PROVIDER_SEND);
        return setting;
    }

    /**
     * 禁用邮件提供商
     *
     * @return {@link Boolean}
     */
    @Override
    public Boolean disableMailProvider() {
        Boolean setting = removeSetting(MESSAGE_PROVIDER_EMAIL);
        // refresh
        ApplicationContextService.refresh(MAIL_PROVIDER_SEND);
        return setting;
    }

    /**
     * 获取邮件提供商配置
     *
     * @return {@link EmailProviderConfigResult}
     */
    @Override
    public EmailProviderConfigResult getMailProviderConfig() {
        SettingEntity entity = getSetting(MESSAGE_PROVIDER_EMAIL);
        return messageSettingConverter.entityToMailProviderConfig(entity);
    }

    /**
     * 获取短信验证服务配置
     *
     * @return {@link SmsProviderConfigResult}
     */
    @Override
    public SmsProviderConfigResult getSmsProviderConfig() {
        SettingEntity entity = getSetting(MESSAGE_SMS_PROVIDER);
        return messageSettingConverter.entityToSmsProviderConfig(entity);
    }

    private final MessageSettingConverter messageSettingConverter;

    public MessageSettingServiceImpl(SettingRepository settingsRepository,
                                     MessageSettingConverter messageSettingConverter) {
        super(settingsRepository);
        this.messageSettingConverter = messageSettingConverter;
    }
}
