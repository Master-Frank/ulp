/*
 * ulp-common - United Login Platform
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
package cn.frank.ulp.common.repository.app;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.frank.ulp.common.entity.app.AppEntity;
import cn.frank.ulp.common.entity.app.po.AppPO;
import cn.frank.ulp.common.entity.app.query.GetAppListQueryParam;

/**
 * 应用 Repository Customized
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/5/26 23:40
 */
public interface AppRepositoryCustomized {
    /**
     * 根据主体ID获取应用列表
     *
     * @param subjectIds {@link  List}
     * @return {@link Page}
     */
    List<AppPO> getAppList(List<String> subjectIds);

    /**
     * 根据主体ID，查询参数、分页条件获取应用列表
     *
     * @param subjectIds {@link  List}
     * @param query {@link GetAppListQueryParam}
     * @param pageable    {@link  Pageable}
     * @return {@link Page}
     */
    Page<AppEntity> getAppList(List<String> subjectIds, GetAppListQueryParam query,
                               Pageable pageable);

    /**
     * 获取用户应用数量
     *
     * @param subjectIds {@link List}
     * @return {@link Long}
     */
    Long getAppCount(List<String> subjectIds);
}
