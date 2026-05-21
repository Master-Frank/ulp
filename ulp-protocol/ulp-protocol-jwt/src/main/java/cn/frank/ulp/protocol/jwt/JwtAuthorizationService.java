/*
 * ulp-protocol-jwt - United Login Platform
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
package cn.frank.ulp.protocol.jwt;

import org.springframework.lang.Nullable;

import cn.frank.ulp.protocol.code.LoginAccount;
import cn.frank.ulp.protocol.jwt.client.JwtRegisteredClient;
import cn.frank.ulp.support.security.userdetails.UserDetails;

/**
 *
 * @author Frank Zhang
 */
public interface JwtAuthorizationService {

    void save(JwtAuthentication token);

    void remove(JwtAuthentication authorization);

    @Nullable
    JwtAuthentication findById(String id);

    @Nullable
    JwtAuthentication findByToken(String token);

    /**
     * 用户是否具有登录权限
     *
     * @param client {@link JwtRegisteredClient}
     * @param user {@link UserDetails}
     * @return {@link Boolean}
     */
    Boolean hasLoginPermission(JwtRegisteredClient client, UserDetails user);

    /**
     * 根据客户端&用户ID查询默认应用账户
     *
     * @param client {@link String} 客户端
     * @param  user {@link UserDetails} 用户
     * @return {@link String}
     */
    LoginAccount getDefaultLoginAccount(JwtRegisteredClient client, UserDetails user);
}
