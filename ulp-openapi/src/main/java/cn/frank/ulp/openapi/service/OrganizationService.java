/*
 * ulp-openapi - United Login Platform
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
package cn.frank.ulp.openapi.service;

import java.util.List;

import cn.frank.ulp.openapi.pojo.result.OrganizationChildResult;
import cn.frank.ulp.openapi.pojo.result.OrganizationResult;
import cn.frank.ulp.openapi.pojo.save.OrganizationCreateParam;
import cn.frank.ulp.openapi.pojo.update.OrganizationUpdateParam;

/**
 * <p>
 * 组织架构 服务类
 * </p>
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020-08-09
 */
public interface OrganizationService {

    /**
     * 创建组织架构
     *
     * @param param {@link OrganizationCreateParam}
     */
    void createOrg(OrganizationCreateParam param);

    /**
     * 修改组织架构
     *
     * @param param {@link OrganizationUpdateParam}
     */
    void updateOrg(OrganizationUpdateParam param);

    /**
     * 启用/禁用
     *
     * @param id      {@link String}
     * @param enabled {@link Boolean}
     * @return {@link Boolean}
     */
    void updateStatus(String id, boolean enabled);

    /**
     * 删除组织架构
     *
     * @param id {@link List}
     * @return {@link Boolean}
     */
    void deleteOrg(String id);

    /**
     * 根据ID查询组织架构
     *
     * @param id {@link String}
     * @return {@link OrganizationResult}
     */
    OrganizationResult getOrganizationById(String id);

    /**
     * 查询子组织
     *
     * @param parentId {@link String}
     * @return {@link OrganizationChildResult}
     */
    List<OrganizationChildResult> getChildOrganization(String parentId);

    /**
     * 根据外部ID获取组织ID
     *
     * @param externalId {@link String}
     * @return {@link String}
     */
    String getOrganizationIdByExternalId(String externalId);
}
