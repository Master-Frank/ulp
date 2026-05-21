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
package cn.frank.ulp.console.service.app.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.frank.ulp.audit.context.AuditContext;
import cn.frank.ulp.audit.entity.Target;
import cn.frank.ulp.audit.enums.TargetType;
import cn.frank.ulp.common.entity.app.AppAccessPolicyEntity;
import cn.frank.ulp.common.entity.app.AppEntity;
import cn.frank.ulp.common.entity.app.po.AppAccessPolicyPO;
import cn.frank.ulp.common.entity.app.query.AppAccessPolicyQueryParam;
import cn.frank.ulp.common.repository.account.OrganizationRepository;
import cn.frank.ulp.common.repository.account.UserGroupRepository;
import cn.frank.ulp.common.repository.account.UserRepository;
import cn.frank.ulp.common.repository.app.AppAccessPolicyRepository;
import cn.frank.ulp.common.repository.app.AppRepository;
import cn.frank.ulp.console.converter.app.AppAccessPolicyConverter;
import cn.frank.ulp.console.pojo.query.app.AppAccessPolicyQuery;
import cn.frank.ulp.console.pojo.result.app.AppAccessPolicyResult;
import cn.frank.ulp.console.pojo.save.app.AppAccessPolicyCreateParam;
import cn.frank.ulp.console.service.app.AppAccessPolicyService;
import cn.frank.ulp.support.exception.TopIamException;
import cn.frank.ulp.support.repository.page.domain.Page;
import cn.frank.ulp.support.repository.page.domain.PageModel;
import cn.frank.ulp.support.util.BeanUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static cn.frank.ulp.support.repository.base.BaseEntity.LAST_MODIFIED_BY;
import static cn.frank.ulp.support.repository.base.BaseEntity.LAST_MODIFIED_TIME;

/**
 * 应用访问权限策略 Service
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/6/4 21:56
 */
@Service
@Slf4j
@AllArgsConstructor
public class AppAccessPolicyServiceImpl implements AppAccessPolicyService {
    /**
     * 查询应用授权策略列表
     *
     * @param pageModel {@link PageModel}
     * @param query     {@link AppAccessPolicyQuery}
     * @return {@link Page}
     */
    @Override
    public Page<AppAccessPolicyResult> getAppAccessPolicyList(PageModel pageModel,
                                                              AppAccessPolicyQuery query) {
        //@formatter:off
        AppAccessPolicyQueryParam param = appAccessPolicyConverter.appAccessPolicyQueryToQueryParam(query);
        //@formatter:on
        //分页条件
        PageRequest request = PageRequest.of(pageModel.getCurrent(), pageModel.getPageSize());
        //查询映射
        org.springframework.data.domain.Page<AppAccessPolicyPO> list = appAccessPolicyRepository
            .getAppPolicyList(param, request);
        return appAccessPolicyConverter.appPolicyEntityListConvertToAppPolicyResult(list);
    }

    /**
     * 创建应用授权策略
     *
     * @param param {@link AppAccessPolicyCreateParam}
     * @return @{link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createAppAccessPolicy(AppAccessPolicyCreateParam param) {
        List<AppAccessPolicyEntity> list = appAccessPolicyConverter
            .appPolicyCreateParamConvertToEntity(param);
        //判断是否已经存在
        for (AppAccessPolicyEntity policy : list) {
            Optional<AppAccessPolicyEntity> policyEntity = appAccessPolicyRepository
                .findByAppIdAndSubjectIdAndSubjectType(policy.getAppId(), policy.getSubjectId(),
                    policy.getSubjectType());
            if (policyEntity.isEmpty()) {
                appAccessPolicyRepository.save(policy);
                setAuditTarget(policy);
                continue;
            }
            AppAccessPolicyEntity entity = policyEntity.get();
            BeanUtils.merge(policy, entity, LAST_MODIFIED_TIME, LAST_MODIFIED_BY);
            appAccessPolicyRepository.save(entity);
            setAuditTarget(entity);
        }
        return true;
    }

    /**
     * 删除应用授权策略
     *
     * @param id {@link  String}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteAppAccessPolicy(String id) {
        Optional<AppAccessPolicyEntity> optional = appAccessPolicyRepository.findById(id);
        //策略不存在
        if (optional.isEmpty()) {
            AuditContext.setContent("删除失败，应用授权策略不存在");
            log.warn(AuditContext.getContent());
            throw new TopIamException(AuditContext.getContent());
        }
        AppAccessPolicyEntity entity = optional.get();
        appAccessPolicyRepository.deleteById(id);
        setAuditTarget(entity);
        return true;
    }

    /**
     * 启用应用访问授权
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Override
    public Boolean enableAppAccessPolicy(String id) {
        Optional<AppAccessPolicyEntity> optional = appAccessPolicyRepository.findById(id);
        //策略不存在
        if (optional.isEmpty()) {
            AuditContext.setContent("启用失败，应用授权策略不存在");
            log.warn(AuditContext.getContent());
            throw new TopIamException(AuditContext.getContent());
        }
        Integer count = appAccessPolicyRepository.updateStatus(id, Boolean.TRUE);
        AppAccessPolicyEntity entity = optional.get();
        setAuditTarget(entity);
        return count > 0;
    }

    /**
     * 禁用应用访问授权
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Override
    public Boolean disableAppAccessPolicy(String id) {
        Optional<AppAccessPolicyEntity> optional = appAccessPolicyRepository.findById(id);
        //策略不存在
        if (optional.isEmpty()) {
            AuditContext.setContent("禁用失败，应用授权策略不存在");
            log.warn(AuditContext.getContent());
            throw new TopIamException(AuditContext.getContent());
        }
        AppAccessPolicyEntity entity = optional.get();
        Integer count = appAccessPolicyRepository.updateStatus(id, Boolean.FALSE);
        setAuditTarget(entity);
        return count > 0;
    }

    private void setAuditTarget(AppAccessPolicyEntity entity) {
        Optional<AppEntity> appEntityOptional = appRepository.findById(entity.getAppId());
        appEntityOptional.ifPresent(appEntity -> AuditContext.addTarget(Target.builder()
            .id(entity.getAppId()).name(appEntity.getName()).type(TargetType.APPLICATION).build()));

        switch (entity.getSubjectType()) {
            case USER -> userRepository.findById(entity.getSubjectId()).ifPresent(
                userEntity -> AuditContext.addTarget(Target.builder().id(entity.getSubjectId())
                    .name(userEntity.getUsername()).type(TargetType.USER).build()));
            case USER_GROUP -> userGroupRepository.findById(entity.getSubjectId()).ifPresent(
                groupEntity -> AuditContext.addTarget(Target.builder().id(entity.getSubjectId())
                    .name(groupEntity.getName()).type(TargetType.USER_GROUP).build()));
            case ORGANIZATION ->
                organizationRepository.findById(entity.getSubjectId())
                    .ifPresent(organizationEntity -> AuditContext.addTarget(Target.builder()
                        .id(entity.getSubjectId()).name(organizationEntity.getName())
                        .type(TargetType.ORGANIZATION).build()));
        }
    }

    /**
     * AppPolicyConverter
     */
    private final AppAccessPolicyConverter  appAccessPolicyConverter;

    /**
     * AppPolicyRepository
     */
    private final AppAccessPolicyRepository appAccessPolicyRepository;

    /**
     * UserRepository
     */
    private final UserRepository            userRepository;

    /**
     * OrganizationRepository
     */
    private final OrganizationRepository    organizationRepository;

    /**
     * UserGroupRepository
     */
    private final UserGroupRepository       userGroupRepository;

    /**
     * AppRepository
     */
    private final AppRepository             appRepository;

}
