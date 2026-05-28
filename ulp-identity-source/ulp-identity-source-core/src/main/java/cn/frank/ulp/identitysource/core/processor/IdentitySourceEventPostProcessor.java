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
package cn.frank.ulp.identitysource.core.processor;

import cn.frank.ulp.identitysource.core.processor.modal.IdentitySourceEventProcessData;

/**
 * 身份源数据 event 处理器
 *
 * @author Frank Zhang
 */
public interface IdentitySourceEventPostProcessor {
    /**
     * 处理数据
     *
     * @param data  {@link  IdentitySourceEventProcessData}
     */
    void process(IdentitySourceEventProcessData<?> data);
}
