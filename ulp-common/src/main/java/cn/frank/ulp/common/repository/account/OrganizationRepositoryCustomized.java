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
package cn.frank.ulp.common.repository.account;

import java.util.List;

import cn.frank.ulp.common.entity.account.OrganizationEntity;
import cn.frank.ulp.common.entity.account.po.OrganizationPO;

/**
 * @author Frank Zhang
 */
public interface OrganizationRepositoryCustomized {

    /**
     * 批量保存
     *
     * @param list {@link List}
     */
    void batchSave(List<OrganizationEntity> list);

    /**
     * 批量更新
     *
     * @param list {@link List}
     */
    void batchUpdate(List<OrganizationEntity> list);

    /**
     * 用户对应组织列表
     *
     * @param userId {@link  String}
     * @return {@link List}
     */
    List<OrganizationPO> getOrganizationList(String userId);
}
