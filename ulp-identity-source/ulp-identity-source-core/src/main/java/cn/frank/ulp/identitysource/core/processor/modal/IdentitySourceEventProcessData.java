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
package cn.frank.ulp.identitysource.core.processor.modal;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import cn.frank.ulp.common.enums.identitysource.IdentitySourceProvider;
import cn.frank.ulp.identitysource.core.enums.IdentitySourceEventReceiveType;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 身份源回调事件数据
 *
 * @author Frank Zhang
 */
@Data
@AllArgsConstructor
public class IdentitySourceEventProcessData<T> implements Serializable {

    private String                         id;

    /**
     * 数据
     */
    private List<T>                        data;

    /**
     * 身份提供商
     */
    private IdentitySourceProvider         provider;

    /**
     * 时间事件
     */
    private LocalDateTime                  eventTime;

    /**
     * 事件类型
     */
    private IdentitySourceEventReceiveType eventType;

}
