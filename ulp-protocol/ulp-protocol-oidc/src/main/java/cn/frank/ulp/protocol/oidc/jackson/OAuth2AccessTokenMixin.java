/*
 * ulp-protocol-oidc - United Login Platform
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
package cn.frank.ulp.protocol.oidc.jackson;

import java.time.Instant;
import java.util.Set;

import org.springframework.security.oauth2.core.OAuth2AccessToken;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * OAuth2AccessTokenMixin
 *
 * @author Frank Zhang
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
@SuppressWarnings({ "AlibabaClassNamingShouldBeCamel",
                    "AlibabaAbstractClassShouldStartWithAbstractNaming" })
abstract class OAuth2AccessTokenMixin {

    @JsonCreator
    OAuth2AccessTokenMixin(@JsonProperty("tokenType") OAuth2AccessToken.TokenType tokenType,
                           @JsonProperty("tokenValue") String tokenValue,
                           @JsonProperty("issuedAt") Instant issuedAt,
                           @JsonProperty("expiresAt") Instant expiresAt,
                           @JsonProperty("scopes") Set<String> scopes) {
    }

}
