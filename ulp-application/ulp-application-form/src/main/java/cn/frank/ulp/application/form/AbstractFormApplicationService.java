/*
 * ulp-application-form - United Login Platform
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
package cn.frank.ulp.application.form;

import java.util.ArrayList;
import java.util.List;

import cn.frank.ulp.application.AbstractApplicationService;
import cn.frank.ulp.application.form.model.FormProtocolConfig;
import cn.frank.ulp.common.entity.app.AppFormConfigEntity;
import cn.frank.ulp.common.entity.app.po.AppFormConfigPO;
import cn.frank.ulp.common.repository.app.AppAccountRepository;
import cn.frank.ulp.common.repository.app.AppFormConfigRepository;
import cn.frank.ulp.common.repository.app.AppGroupAssociationRepository;
import cn.frank.ulp.common.repository.app.AppRepository;

/**
 * Form 应用配置
 *
 * @author Frank Zhang
 */
public abstract class AbstractFormApplicationService extends AbstractApplicationService
                                                     implements FormApplicationService {

    @Override
    public void delete(String appId) {
        //删除应用
        appRepository.deleteById(appId);
        //删除应用账户
        appAccountRepository.deleteAllByAppId(appId);
        // 删除应用配置
        appFormConfigRepository.deleteByAppId(appId);
    }

    @Override
    public FormProtocolConfig getProtocolConfig(String appCode) {
        AppFormConfigPO configPo = appFormConfigRepository.findByAppCode(appCode);

        FormProtocolConfig.FormProtocolConfigBuilder<?, ?> configBuilder = FormProtocolConfig
            .builder();
        if (configPo.getAppId() != null) {
            configBuilder.appId(String.valueOf(configPo.getAppId()));
        }

        configBuilder.clientId(configPo.getClientId());
        configBuilder.clientSecret(configPo.getClientSecret());
        configBuilder.appCode(configPo.getAppCode());
        configBuilder.appName(configPo.getAppName());
        configBuilder.appTemplate(configPo.getAppTemplate());
        configBuilder.loginUrl(configPo.getLoginUrl());
        configBuilder.usernameField(configPo.getUsernameField());
        configBuilder.passwordField(configPo.getPasswordField());
        configBuilder.usernameEncryptType(configPo.getUsernameEncryptType());
        configBuilder.usernameEncryptKey(configPo.getUsernameEncryptKey());
        configBuilder.passwordEncryptType(configPo.getPasswordEncryptType());
        configBuilder.passwordEncryptKey(configPo.getPasswordEncryptKey());
        configBuilder.submitType(configPo.getSubmitType());
        List<AppFormConfigEntity.OtherField> list = configPo.getOtherField();
        if (list != null) {
            configBuilder.otherField(new ArrayList<>(list));
        }
        configBuilder.configured(configPo.getConfigured());
        return configBuilder.build();
    }

    /**
     * AppFormConfigRepository
     */
    protected final AppFormConfigRepository appFormConfigRepository;

    protected AbstractFormApplicationService(AppRepository appRepository,
                                             AppGroupAssociationRepository appGroupAssociationRepository,
                                             AppAccountRepository appAccountRepository,
                                             AppFormConfigRepository appFormConfigRepository) {
        super(appAccountRepository, appGroupAssociationRepository, appRepository);
        this.appFormConfigRepository = appFormConfigRepository;
    }
}
