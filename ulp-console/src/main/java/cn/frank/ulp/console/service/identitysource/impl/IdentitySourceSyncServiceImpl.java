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
package cn.frank.ulp.console.service.identitysource.impl;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import cn.frank.ulp.audit.context.AuditContext;
import cn.frank.ulp.audit.entity.Target;
import cn.frank.ulp.common.entity.identitysource.IdentitySourceEntity;
import cn.frank.ulp.common.entity.identitysource.IdentitySourceSyncHistoryEntity;
import cn.frank.ulp.common.entity.identitysource.IdentitySourceSyncRecordEntity;
import cn.frank.ulp.common.repository.identitysource.IdentitySourceSyncHistoryRepository;
import cn.frank.ulp.common.repository.identitysource.IdentitySourceSyncRecordRepository;
import cn.frank.ulp.console.converter.identitysource.IdentitySourceSyncConverter;
import cn.frank.ulp.console.pojo.query.identity.IdentitySourceSyncHistoryListQuery;
import cn.frank.ulp.console.pojo.query.identity.IdentitySourceSyncRecordListQuery;
import cn.frank.ulp.console.pojo.result.identitysource.IdentitySourceSyncHistoryListResult;
import cn.frank.ulp.console.pojo.result.identitysource.IdentitySourceSyncRecordListResult;
import cn.frank.ulp.console.service.identitysource.IdentitySourceService;
import cn.frank.ulp.console.service.identitysource.IdentitySourceSyncService;
import cn.frank.ulp.identitysource.core.event.IdentitySourceEventUtils;
import cn.frank.ulp.identitysource.core.exception.IdentitySourceNotExistException;
import cn.frank.ulp.support.repository.page.domain.Page;
import cn.frank.ulp.support.repository.page.domain.PageModel;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static cn.frank.ulp.audit.enums.TargetType.IDENTITY_SOURCE;

/**
 * 同步身份源同步
 *
 * @author Frank Zhang
 */
@Slf4j
@Service
@AllArgsConstructor
public class IdentitySourceSyncServiceImpl implements IdentitySourceSyncService {
    /**
     * 查询身份源同步列表
     *
     * @param query     {@link  IdentitySourceSyncHistoryListQuery}
     * @param pageModel {@link  PageModel}
     * @return {@link  IdentitySourceSyncRecordListResult}
     */
    @Override
    public Page<IdentitySourceSyncHistoryListResult> getIdentitySourceSyncHistoryList(IdentitySourceSyncHistoryListQuery query,
                                                                                      PageModel pageModel) {
        //查询条件
        Specification<IdentitySourceSyncHistoryEntity> specification = identitySourceSyncConverter
            .queryIdentitySourceSyncHistoryListQueryConvertToSpecification(query);
        PageRequest request = PageRequest.of(pageModel.getCurrent(), pageModel.getPageSize());
        //查询映射
        org.springframework.data.domain.Page<IdentitySourceSyncHistoryEntity> list = identitySourceSyncHistoryRepository
            .findAll(specification, request);
        return identitySourceSyncConverter.entityConvertToIdentitySourceSyncHistoryListResult(list);
    }

    /**
     * 查询身份源同步详情
     *
     * @param query     {@link  IdentitySourceSyncRecordListQuery}
     * @param pageModel {@link  PageModel}
     * @return {@link  IdentitySourceSyncRecordListResult}
     */
    @Override
    public Page<IdentitySourceSyncRecordListResult> getIdentitySourceSyncRecordList(IdentitySourceSyncRecordListQuery query,
                                                                                    PageModel pageModel) {
        //查询条件
        Specification<IdentitySourceSyncRecordEntity> specification = identitySourceSyncConverter
            .queryIdentitySourceSyncRecordListQueryConvertToSpecification(query);
        //分页条件
        PageRequest request = PageRequest.of(pageModel.getCurrent(), pageModel.getPageSize());
        //查询映射
        org.springframework.data.domain.Page<IdentitySourceSyncRecordEntity> list = identitySourceSyncRecordRepository
            .findAll(specification, request);
        return identitySourceSyncConverter.entityConvertToIdentitySourceSyncRecordListResult(list);
    }

    /**
     * 执行身份源同步
     *
     * @param id {@link  String} 身份源ID
     */
    @Override
    public void executeIdentitySourceSync(String id) {
        IdentitySourceEntity entity = identitySourceService.getIdentitySource(id);
        AuditContext.setTarget(
            Target.builder().id(id).name(entity.getName()).type(IDENTITY_SOURCE).build());
        if (!ObjectUtils.isEmpty(entity)) {
            if (Objects.isNull(entity.getBasicConfig())) {
                throw new NullPointerException("请完善参数配置");
            }
            if (!entity.getEnabled()) {
                throw new NullPointerException("身份源已禁用");
            }
            //发送分布式事件
            IdentitySourceEventUtils.sync(id);
            return;
        }
        throw new IdentitySourceNotExistException();
    }

    /**
     * 身份源service
     */
    private final IdentitySourceService               identitySourceService;
    /**
     * 身份源同步记录
     */
    private final IdentitySourceSyncHistoryRepository identitySourceSyncHistoryRepository;
    /**
     * 身份源同步详情
     */
    private final IdentitySourceSyncRecordRepository  identitySourceSyncRecordRepository;
    /**
     * 身份源同步转换
     */
    private final IdentitySourceSyncConverter         identitySourceSyncConverter;
}
