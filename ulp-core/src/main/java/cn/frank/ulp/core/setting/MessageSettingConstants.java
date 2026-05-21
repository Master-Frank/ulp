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
package cn.frank.ulp.core.setting;

import cn.frank.ulp.common.constant.SettingConstants;
import static org.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;

/**
 * 消息设置常量
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/12/5 21:53
 */
public final class MessageSettingConstants {
    /**
     * 消息提供商前缀
     */
    public static final String MESSAGE_SETTING_PREFIX            = "message.setting.";
    /**
     * 邮件提供商key
     */
    public static final String MESSAGE_PROVIDER_EMAIL            = MESSAGE_SETTING_PREFIX
                                                                   + "email_provider";

    /**
     * 短信验证服务key
     */
    public static final String MESSAGE_SMS_PROVIDER              = MESSAGE_SETTING_PREFIX
                                                                   + "sms_provider";

    /**
     * 邮件内容路径
     */
    public final static String MAIL_CONTENT_PATH                 = CLASSPATH_URL_PREFIX
                                                                   + "mail/content/";

    /**
     * 系统设置电子邮件缓存 cacheName
     */
    public static final String SETTING_EMAIL_CACHE_NAME          = SettingConstants.SETTING_CACHE_NAME
                                                                   + ":email";

    /**
     * 系统设置电子邮件模板缓存 cacheName
     */
    public static final String SETTING_EMAIL_TEMPLATE_CACHE_NAME = SETTING_EMAIL_CACHE_NAME
                                                                   + ":template";

    /**
     * 系统设置电子邮件服务商缓存 cacheName
     */
    public static final String SETTING_EMAIL_PROVIDER_CACHE_NAME = SETTING_EMAIL_CACHE_NAME
                                                                   + ":provider";

    /**
     * 系统设置短信服务商缓存 cacheName
     */
    public static final String SETTING_SMS_PROVIDER_CACHE_NAME   = SettingConstants.SETTING_CACHE_NAME
                                                                   + ":sms:provider";

    /**
     * 系统设置短信模板缓存 cacheName
     */
    public static final String SETTING_SMS_TEMPLATE_CACHE_NAME   = SettingConstants.SETTING_CACHE_NAME
                                                                   + "sms:template";
}
