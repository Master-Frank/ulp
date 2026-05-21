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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.frank.ulp.common.entity.account.UserEntity;
import cn.frank.ulp.common.entity.account.po.UserPO;
import cn.frank.ulp.common.entity.account.query.UserListNotInGroupQuery;
import cn.frank.ulp.common.entity.account.query.UserListQuery;

/**
 * User Repository Customized
 *
 * @author Frank Zhang
 */
public interface UserRepositoryCustomized {
    /**
     * 获取用户列表
     *
     * @param pageable {@link  Pageable}
     * @param query    {@link  UserListQuery}
     * @return {@link Page}
     */
    Page<UserPO> getUserList(UserListQuery query, Pageable pageable);

    /**
     * 获取用户组不存在成员列表
     *
     * @param query    {@link UserListNotInGroupQuery}
     * @param pageable {@link Pageable}
     * @return {@link Page}
     */
    Page<UserPO> getUserListNotInGroupId(UserListNotInGroupQuery query, Pageable pageable);

    /**
     * 不在组织下和数据来源查找用户列表
     *
     * @param identitySourceId {@link String}
     * @return {@link List}
     */
    List<UserEntity> findAllByOrgIdNotExistAndIdentitySourceId(String identitySourceId);

    /**
     * 批量新增
     *
     * @param list {@link List}
     */
    void batchSave(List<UserEntity> list);

    /**
     * 批量更新
     *
     * @param list {@link List}
     */
    void batchUpdate(List<UserEntity> list);
}
