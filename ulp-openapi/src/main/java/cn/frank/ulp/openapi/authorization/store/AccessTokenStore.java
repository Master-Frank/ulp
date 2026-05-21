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
package cn.frank.ulp.openapi.authorization.store;

import java.io.Serializable;
import java.util.List;

import cn.frank.ulp.openapi.authorization.AccessToken;

/**
 * TokenStorage
 *
 * @author Frank Zhang
 */
public interface AccessTokenStore extends Serializable {

    /**
     * save
     *
     * @param token {@link AccessToken}
     */
    void save(AccessToken token);

    /**
     * 根据token删除
     *
     * @param token {@link String}
     */
    void deleteByToken(String token);

    /**
     * 根据token查询
     *
     * @param token {@link String}
     * @return {@link AccessToken}
     */
    AccessToken findByToken(String token);

    /**
     * 根据 clientId 查询
     *
     * @param clientId {@link String}
     * @return {@link List}
     */
    AccessToken findByClientId(String clientId);
}
