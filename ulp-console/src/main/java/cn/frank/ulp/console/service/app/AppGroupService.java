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
package cn.frank.ulp.console.service.app;

import java.util.List;

import cn.frank.ulp.console.pojo.query.app.AppGroupAssociationListQuery;
import cn.frank.ulp.console.pojo.query.app.AppGroupListQuery;
import cn.frank.ulp.console.pojo.result.app.AppGroupGetResult;
import cn.frank.ulp.console.pojo.result.app.AppGroupListResult;
import cn.frank.ulp.console.pojo.result.app.AppListResult;
import cn.frank.ulp.console.pojo.save.app.AppGroupCreateParam;
import cn.frank.ulp.console.pojo.update.app.AppGroupUpdateParam;
import cn.frank.ulp.support.repository.page.domain.Page;
import cn.frank.ulp.support.repository.page.domain.PageModel;

/**
 * <p>
 * 应用分组管理 服务类
 * </p>
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023-08-31
 */
public interface AppGroupService {

    /**
     * 获取应用分组（分页）
     *
     * @param pageModel {@link PageModel}
     * @param query     {@link AppGroupListQuery}
     * @return {@link AppGroupListResult}
     */
    Page<AppGroupListResult> getAppGroupList(PageModel pageModel, AppGroupListQuery query);

    /**
     * 创建应用分组
     *
     * @param param {@link AppGroupCreateParam}
     * @return {@link Boolean}
     */
    Boolean createAppGroup(AppGroupCreateParam param);

    /**
     * 修改应用分组
     *
     * @param param {@link AppGroupUpdateParam}
     * @return {@link Boolean}
     */
    boolean updateAppGroup(AppGroupUpdateParam param);

    /**
     * 删除应用分组
     *
     * @param id {@link  Long}
     * @return {@link Boolean}
     */
    boolean deleteAppGroup(String id);

    /**
     * 获取单个应用分组详情
     *
     * @param id {@link Long}
     * @return {@link AppGroupGetResult}
     */
    AppGroupGetResult getAppGroup(String id);

    /**
     * 批量移除应用
     *
     * @param id      {@link String}
     * @param appIds {@link String}
     * @return {@link Boolean}
     */
    Boolean batchRemoveAssociation(String id, List<String> appIds);

    /**
     * 获取应用组内应用列表
     *
     * @param query {@link AppGroupAssociationListQuery}
     * @param page  {@link PageModel}
     * @return {@link AppListResult}
     */
    Page<AppListResult> getAppGroupAssociationList(PageModel page,
                                                   AppGroupAssociationListQuery query);
}
