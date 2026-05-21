/*
 * eiam-audit - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.audit.service;

import java.util.List;

import cn.frank.ulp.audit.endpoint.pojo.AuditListQuery;
import cn.frank.ulp.audit.endpoint.pojo.AuditListResult;
import cn.frank.ulp.audit.endpoint.pojo.DictResult;
import cn.frank.ulp.support.repository.page.domain.Page;
import cn.frank.ulp.support.repository.page.domain.PageModel;

/**
 * 审计service
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/10 23:06
 */
public interface AuditService {
    /**
     * List
     *
     * @param query     {@link AuditListQuery}
     * @param pageModel {@link PageModel}
     * @return {@link Page}
     */
    Page<AuditListResult> getAuditList(AuditListQuery query, PageModel pageModel);

    /**
     * 获取字典类型
     *
     * @param userType {@link String}
     * @return {@link List}
     */
    List<DictResult> getAuditDict(String userType);
}
