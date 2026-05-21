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
package cn.frank.ulp.authentication.common.jackjson;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.*;

import cn.frank.ulp.authentication.common.IdentityProviderType;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/10 22:25
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, isGetterVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class IdentityProviderAuthenticationTokenMixin {
    @JsonCreator
    IdentityProviderAuthenticationTokenMixin(@JsonProperty("principal") Object principal,
                                             @JsonProperty("providerType") IdentityProviderType providerType,
                                             @JsonProperty("providerId") String providerId,
                                             @JsonProperty("authorities") Collection<? extends GrantedAuthority> authorities) {
    }
}
