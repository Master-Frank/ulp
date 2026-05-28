/*
 * ulp-openapi - United Login Platform
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
package cn.frank.ulp.openapi.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import cn.frank.ulp.audit.context.AuditContext;
import cn.frank.ulp.audit.entity.Target;
import cn.frank.ulp.audit.enums.TargetType;
import cn.frank.ulp.common.entity.app.AppAccountEntity;
import cn.frank.ulp.common.entity.app.po.AppAccountPO;
import cn.frank.ulp.common.entity.app.query.AppAccountQueryParam;
import cn.frank.ulp.common.exception.app.AppAccountExistException;
import cn.frank.ulp.common.exception.app.AppDefaultAccountExistException;
import cn.frank.ulp.common.repository.app.AppAccountRepository;
import cn.frank.ulp.openapi.converter.AppAccountConverter;
import cn.frank.ulp.openapi.pojo.query.OapiV1AppAccountQuery;
import cn.frank.ulp.openapi.pojo.result.AppAccountListResult;
import cn.frank.ulp.openapi.pojo.save.AppAccountCreateParam;
import cn.frank.ulp.openapi.service.AppAccountService;
import cn.frank.ulp.support.exception.UlpException;
import cn.frank.ulp.support.repository.page.domain.Page;
import cn.frank.ulp.support.repository.page.domain.PageModel;
import cn.frank.ulp.support.security.util.SecurityUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 应用账户
 *
 * @author Frank Zhang
 */
@Service
@Slf4j
@AllArgsConstructor
public class AppAccountServiceImpl implements AppAccountService {

    /**
     * 查询应用账户
     *
     * @param pageModel {@link PageModel}
     * @param query     {@link  OapiV1AppAccountQuery}
     * @return {@link Page}
     */
    @Override
    public Page<AppAccountListResult> getAppAccountList(PageModel pageModel,
                                                        OapiV1AppAccountQuery query) {
        //@formatter:off
        AppAccountQueryParam param = appAccountConverter.appAccountQueryToQueryParam(query);
        //@formatter:on
        //分页条件
        PageRequest request = PageRequest.of(pageModel.getCurrent(), pageModel.getPageSize());
        //查询映射
        org.springframework.data.domain.Page<AppAccountPO> list = appAccountRepository
            .getAppAccountList(param, request);
        return appAccountConverter.appAccountEntityConvertToAppAccountResult(list);
    }

    /**
     * 新增应用账户
     *
     * @param param {@link AppAccountCreateParam}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createAppAccount(AppAccountCreateParam param) {
        List<AppAccountEntity> appAccounts = appAccountRepository
            .findByAppIdAndUserId(param.getAppId(), SecurityUtils.getCurrentUserId());
        if (!CollectionUtils.isEmpty(appAccounts)) {
            if (appAccounts.stream()
                .anyMatch(appAccount -> appAccount.getAccount().equals(param.getAccount()))) {
                throw new AppAccountExistException();
            }
            if (param.getDefaulted()
                && appAccounts.stream().anyMatch(AppAccountEntity::getDefaulted)) {
                throw new AppDefaultAccountExistException();
            }
        }
        AppAccountEntity entity = appAccountConverter.appAccountCreateParamConvertToEntity(param);
        appAccountRepository.save(entity);
        AuditContext.setTarget(Target.builder().id(entity.getId()).name("")
            .type(TargetType.APPLICATION_ACCOUNT).build());
        return true;
    }

    /**
     * 删除应用账户
     *
     * @param id {@link Long}
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
            throw new UlpException(AuditContext.getContent());
        }
        appAccountRepository.deleteById(id);
        AuditContext.setTarget(Target.builder().id(id).name(optional.get().getAccount())
            .type(TargetType.APPLICATION_ACCOUNT).build());
        return true;
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
