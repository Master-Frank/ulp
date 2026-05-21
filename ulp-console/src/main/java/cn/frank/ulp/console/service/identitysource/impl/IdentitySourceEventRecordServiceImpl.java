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

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import cn.frank.ulp.common.entity.identitysource.IdentitySourceEventRecordEntity;
import cn.frank.ulp.common.repository.identitysource.IdentitySourceEventRecordRepository;
import cn.frank.ulp.console.converter.identitysource.IdentitySourceEventRecordConverter;
import cn.frank.ulp.console.pojo.query.identity.IdentitySourceEventRecordListQuery;
import cn.frank.ulp.console.pojo.result.identitysource.IdentitySourceEventRecordListResult;
import cn.frank.ulp.console.service.identitysource.IdentitySourceEventRecordService;
import cn.frank.ulp.support.repository.page.domain.Page;
import cn.frank.ulp.support.repository.page.domain.PageModel;

import lombok.AllArgsConstructor;

/**
 * 身份源事件记录
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/3/16 21:04
 */
@Service
@AllArgsConstructor
public class IdentitySourceEventRecordServiceImpl implements IdentitySourceEventRecordService {

    /**
     * 身份源事件记录  列表
     *
     * @param query     {@link  IdentitySourceEventRecordListQuery}
     * @param pageModel {@link  PageModel}
     * @return {@link  IdentitySourceEventRecordListResult}
     */
    @Override
    public Page<IdentitySourceEventRecordListResult> getIdentitySourceEventRecordList(IdentitySourceEventRecordListQuery query,
                                                                                      PageModel pageModel) {
        //查询条件
        Specification<IdentitySourceEventRecordEntity> specification = identitySourceEventRecordConverter
            .queryIdentitySourceEventRecordListQueryConvertToSpecification(query);
        //分页条件
        //查询映射
        org.springframework.data.domain.Page<IdentitySourceEventRecordEntity> list = identitySourceEventRecordRepository
            .findAll(specification,
                PageRequest.of(pageModel.getCurrent(), pageModel.getPageSize()));
        return identitySourceEventRecordConverter
            .entityConvertToIdentitySourceSyncRecordListResult(list);
    }

    /**
     * 身份源时间记录
     */
    private final IdentitySourceEventRecordRepository identitySourceEventRecordRepository;

    private final IdentitySourceEventRecordConverter  identitySourceEventRecordConverter;
}
