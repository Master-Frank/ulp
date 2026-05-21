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

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 获取 access_token 授权
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "获取 access_token 授权")
public class GetAccessTokenRequest implements Serializable {
    /**
     * 客户端ID
     */
    @JsonProperty(value = "client_id")
    @Schema(description = "客户端ID")
    private String clientId;

    /**
     * 客户端秘钥
     */
    @JsonProperty(value = "client_secret")
    @Schema(description = "客户端秘钥")
    private String clientSecret;
}
