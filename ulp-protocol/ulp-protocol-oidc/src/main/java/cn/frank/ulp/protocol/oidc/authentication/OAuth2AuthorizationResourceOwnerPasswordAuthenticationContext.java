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
package cn.frank.ulp.protocol.oidc.authentication;

import java.util.Map;

import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthenticationContext;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.util.Assert;

/**
 * @author Frank Zhang
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public final class OAuth2AuthorizationResourceOwnerPasswordAuthenticationContext implements
                                                                                 OAuth2AuthenticationContext {
    private final Map<Object, Object> context;

    private OAuth2AuthorizationResourceOwnerPasswordAuthenticationContext(Map<Object, Object> context) {
        this.context = Map.copyOf(context);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <V> V get(Object key) {
        return hasKey(key) ? (V) this.context.get(key) : null;
    }

    @Override
    public boolean hasKey(Object key) {
        Assert.notNull(key, "key cannot be null");
        return this.context.containsKey(key);
    }

    /**
     * Returns the {@link RegisteredClient registered client}.
     *
     * @return the {@link RegisteredClient}
     */
    public RegisteredClient getRegisteredClient() {
        return get(RegisteredClient.class);
    }

    /**
     * Constructs a new {@link Builder} with the provided {@link OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken}.
     *
     * @param authentication the {@link OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken}
     * @return the {@link Builder}
     */
    public static Builder with(OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken authentication) {
        return new Builder(authentication);
    }

    /**
     * A builder for {@link OAuth2AuthorizationResourceOwnerPasswordAuthenticationContext}.
     */
    public static final class Builder extends
                                      AbstractBuilder<OAuth2AuthorizationResourceOwnerPasswordAuthenticationContext, Builder> {

        private Builder(OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken authentication) {
            super(authentication);
        }

        /**
         * Sets the {@link RegisteredClient registered client}.
         *
         * @param registeredClient the {@link RegisteredClient}
         * @return the {@link Builder} for further configuration
         */
        public Builder registeredClient(RegisteredClient registeredClient) {
            return put(RegisteredClient.class, registeredClient);
        }

        /**
         * Builds a new {@link OAuth2AuthorizationResourceOwnerPasswordAuthenticationContext}.
         *
         * @return the {@link OAuth2AuthorizationResourceOwnerPasswordAuthenticationContext}
         */
        @Override
        public OAuth2AuthorizationResourceOwnerPasswordAuthenticationContext build() {
            Assert.notNull(get(RegisteredClient.class), "registeredClient cannot be null");
            return new OAuth2AuthorizationResourceOwnerPasswordAuthenticationContext(getContext());
        }

    }

}
