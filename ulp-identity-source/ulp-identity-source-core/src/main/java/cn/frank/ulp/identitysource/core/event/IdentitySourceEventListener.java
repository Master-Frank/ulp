/*
 * ulp-identity-source-core - United Login Platform
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
package cn.frank.ulp.identitysource.core.event;

/**
 * 身份源监听
 *
 * @author Frank Zhang
 */
public interface IdentitySourceEventListener {
    /**
     * 注册
     * @param id {@link String}
     */
    void register(String id);

    /**
     * 注册
     * @param id {@link String}
     */
    void destroy(String id);

    /**
     * 同步
     *
     * @param id {@link String}
     */
    void sync(String id);
}
