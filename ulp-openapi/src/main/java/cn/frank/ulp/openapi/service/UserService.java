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

import java.io.Serializable;

import cn.frank.ulp.common.entity.account.query.UserListQuery;
import cn.frank.ulp.common.enums.UserStatus;
import cn.frank.ulp.openapi.pojo.result.UserListResult;
import cn.frank.ulp.openapi.pojo.result.UserResult;
import cn.frank.ulp.openapi.pojo.save.UserCreateParam;
import cn.frank.ulp.openapi.pojo.update.UserUpdateParam;
import cn.frank.ulp.support.repository.page.domain.Page;
import cn.frank.ulp.support.repository.page.domain.PageModel;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Frank Zhang
 */
public interface UserService {

    /**
     * 获取用户（分页）
     *
     * @param page  {@link PageModel}
     * @param query {@link UserListQuery}
     * @return {@link UserListQuery}
     */
    Page<UserListResult> getUserList(PageModel page, UserListQuery query);

    /**
     * 更改用户状态
     *
     * @param id     {@link Long}
     * @param status {@link UserStatus}
     */
    void changeUserStatus(String id, UserStatus status);

    /**
     * 创建用户
     *
     * @param param {@link UserCreateParam}
     */
    void createUser(UserCreateParam param);

    /**
     * 根据ID查询用户
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    UserResult getUser(String id);

    /**
     * 更新用户
     *
     * @param param {@link UserUpdateParam}
     */
    void updateUser(UserUpdateParam param);

    /**
     * 删除用户
     *
     * @param id {@link Serializable}
     */
    void deleteUser(String id);

    /**
     * 获取用户id
     *
     * @param externalId {@link String}
     * @param phoneNumber {@link String}
     * @param email {@link String}
     * @param username {@link String}
     * @return {@link String}
     */
    String getUserIdByParams(String externalId, String phoneNumber, String email, String username);
}
