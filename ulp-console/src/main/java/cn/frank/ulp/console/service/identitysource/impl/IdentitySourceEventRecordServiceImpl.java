/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
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
