/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
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
 * @author TopIAM
 * Created by support@topiam.cn on 2021/10/1 21:19
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
