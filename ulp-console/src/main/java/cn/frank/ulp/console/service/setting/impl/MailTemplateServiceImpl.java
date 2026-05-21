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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.frank.ulp.audit.context.AuditContext;
import cn.frank.ulp.audit.entity.Target;
import cn.frank.ulp.audit.enums.TargetType;
import cn.frank.ulp.common.entity.setting.MailTemplateEntity;
import cn.frank.ulp.common.enums.MailType;
import cn.frank.ulp.common.repository.setting.MailTemplateRepository;
import cn.frank.ulp.common.repository.setting.SettingRepository;
import cn.frank.ulp.console.converter.setting.MailTemplateConverter;
import cn.frank.ulp.console.pojo.result.setting.EmailTemplateListResult;
import cn.frank.ulp.console.pojo.result.setting.EmailTemplateResult;
import cn.frank.ulp.console.pojo.save.setting.EmailCustomTemplateSaveParam;
import cn.frank.ulp.console.service.setting.MailTemplateService;
import cn.frank.ulp.support.context.ApplicationContextService;
import cn.frank.ulp.support.util.BeanUtils;
import static cn.frank.ulp.core.setting.MessageSettingConstants.SETTING_EMAIL_TEMPLATE_CACHE_NAME;
import static cn.frank.ulp.support.repository.base.BaseEntity.LAST_MODIFIED_BY;
import static cn.frank.ulp.support.repository.base.BaseEntity.LAST_MODIFIED_TIME;

/**
 * <p>
 * 邮件模板 服务实现类
 * </p>
 *
 * @author Frank Zhang
 */
@Service
@CacheConfig(cacheNames = SETTING_EMAIL_TEMPLATE_CACHE_NAME)
public class MailTemplateServiceImpl extends SettingServiceImpl implements MailTemplateService {

    /**
     * Logger
     */
    private final Logger logger = LoggerFactory.getLogger(MailTemplateServiceImpl.class);

    /**
     * 根据邮件模板类型获取配置
     *
     * @param type {@link MailType}
     * @return SettingMailTemplateEntity
     */
    @Override
    @Cacheable(key = "#type.code", unless = "#result == null")
    public MailTemplateEntity getEmailTemplateByType(MailType type) {
        return mailTemplateRepository.findByType(type);
    }

    /**
     * 添加邮件模板
     *
     * @param type {@link MailType}
     * @return SettingMailTemplateEntity
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CachePut(key = "#result.type.code", unless = "#result == null")
    public MailTemplateEntity saveCustomEmailTemplate(MailType type,
                                                      EmailCustomTemplateSaveParam param) {
        //入参映射为实体
        MailTemplateEntity entity = messageDataConverter
            .emailTemplateConfigSaveParamConvertToEntity(param);
        entity.setType(type);
        //查询数据库
        MailTemplateEntity template = getSelf().getEmailTemplateByType(type);
        if (!Objects.isNull(template)) {
            BeanUtils.merge(entity, template, LAST_MODIFIED_BY, LAST_MODIFIED_TIME);
            return mailTemplateRepository.save(template);
        }
        entity = mailTemplateRepository.save(entity);
        AuditContext.setTarget(Target.builder().id(entity.getId()).name(entity.getType().getName())
            .type(TargetType.MAIL_TEMPLATE).build());
        return entity;
    }

    /**
     * 邮件模板详情
     *
     * @param templateType {@link MailType}
     * @return {@link EmailTemplateResult}
     */
    @Override
    public EmailTemplateResult getEmailTemplate(MailType templateType) {
        //构建查询条件
        //查询
        MailTemplateEntity template = getSelf().getEmailTemplateByType(templateType);
        //如果数据库为空，查找默认模板
        EmailTemplateResult result;
        if (Objects.isNull(template)) {
            result = messageDataConverter
                .mailTemplateTypeConvertToEmailTemplateDetailResult(templateType);
        }
        //查询数据库
        else {
            result = messageDataConverter.entityConvertToEmailTemplateDetailResult(template);
        }
        return result;
    }

    /**
     * 获取邮件模板列表
     * 系统自带邮件模板及内容存入枚举，
     * 如果用户进行了自定义模板内容，将会把自定义的存入数据库，使用用户自定义的
     *
     * @return {@link List}
     */
    @Override
    public List<EmailTemplateListResult> getEmailTemplateList() {
        //从枚举类获取信息
        List<MailType> values = Arrays.asList(MailType.values());
        //从数据库获取
        List<MailTemplateEntity> lists = mailTemplateRepository.findAll();
        return messageDataConverter.mailTemplateTypeConvertToEmailTemplateListResult(values, lists);
    }

    /**
     * 禁用自定义模块
     *
     * @param type {@link MailType}
     */
    @Override
    @CacheEvict(key = "#type.code")
    public void disableCustomEmailTemplate(MailType type) {
        mailTemplateRepository.deleteByType(type);
    }

    private MailTemplateService getSelf() {
        return ApplicationContextService.getBean(MailTemplateService.class);
    }

    /**
     * 消息服务数据映射
     */
    private final MailTemplateConverter  messageDataConverter;
    /**
     * SettingMailTemplateRepository
     */
    private final MailTemplateRepository mailTemplateRepository;

    public MailTemplateServiceImpl(SettingRepository settingsRepository,
                                   MailTemplateConverter messageDataConverter,
                                   MailTemplateRepository mailTemplateRepository) {
        super(settingsRepository);
        this.messageDataConverter = messageDataConverter;
        this.mailTemplateRepository = mailTemplateRepository;
    }
}
