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

import cn.frank.ulp.common.entity.app.po.AppGroupPO;
import cn.frank.ulp.common.entity.app.query.AppGroupAssociationListQueryParam;
import cn.frank.ulp.common.entity.app.query.AppGroupQueryParam;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2023/9/8 19:20
 */
public interface AppGroupRepositoryCustomized {

    /**
     * 获取应用组应用列表
     *
     * @param query    {@link AppGroupAssociationListQueryParam}
     * @param pageable {@link Pageable}
     * @return {@link Page}
     */
    Page<AppGroupPO> getAppGroupList(AppGroupQueryParam query, Pageable pageable);

    /**
     * 查询应用组列表
     *
     * @param subjectIds  {@link List}
     * @param query {@link AppGroupQueryParam}
     * @return {@link List}
     */
    List<AppGroupPO> getAppGroupList(List<String> subjectIds, AppGroupQueryParam query);

    /**
     * 根据当前用户和分组获取应用数量
     *
     * @param groupId {@link String}
     * @param subjectIds {@link List}
     * @return {@link Long}
     */
    Long getAppCount(List<String> subjectIds, String groupId);
}
