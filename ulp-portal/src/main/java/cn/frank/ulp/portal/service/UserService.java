/*
 * ulp-portal - United Login Platform
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
package cn.frank.ulp.portal.service;

import java.util.Optional;

import cn.frank.ulp.common.entity.account.UserEntity;
import cn.frank.ulp.support.security.userdetails.UserDetails;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2024/3/2 22:21
 */
public interface UserService {
    /**
     * 获取用户详情
     *
     * @return {@link UserDetails}
     */
    UserDetails getUserDetails(String userId);

    /**
     * 获取用户详情
     *
     * @return {@link UserEntity}
     */
    UserDetails getUserDetails(UserEntity user);

    /**
     * 根据用户名、手机号、邮箱查询用户
     *
     * @return {@link UserEntity}
     */
    Optional<UserEntity> findByUsernameOrPhoneOrEmail(String keyword);
}
