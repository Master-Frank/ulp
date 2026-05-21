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
package cn.frank.ulp.console.service.account;

import java.util.List;

import cn.frank.ulp.common.entity.account.OrganizationEntity;
import cn.frank.ulp.console.pojo.result.account.*;
import cn.frank.ulp.console.pojo.save.account.OrganizationCreateParam;
import cn.frank.ulp.console.pojo.update.account.OrganizationUpdateParam;

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
     * @return {@link Boolean}
     */
    Boolean createOrg(OrganizationCreateParam param);

    /**
     * 修改组织架构
     *
     * @param param {@link OrganizationUpdateParam}
     * @return {@link Boolean}
     */
    Boolean updateOrg(OrganizationUpdateParam param);

    /**
     * 启用/禁用
     *
     * @param id      {@link String}
     * @param enabled {@link Boolean}
     * @return {@link Boolean}
     */
    Boolean updateStatus(String id, boolean enabled);

    /**
     * 删除组织架构
     *
     * @param id {@link List}
     * @return {@link Boolean}
     */
    Boolean deleteOrg(String id);

    /**
     * 根据ID查询组织架构
     *
     * @param id {@link String}
     * @return {@link OrganizationResult}
     */
    OrganizationResult getOrganization(String id);

    /**
     * 移动组织机构
     *
     * @param id       {@link String}
     * @param parentId {@link String}
     * @return {@link Boolean}
     */
    Boolean moveOrganization(String id, String parentId);

    /**
     * 查询根组织
     *
     * @return {@link OrganizationRootResult}
     */
    OrganizationRootResult getRootOrganization();

    /**
     * 查询子组织
     *
     * @param parentId {@link String}
     * @return {@link OrganizationChildResult}
     */
    List<OrganizationChildResult> getChildOrganization(String parentId);

    /**
     * 查询子组织
     *
     * @param parentId         {@link String}
     * @param dataOrigin       {@link String}
     * @param identitySourceId {@link String}
     * @return {@link OrganizationEntity}
     */
    List<OrganizationEntity> getChildOrgList(String parentId, String dataOrigin,
                                             String identitySourceId);

    /**
     * 过滤组织树
     *
     * @param keyWord {@link String} 关键字
     * @return {@link List}
     */
    List<SearchOrganizationTreeResult> searchOrganizationTree(String keyWord);

    /**
     * 过滤组织
     *
     * @param keyWord {@link String} 关键字
     * @return {@link List}
     */
    List<SearchOrganizationResult> searchOrganization(String keyWord);

    /**
     * 根据ID查询组织架构
     *
     * @param id {@link String}
     * @return {@link OrganizationEntity}
     */
    OrganizationEntity getById(String id);

    /**
     * 根据外部ID查询组织架构
     *
     * @param id               {@link String}
     * @param identitySourceId {@link String}
     * @return {@link OrganizationEntity}
     */
    OrganizationEntity getOrganizationByExternalId(String id, String identitySourceId);

    /**
     * 批量获取组织
     *
     * @param ids {@link List}
     * @return  {@link List}
     */
    List<BatchOrganizationResult> batchGetOrganization(List<String> ids);
}
