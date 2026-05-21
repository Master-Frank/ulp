/*
 * ulp-protocol-core - United Login Platform
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
package cn.frank.ulp.protocol.code.util;

import java.util.Optional;

import org.springframework.security.web.SecurityFilterChain;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/11/25 14:03
 */
public final class SpringSecurityEndpointUtils<T> {

    /**
     * The Oauth 2 endpoint filter.
     */
    private final T oauth2EndpointFilter;

    /**
     * Instantiates a new Spring doc security o auth 2 endpoint utils.
     *
     * @param oauth2EndpointFilter the oauth 2 endpoint filter
     */
    public SpringSecurityEndpointUtils(T oauth2EndpointFilter) {
        this.oauth2EndpointFilter = oauth2EndpointFilter;
    }

    /**
     * Find endpoint object.
     *
     * @param filterChain the filter chain
     * @return the object
     */
    public Object findEndpoint(SecurityFilterChain filterChain) {
        Optional<?> oAuth2EndpointFilterOptional = filterChain.getFilters().stream()
            .filter(((Class<?>) oauth2EndpointFilter)::isInstance)
            .map(((Class<?>) oauth2EndpointFilter)::cast).findAny();
        return oAuth2EndpointFilterOptional.orElse(null);
    }
}
