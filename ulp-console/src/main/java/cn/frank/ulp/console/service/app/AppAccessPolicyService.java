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

import cn.frank.ulp.console.pojo.query.app.AppAccessPolicyQuery;
import cn.frank.ulp.console.pojo.result.app.AppAccessPolicyResult;
import cn.frank.ulp.console.pojo.save.app.AppAccessPolicyCreateParam;
import cn.frank.ulp.support.repository.page.domain.Page;
import cn.frank.ulp.support.repository.page.domain.PageModel;

/**
 * 应用访问权限策略 Service
 *
 * @author Frank Zhang
 */
public interface AppAccessPolicyService {

    /**
     * 查询应用授权策略列表
     *
     * @param pageModel {@link PageModel}
     * @param query     {@link AppAccessPolicyQuery}
     * @return {@link Page}
     */
    Page<AppAccessPolicyResult> getAppAccessPolicyList(PageModel pageModel,
                                                       AppAccessPolicyQuery query);

    /**
     * 创建应用授权策略
     *
     * @param param {@link AppAccessPolicyCreateParam}
     * @return @{link Boolean}
     */
    Boolean createAppAccessPolicy(AppAccessPolicyCreateParam param);

    /**
     * 删除应用授权策略
     *
     * @param id {@link  String}
     * @return {@link Boolean}
     */
    Boolean deleteAppAccessPolicy(String id);

    /**
     * 启用应用访问授权
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    Boolean enableAppAccessPolicy(String id);

    /**
     * 禁用应用访问授权
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    Boolean disableAppAccessPolicy(String id);
}
