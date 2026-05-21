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
