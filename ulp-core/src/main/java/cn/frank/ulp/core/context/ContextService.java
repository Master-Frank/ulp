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
package cn.frank.ulp.core.context;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import tools.jackson.core.JsonProcessingException;
import tools.jackson.databind.ObjectMapper;
import com.shapesecurity.salvation2.Directives.SourceExpressionDirective;
import com.shapesecurity.salvation2.FetchDirectiveKind;
import com.shapesecurity.salvation2.Policy;
import com.shapesecurity.salvation2.Values.Host;

import cn.frank.ulp.common.entity.setting.SettingEntity;
import cn.frank.ulp.common.entity.setting.config.SmsConfig;
import cn.frank.ulp.common.jackjson.encrypt.EncryptContextHelp;
import cn.frank.ulp.common.jackjson.encrypt.EncryptionModule;
import cn.frank.ulp.common.message.enums.SmsProvider;
import cn.frank.ulp.common.message.mail.MailProviderConfig;
import cn.frank.ulp.common.message.sms.aliyun.AliyunSmsProviderConfig;
import cn.frank.ulp.common.message.sms.qiniu.QiNiuSmsProviderConfig;
import cn.frank.ulp.common.message.sms.tencent.TencentSmsProviderConfig;
import cn.frank.ulp.common.repository.setting.SettingRepository;
import cn.frank.ulp.core.setting.MessageSettingConstants;
import cn.frank.ulp.core.setting.SecuritySettingConstants;
import cn.frank.ulp.support.autoconfiguration.SupportProperties;
import cn.frank.ulp.support.context.ApplicationContextService;
import cn.frank.ulp.support.exception.UlpException;
import cn.frank.ulp.support.security.util.ContentSecurityPolicyUtils;
import static cn.frank.ulp.common.constant.AuthnConstants.FE_LOGIN;
import static cn.frank.ulp.common.constant.ConfigBeanNameConstants.DEFAULT_SECURITY_FILTER_CHAIN;
import static cn.frank.ulp.core.setting.MessageSettingConstants.MESSAGE_SMS_PROVIDER;
import static cn.frank.ulp.core.setting.SecuritySettingConstants.*;

/**
 * ULP 上下文
 *
 * @author Frank Zhang
 */
public final class ContextService {

    private static final Logger logger = LoggerFactory.getLogger(ContextService.class);

    /**
     * 获取验证码提供商配置
     *
     * @return {@link SmsConfig}
     */
    public static SmsConfig getSmsProviderConfig() {
        SettingEntity setting = getSettingRepository().findByName(MESSAGE_SMS_PROVIDER);
        if (!Objects.isNull(setting) && StringUtils.isNotBlank(setting.getValue())) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                // 指定序列化输入的类型
                objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
                    ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
                SmsConfig config = objectMapper.readValue(setting.getValue(), SmsConfig.class);
                SmsProvider provider = config.getProvider();
                //阿里
                if (SmsProvider.ALIYUN.equals(provider)) {
                    AliyunSmsProviderConfig smsConfig = (AliyunSmsProviderConfig) config
                        .getConfig();
                    smsConfig.setAccessKeySecret(
                        EncryptContextHelp.decrypt(smsConfig.getAccessKeySecret()));
                    return config;
                }
                //腾讯
                else if (SmsProvider.TENCENT.equals(provider)) {
                    TencentSmsProviderConfig smsConfig = (TencentSmsProviderConfig) config
                        .getConfig();
                    smsConfig.setSecretKey(EncryptContextHelp.decrypt(smsConfig.getSecretKey()));
                    return config;
                }
                //七牛
                else if (SmsProvider.QINIU.equals(provider)) {
                    QiNiuSmsProviderConfig smsConfig = (QiNiuSmsProviderConfig) config.getConfig();
                    smsConfig.setSecretKey(EncryptContextHelp.decrypt(smsConfig.getSecretKey()));
                    return config;
                }
                throw new UlpException("暂未支持此短信 [" + provider + "] 提供商配置获取");
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return new SmsConfig();
    }

    /**
     * 获取邮箱提供商配置
     *
     * @return {@link MailProviderConfig}
     */
    public static MailProviderConfig getMailProviderConfig() {
        try {
            SettingEntity setting = getSettingRepository()
                .findByName(MessageSettingConstants.MESSAGE_PROVIDER_EMAIL);
            if (!Objects.isNull(setting) && StringUtils.isNotBlank(setting.getValue())) {
                String value = setting.getValue();
                ObjectMapper objectMapper = EncryptionModule.deserializerDecrypt();
                // 指定序列化输入的类型
                objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
                    ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
                // 根据提供商序列化
                return objectMapper.readValue(value, MailProviderConfig.class);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);

        }
        return null;
    }

    /**
     * 获取登录失败持续时间
     *
     * @return {@link Integer}
     */
    public static Integer getLoginFailureDuration() {
        SettingEntity setting = getSettingRepository()
            .findByName(SECURITY_DEFENSE_POLICY_LOGIN_FAILURE_DURATION);
        if (Objects.isNull(setting)) {
            return Integer.valueOf(SecuritySettingConstants.SECURITY_DEFENSE_POLICY_DEFAULT_SETTINGS
                .get(SECURITY_DEFENSE_POLICY_LOGIN_FAILURE_DURATION));
        }
        return Integer.valueOf(setting.getValue());
    }

    /**
     * 获取连续登录失败次数
     *
     * @return {@link Integer}
     */
    public static Integer getLoginFailureCount() {
        SettingEntity setting = getSettingRepository()
            .findByName(SECURITY_DEFENSE_POLICY_FAILURE_COUNT);
        if (Objects.isNull(setting)) {
            return Integer.valueOf(SECURITY_DEFENSE_POLICY_DEFAULT_SETTINGS
                .get(SECURITY_DEFENSE_POLICY_FAILURE_COUNT));
        }
        return Integer.valueOf(setting.getValue());
    }

    /**
     * 获取验证码有效时间
     *
     * @return {@link Integer} 秒
     */
    public static Integer getCodeValidTime() {
        SettingEntity setting = getSettingRepository().findByName(VERIFY_CODE_VALID_TIME);
        if (Objects.isNull(setting)) {
            return Integer.valueOf(SECURITY_BASIC_DEFAULT_SETTINGS.get(VERIFY_CODE_VALID_TIME));
        }
        return Integer.valueOf(setting.getValue());
    }

    /**
     * 获取自动解锁时间
     *
     * @return {@link Integer} 秒
     */
    public static Integer getAutoUnlockTime() {
        SettingEntity setting = getSettingRepository()
            .findByName(SECURITY_DEFENSE_POLICY_AUTO_UNLOCK_TIME);
        if (Objects.isNull(setting)) {
            return Integer.valueOf(SECURITY_DEFENSE_POLICY_DEFAULT_SETTINGS
                .get(SECURITY_DEFENSE_POLICY_AUTO_UNLOCK_TIME));
        }
        return Integer.valueOf(setting.getValue());
    }

    /**
     * 添加 img-src host 内容安全策略
     *
     * @param value {@link String}
     */
    public static void addImgSrcHostContentSecurityPolicy(String value) {
        SettingEntity settingEntity = getSettingRepository()
            .findByName(SECURITY_DEFENSE_POLICY_CONTENT_SECURITY_POLICY);
        String contentSecurityPolicy;
        if (Objects.isNull(settingEntity)) {
            settingEntity = new SettingEntity();
            settingEntity.setName(SECURITY_DEFENSE_POLICY_CONTENT_SECURITY_POLICY);
            contentSecurityPolicy = SECURITY_DEFENSE_POLICY_DEFAULT_SETTINGS
                .get(SECURITY_DEFENSE_POLICY_CONTENT_SECURITY_POLICY);
        } else {
            contentSecurityPolicy = settingEntity.getValue();
        }
        Policy parse = ContentSecurityPolicyUtils.parse(contentSecurityPolicy);
        SourceExpressionDirective directive = parse.getFetchDirective(FetchDirectiveKind.ImgSrc)
            .orElseThrow();
        Host host = Host.parseHost(value).orElseThrow();
        directive.removeHost(host);
        directive.addHost(host, (severity, message) -> {
            logger.error("添加操作内容安全策略 img-src host 异常：{}", message);
            throw new UlpException(message);
        });
        settingEntity.setValue(parse.toString());
        getSettingRepository().save(settingEntity);
        ApplicationContextService.refresh(DEFAULT_SECURITY_FILTER_CHAIN);
    }

    /**
     * 获取控制台基础网址
     *
     * @return {@link  String}
     */
    public static String getConsolePublicBaseUrl() {
        return getSupportProperties().getServer().getConsolePublicBaseUrl();
    }

    /**
     * 获取门户基础网址
     *
     * @return {@link  String}
     */
    public static String getPortalPublicBaseUrl() {
        return getSupportProperties().getServer().getPortalPublicBaseUrl();
    }

    /**
     * 获取门户登录网址
     *
     * @return {@link  String}
     */
    public static String getPortalLoginUrl() {
        return getSupportProperties().getServer().getPortalPublicBaseUrl() + FE_LOGIN;
    }

    public static SettingRepository getSettingRepository() {
        return ApplicationContextService.getBean(SettingRepository.class);
    }

    public static SupportProperties getSupportProperties() {
        return ApplicationContextService.getBean(SupportProperties.class);
    }
}
