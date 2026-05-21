/*
 * ulp-portal - United Login Platform
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
package cn.frank.ulp.portal.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.frank.ulp.application.AppAccount;
import cn.frank.ulp.audit.context.AuditContext;
import cn.frank.ulp.audit.entity.Target;
import cn.frank.ulp.audit.enums.TargetType;
import cn.frank.ulp.common.entity.app.AppAccountEntity;
import cn.frank.ulp.common.exception.app.AppAccountExistException;
import cn.frank.ulp.common.exception.app.AppAccountNotExistException;
import cn.frank.ulp.common.exception.app.AppDefaultAccountExistException;
import cn.frank.ulp.common.jackjson.encrypt.EncryptContextHelp;
import cn.frank.ulp.common.repository.app.AppAccountRepository;
import cn.frank.ulp.portal.converter.AppAccountConverter;
import cn.frank.ulp.portal.pojo.request.AppAccountRequest;
import cn.frank.ulp.portal.service.AppAccountService;
import cn.frank.ulp.support.exception.TopIamException;
import cn.frank.ulp.support.security.util.SecurityUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 应用账户
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/8/25 21:07
 */
@Service
@Slf4j
@AllArgsConstructor
public class AppAccountServiceImpl implements AppAccountService {

    /**
     * 新增应用账户
     *
     * @param param {@link AppAccountRequest}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createAppAccount(AppAccountRequest param) {
        Optional<AppAccountEntity> appAccount = appAccountRepository.findByAppIdAndUserIdAndAccount(
            param.getAppId(), SecurityUtils.getCurrentUserId(), param.getAccount());
        if (Objects.nonNull(appAccount)) {
            throw new AppAccountExistException();
        }
        AppAccountEntity entity = appAccountConverter.appAccountRequestConvertToEntity(param);
        //密码不为空
        if (!StringUtils.isBlank(param.getPassword())) {
            Base64 base64 = new Base64();
            String password = new String(base64.decode(param.getPassword()),
                StandardCharsets.UTF_8);
            entity.setPassword(EncryptContextHelp.encrypt(password));
        }
        appAccountRepository.save(entity);
        AuditContext.setTarget(
            Target.builder().id(entity.getUserId()).name("").type(TargetType.USER).build(),
            Target.builder().id(entity.getAccount()).name("").type(TargetType.APPLICATION_ACCOUNT)
                .build(),
            Target.builder().id(entity.getAppId()).name("").type(TargetType.APPLICATION).build());
        return true;
    }

    /**
     * 删除应用账户
     *
     * @param id {@link String}
     * @return {@link String}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteAppAccount(String id) {
        Optional<AppAccountEntity> optional = appAccountRepository.findById(id);
        //管理员不存在
        if (optional.isEmpty()) {
            AuditContext.setContent("删除失败，应用账户不存在");
            log.warn(AuditContext.getContent());
            throw new TopIamException(AuditContext.getContent());
        }
        appAccountRepository.deleteById(id);
        AuditContext.setTarget(
            Target.builder().id(optional.get().getId()).name("").type(TargetType.USER).build(),
            Target.builder().id(optional.get().getAppId()).name("").type(TargetType.APPLICATION)
                .build());
        return true;
    }

    @Override
    public List<AppAccount> getAppAccountList(String appId) {
        List<AppAccountEntity> appAccountList = appAccountRepository.findByAppIdAndUserId(appId,
            SecurityUtils.getCurrentUserId());
        return appAccountList.stream().map(entity -> {
            AppAccount account = new AppAccount();
            account.setAppId(entity.getAppId());
            account.setAccount(entity.getAccount());
            account.setPassword(EncryptContextHelp.decrypt(entity.getPassword()));
            account.setDefaulted(entity.getDefaulted());
            return account;
        }).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateAppAccountDefault(String id, Boolean defaulted) {
        AppAccountEntity appAccount = appAccountRepository.findById(id)
            .orElseThrow(AppAccountNotExistException::new);
        if (defaulted.equals(appAccount.getDefaulted())
            || (!defaulted && appAccount.getDefaulted() == null)) {
            return Boolean.TRUE;
        }
        if (defaulted) {
            appAccountRepository.findByAppIdAndUserIdAndDefaultedIsTrue(appAccount.getAppId(),
                appAccount.getUserId()).ifPresent(defaultAccount -> {
                    throw new AppDefaultAccountExistException();
                });
        }
        appAccount.setDefaulted(Boolean.FALSE.equals(defaulted) ? null : Boolean.TRUE);
        appAccountRepository.save(appAccount);
        AuditContext.setTarget(
            Target.builder().id(appAccount.getUserId()).name("").type(TargetType.USER).build(),
            Target.builder().id(appAccount.getAccount()).name("")
                .type(TargetType.APPLICATION_ACCOUNT).build(),
            Target.builder().id(appAccount.getAppId()).name("").type(TargetType.APPLICATION)
                .build());
        return Boolean.TRUE;
    }

    /**
     * AppAccountConverter
     */
    private final AppAccountConverter  appAccountConverter;

    /**
     * AppAccountRepository
     */
    private final AppAccountRepository appAccountRepository;
}
