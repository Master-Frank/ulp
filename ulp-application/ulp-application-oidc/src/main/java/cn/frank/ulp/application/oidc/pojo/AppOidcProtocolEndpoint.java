/*
 * ulp-application-oidc - United Login Platform
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
package cn.frank.ulp.application.oidc.pojo;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 协议端点域
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "协议端点")
public class AppOidcProtocolEndpoint implements Serializable {

    @Serial
    private static final long serialVersionUID = -2261602995152894964L;
    /**
     * oidcIssuer
     */
    @Schema(description = "Issuer")
    private String            issuer;

    /**
     * discoveryEndpoint
     */
    @Schema(description = "Discovery Endpoint")
    private String            discoveryEndpoint;

    /**
     * UserinfoEndpoint
     */
    @Schema(description = "UserInfo Endpoint")
    private String            userinfoEndpoint;

    /**
     * jwksEndpoint
     */
    @Schema(description = "Jwks Endpoint")
    private String            jwksEndpoint;

    /**
     * revokeEndpoint
     */
    @Schema(description = "Revoke Endpoint")
    private String            revokeEndpoint;

    /**
     * tokenEndpoint
     */
    @Schema(description = "Token Endpoint")
    private String            tokenEndpoint;

    /**
     * authorizationEndpoint
     */
    @Schema(description = "Authorization Endpoint")
    private String            authorizationEndpoint;

    /**
     * endSessionEndpoint
     */
    @Schema(description = "End Session Endpoint")
    private String            endSessionEndpoint;
}
