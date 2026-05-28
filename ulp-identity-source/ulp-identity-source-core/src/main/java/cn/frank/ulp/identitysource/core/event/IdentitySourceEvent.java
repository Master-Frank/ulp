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

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
     * 身份源配置事件
     *
     * @author Frank Zhang
     */
@Data
@RequiredArgsConstructor
public class IdentitySourceEvent implements Serializable {
    @Serial
    private static final long             serialVersionUID = 1099740917667842614L;
    /**
     * ID
     */
    private final String                  id;
    /**
     * 事件类型
     */
    private final IdentitySourceEventType identitySourceEventType;
}
