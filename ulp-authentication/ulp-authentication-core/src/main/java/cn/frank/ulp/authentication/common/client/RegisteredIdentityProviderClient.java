/*
 * ulp-authentication-core - United Login Platform
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
package cn.frank.ulp.authentication.common.client;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * 已注册的身份提供程序客户端
 *
 * @author Frank Zhang
 */
@Data
@Builder
public class RegisteredIdentityProviderClient<T extends IdentityProviderConfig>
                                             implements Serializable {

    /**
     * id
     */
    private String id;

    /**
     * code
     */
    private String code;

    /**
     * name
     */
    private String name;

    /**
     * 配置
     */
    private T      config;
}
