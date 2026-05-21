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

import java.util.List;

import cn.frank.ulp.common.entity.setting.MailTemplateEntity;
import cn.frank.ulp.common.enums.MailType;
import cn.frank.ulp.console.pojo.result.setting.EmailTemplateListResult;
import cn.frank.ulp.console.pojo.result.setting.EmailTemplateResult;
import cn.frank.ulp.console.pojo.save.setting.EmailCustomTemplateSaveParam;

/**
 * <p>
 * 邮件模板 服务类
 * </p>
 *
 * @author Frank Zhang
 */
public interface MailTemplateService extends SettingService {
    /**
     * 根据邮件模板类型获取配置
     *
     * @param type {@link MailType}
     * @return SettingMailTemplateEntity
     */
    MailTemplateEntity getEmailTemplateByType(MailType type);

    /**
     * 添加邮件模板
     *
     * @param type  {@link MailType}
     * @param param {@link EmailCustomTemplateSaveParam}
     * @return SettingMailTemplateEntity
     */
    MailTemplateEntity saveCustomEmailTemplate(MailType type, EmailCustomTemplateSaveParam param);

    /**
     * 邮件模板详情
     *
     * @param templateType {@link MailType}
     * @return {@link EmailTemplateResult}
     */
    EmailTemplateResult getEmailTemplate(MailType templateType);

    /**
     * 获取邮件模板列表
     *
     * @return {@link List}
     */
    List<EmailTemplateListResult> getEmailTemplateList();

    /**
     * 禁用自定义模块
     *
     * @param type {@link MailType}
     */
    void disableCustomEmailTemplate(MailType type);
}
