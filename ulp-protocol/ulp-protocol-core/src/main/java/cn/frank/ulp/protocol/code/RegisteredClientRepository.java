/*
 * ulp-protocol-core - United Login Platform
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
package cn.frank.ulp.protocol.code;

import org.springframework.lang.Nullable;

/**
 * 客户端存储库
 *
 * @author Frank Zhang
 */
public interface RegisteredClientRepository<T extends RegisteredClient> {
    /**
     * save 客户端
     *
     * @param client {@link T}
     */
    void save(T client);

    /**
     * 根据ID查询已注册客户端
     *
     * @param id {@link String}
     * @return {@link T}
     */
    @Nullable
    T findById(String id);

    /**
     * 根据客户端ID查询已注册客户端
     *
     * @param clientId {@link String}
     * @return {@link T}
     */
    @Nullable
    T findByClientId(String clientId);

}
