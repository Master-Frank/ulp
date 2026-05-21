/*
 * ulp-protocol-form - United Login Platform
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
package cn.frank.ulp.protocol.form.client;

import java.time.Instant;

import cn.frank.ulp.protocol.code.RegisteredClient;

import lombok.Builder;
import lombok.Data;

/**
 * Form 已注册的客户端
 *
 * @author Frank Zhang
 */
@Data
@Builder
public class FormRegisteredClient implements RegisteredClient {
    /**
     * 应用ID
     */
    private String  id;

    /**
     * 应用code
     */
    private String  code;

    /**
     * 客户端ID
     */
    private String  clientId;

    /**
     * 客户端名称
     */
    private String  clientName;

    /**
     * 客户端ID创建时间
     */
    private Instant clientIdIssuedAt;

    /**
     * 客户端秘钥
     */
    private String  clientSecret;

    /**
     * 客户端秘钥创建时间
     */
    private Instant clientSecretExpiresAt;

}
