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
