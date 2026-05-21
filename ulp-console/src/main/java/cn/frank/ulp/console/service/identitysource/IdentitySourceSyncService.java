/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.console.service.identitysource;

import cn.frank.ulp.console.pojo.query.identity.IdentitySourceSyncHistoryListQuery;
import cn.frank.ulp.console.pojo.query.identity.IdentitySourceSyncRecordListQuery;
import cn.frank.ulp.console.pojo.result.identitysource.IdentitySourceSyncHistoryListResult;
import cn.frank.ulp.console.pojo.result.identitysource.IdentitySourceSyncRecordListResult;
import cn.frank.ulp.support.repository.page.domain.Page;
import cn.frank.ulp.support.repository.page.domain.PageModel;

/**
 * 身份源同步service接口
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/3/16 22:56
 */
public interface IdentitySourceSyncService {
    /**
     * 执行身份源同步
     *
     * @param id {@link  String} 身份源ID
     */
    void executeIdentitySourceSync(String id);

    /**
     * 查询身份源同步列表
     *
     * @param query     {@link  IdentitySourceSyncHistoryListQuery}
     * @param pageModel {@link  PageModel}
     * @return {@link  IdentitySourceSyncHistoryListResult}
     */
    Page<IdentitySourceSyncHistoryListResult> getIdentitySourceSyncHistoryList(IdentitySourceSyncHistoryListQuery query,
                                                                               PageModel pageModel);

    /**
     * 查询身份源同步详情
     *
     * @param query     {@link  IdentitySourceSyncRecordListQuery}
     * @param pageModel {@link  PageModel}
     * @return {@link  IdentitySourceSyncRecordListResult}
     */
    Page<IdentitySourceSyncRecordListResult> getIdentitySourceSyncRecordList(IdentitySourceSyncRecordListQuery query,
                                                                             PageModel pageModel);
}
