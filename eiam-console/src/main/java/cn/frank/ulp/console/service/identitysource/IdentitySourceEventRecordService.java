/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.console.service.identitysource;

import cn.frank.ulp.console.pojo.query.identity.IdentitySourceEventRecordListQuery;
import cn.frank.ulp.console.pojo.result.identitysource.IdentitySourceEventRecordListResult;
import cn.frank.ulp.support.repository.page.domain.Page;
import cn.frank.ulp.support.repository.page.domain.PageModel;

/**
 * <p>
 * 身份源事件记录 服务类
 * </p>
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020-08-16
 */
public interface IdentitySourceEventRecordService {

    /**
     * 身份源事件记录  列表
     *
     * @param query     {@link  IdentitySourceEventRecordListQuery}
     * @param pageModel {@link  PageModel}
     * @return {@link  IdentitySourceEventRecordListResult}
     */
    Page<IdentitySourceEventRecordListResult> getIdentitySourceEventRecordList(IdentitySourceEventRecordListQuery query,
                                                                               PageModel pageModel);
}
