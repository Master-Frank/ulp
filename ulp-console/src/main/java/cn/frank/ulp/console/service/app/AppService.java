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

import java.util.Map;

import cn.frank.ulp.console.pojo.query.app.AppQuery;
import cn.frank.ulp.console.pojo.result.app.AppCreateResult;
import cn.frank.ulp.console.pojo.result.app.AppGetResult;
import cn.frank.ulp.console.pojo.result.app.AppListResult;
import cn.frank.ulp.console.pojo.save.app.AppCreateParam;
import cn.frank.ulp.console.pojo.update.app.AppSaveConfigParam;
import cn.frank.ulp.console.pojo.update.app.AppUpdateParam;
import cn.frank.ulp.support.repository.page.domain.Page;
import cn.frank.ulp.support.repository.page.domain.PageModel;

/**
 * <p>
 * 应用管理 服务类
 * </p>
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020-07-31
 */
public interface AppService {

    /**
     * 获取应用（分页）
     *
     * @param pageModel {@link PageModel}
     * @param query     {@link AppQuery}
     * @return {@link AppListResult}
     */
    Page<AppListResult> getAppList(PageModel pageModel,
                                   AppQuery query);

    /**
     * 创建应用
     *
     * @param param {@link AppCreateParam}
     * @return {@link AppCreateResult}
     */
    AppCreateResult createApp(AppCreateParam param);

    /**
     * 修改应用
     *
     * @param param {@link AppUpdateParam}
     * @return {@link Boolean}
     */
    boolean updateApp(AppUpdateParam param);

    /**
     * 删除应用
     *
     * @param id {@link  Long}
     * @return {@link Boolean}
     */
    boolean deleteApp(String id);

    /**
     * 获取单个应用详情
     *
     * @param id {@link Long}
     * @return {@link AppGetResult}
     */
    AppGetResult getApp(String id);

    /**
     * 启用应用
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    Boolean enableApp(String id);

    /**
     * 禁用应用
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    Boolean disableApp(String id);

    /**
     *  更新应用配置
     *
     * @param param {@link AppSaveConfigParam}
     * @return {@link Boolean}
     */
    Boolean saveAppConfig(AppSaveConfigParam param);

    /**
     * 获取应用配置
     *
     * @param appId {@link String}
     * @return {@link Map}
     */
    Object getAppConfig(String appId);
}
