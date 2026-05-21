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

import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/6/23 21:19
 */
@Data
@Schema(description = "获取 access_token 响应")
public class GetAccessTokenResponse {

    /**
     * access_token
     */
    @JsonProperty(value = "access_token")
    @Schema(name = "access_token")
    private String  accessToken;

    /**
     * expires_in
     */
    @JsonProperty(value = "expires_in")
    @Schema(name = "expires_in")
    private Integer expiresIn;

    /**
     * code
     */
    @JsonProperty(value = "code")
    @Schema(name = "code")
    private String  code;

    /**
     * msg
     */
    @JsonProperty(value = "msg")
    @Schema(name = "msg")
    private String  msg;
}
