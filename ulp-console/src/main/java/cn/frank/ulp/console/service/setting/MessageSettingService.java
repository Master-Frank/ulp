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
package cn.frank.ulp.console.service.setting;

import cn.frank.ulp.console.pojo.result.setting.EmailProviderConfigResult;
import cn.frank.ulp.console.pojo.result.setting.SmsProviderConfigResult;
import cn.frank.ulp.console.pojo.save.setting.MailProviderSaveParam;
import cn.frank.ulp.console.pojo.save.setting.SmsProviderSaveParam;

/**
 * 消息设置接口
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/10/1 21:19
 */
public interface MessageSettingService extends SettingService {
    /**
     * 保存配置
     *
     * @param param {@link MailProviderSaveParam}
     * @return {@link Boolean}
     */
    Boolean saveMailProviderConfig(MailProviderSaveParam param);

    /**
     * 保存邮件验证配置
     *
     * @param param {@link SmsProviderSaveParam}
     * @return {@link Boolean}
     */
    Boolean saveSmsProviderConfig(SmsProviderSaveParam param);

    /**
     * 禁用短信验证服务
     *
     * @return {@link Boolean}
     */
    Boolean disableSmsProvider();

    /**
     * 禁用邮件提供商
     *
     * @return {@link Boolean}
     */
    Boolean disableMailProvider();

    /**
     * 获取邮件提供商配置
     *
     * @return {@link EmailProviderConfigResult}
     */
    EmailProviderConfigResult getMailProviderConfig();

    /**
     * 获取短信验证服务配置
     *
     * @return {@link SmsProviderConfigResult}
     */
    SmsProviderConfigResult getSmsProviderConfig();
}
