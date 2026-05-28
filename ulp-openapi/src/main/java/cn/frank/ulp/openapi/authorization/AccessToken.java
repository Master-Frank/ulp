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
package cn.frank.ulp.openapi.authorization;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Authorization
 *
 * @author Frank Zhang
 */
@Data
public class AccessToken {
    /**
     * 客户端ID
     */
    @JsonProperty(value = "client_id")
    private String  clientId;

    /**
     * token 值
     */
    @JsonProperty(value = "value")
    private String  value;

    /**
     * 过期时间
     */
    @JsonProperty(value = "expires_in")
    private Integer expiresIn;

    public AccessToken() {
    }

    public AccessToken(String clientId, String value, Integer expiresIn) {
        this.clientId = clientId;
        this.value = value;
        this.expiresIn = expiresIn;
    }
}
