/*
 * ulp-application-core - United Login Platform
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
package cn.frank.ulp.application;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.IdGenerator;

import cn.frank.ulp.common.entity.app.AppAccountEntity;
import cn.frank.ulp.common.entity.app.AppEntity;
import cn.frank.ulp.common.entity.app.AppGroupAssociationEntity;
import cn.frank.ulp.common.enums.app.AuthorizationType;
import cn.frank.ulp.common.exception.app.AppAccountNotExistException;
import cn.frank.ulp.common.repository.app.AppAccountRepository;
import cn.frank.ulp.common.repository.app.AppGroupAssociationRepository;
import cn.frank.ulp.common.repository.app.AppRepository;

import lombok.extern.slf4j.Slf4j;

import tools.jackson.databind.ObjectMapper;

/**
 * AbstractApplicationService
 *
 * @author Frank Zhang
 */
@Slf4j
public abstract class AbstractApplicationService implements ApplicationService {

    protected final ObjectMapper mapper = new ObjectMapper();

    /**
     * 获取默认应用账户
     *
     * @param appId {@link String}
     * @param userId {@link String}
     * @return {@link AppAccount} AppAccount
     */
    @Override
    public AppAccount getDefaultAppAccount(String appId, String userId) {
        AppAccountEntity entity = appAccountRepository
            .findByAppIdAndUserIdAndDefaultedIsTrue(appId, userId)
            .orElseThrow(AppAccountNotExistException::new);
        AppAccount account = new AppAccount();
        account.setAppId(entity.getAppId());
        account.setPassword(entity.getPassword());
        account.setAccount(entity.getAccount());
        return account;
    }

    /**
     * 创建应用
     *
     * @param name              {@link String}
     * @param icon              {@link String}
     * @param remark            {@link String}
     * @param authorizationType {@link AuthorizationType}
     * @return {@link AppEntity}
     */
    @Override
    public AppEntity createApp(String name, String icon, String remark,
                               AuthorizationType authorizationType) {
        AppEntity appEntity = new AppEntity();
        appEntity.setName(name);
        appEntity.setIcon(icon);
        appEntity.setCode(RandomStringUtils.randomAlphanumeric(32).toLowerCase());
        appEntity.setTemplate(getCode());
        appEntity.setType(getType());
        appEntity.setEnabled(true);
        appEntity.setConfigured(false);
        appEntity.setProtocol(getProtocol());
        appEntity.setClientId(idGenerator.generateId().toString().replace("-", ""));
        appEntity.setClientSecret(idGenerator.generateId().toString().replace("-", ""));
        appEntity.setAuthorizationType(authorizationType);
        appEntity.setRemark(remark);
        return appRepository.save(appEntity);
    }

    @Override
    public AppEntity createApp(String name, String icon, String remark, List<String> groups,
                               AuthorizationType authorizationType) {

        AppEntity appEntity = createApp(name, icon, remark, authorizationType);
        List<AppGroupAssociationEntity> list = new ArrayList<>();
        for (String id : groups) {
            AppGroupAssociationEntity appGroupAssociationEntity = new AppGroupAssociationEntity();
            appGroupAssociationEntity.setGroupId(id);
            appGroupAssociationEntity.setApp(appEntity);
            list.add(appGroupAssociationEntity);
        }
        appGroupAssociationRepository.saveAll(list);
        return appEntity;
    }

    /**
     * AppAccountRepository
     */
    protected final AppAccountRepository          appAccountRepository;

    /**
     * ApplicationRepository
     */
    protected final AppRepository                 appRepository;

    /**
     * AppGroupAssociationRepository
     */
    protected final AppGroupAssociationRepository appGroupAssociationRepository;

    /**
     * IdGenerator
     */
    protected final IdGenerator                   idGenerator;

    protected AbstractApplicationService(AppAccountRepository appAccountRepository,
                                         AppGroupAssociationRepository appGroupAssociationRepository,
                                         AppRepository appRepository) {
        this.appAccountRepository = appAccountRepository;
        this.appGroupAssociationRepository = appGroupAssociationRepository;
        this.appRepository = appRepository;
        this.idGenerator = new AlternativeJdkIdGenerator();
    }
}
