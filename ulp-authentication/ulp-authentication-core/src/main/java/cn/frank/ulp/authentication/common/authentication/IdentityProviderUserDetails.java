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
package cn.frank.ulp.authentication.common.authentication;

import java.util.Map;

import cn.frank.ulp.authentication.common.IdentityProviderType;

import lombok.Getter;

import tools.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * IDP用户信息
 *
 * @author Frank Zhang
 */
@Getter
public class IdentityProviderUserDetails {

    /**
     * 个人邮箱
     */
    private final String               email;

    /**
     * 手机号
     */
    private final String               mobile;

    /**
     * 昵称
     */
    private final String               nickName;

    /**
     * 头像url
     */
    private final String               avatarUrl;

    /**
     * openId
     */
    private final String               openId;

    /**
     * 手机号对应的国家号
     */
    private final String               stateCode;

    /**
     * unionId
     */
    public String                      unionId;

    /**
     * providerId
     */
    private final String               providerId;

    /**
     * providerCode
     */
    private final String               providerCode;

    /**
     * providerType
     */
    private final IdentityProviderType providerType;

    /**
     * 额外配置
     */
    private final Map<String, Object>  additionalInfo;

    public static IdpUserDetailsBuilder builder() {
        return new IdpUserDetailsBuilder();
    }

    IdentityProviderUserDetails(final String email, final String mobile, final String nickName,
                                final String avatarUrl, final String openId, final String stateCode,
                                final String unionId, final String providerId,
                                final String providerCode, final IdentityProviderType providerType,
                                final Map<String, Object> additionalInfo) {
        if (providerId == null) {
            throw new NullPointerException("providerId is marked non-null but is null");
        } else if (providerType == null) {
            throw new NullPointerException("providerType is marked non-null but is null");
        } else {
            this.email = email;
            this.mobile = mobile;
            this.nickName = nickName;
            this.avatarUrl = avatarUrl;
            this.openId = openId;
            this.stateCode = stateCode;
            this.unionId = unionId;
            this.providerId = providerId;
            this.providerType = providerType;
            this.providerCode = providerCode;
            this.additionalInfo = additionalInfo;
        }
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class IdpUserDetailsBuilder {

        private String               email;

        private String               mobile;

        private String               nickName;

        private String               avatarUrl;

        private String               openId;

        private String               stateCode;

        private String               unionId;

        private String               providerId;

        private String               providerCode;

        private IdentityProviderType providerType;

        private Map<String, Object>  additionalInfo;

        IdpUserDetailsBuilder() {
        }

        public IdpUserDetailsBuilder email(final String email) {
            this.email = email;
            return this;
        }

        public IdpUserDetailsBuilder mobile(final String mobile) {
            this.mobile = mobile;
            return this;
        }

        public IdpUserDetailsBuilder nickName(final String nickName) {
            this.nickName = nickName;
            return this;
        }

        public IdpUserDetailsBuilder avatarUrl(final String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }

        public IdpUserDetailsBuilder openId(final String openId) {
            this.openId = openId;
            return this;
        }

        public IdpUserDetailsBuilder stateCode(final String stateCode) {
            this.stateCode = stateCode;
            return this;
        }

        public IdpUserDetailsBuilder unionId(final String unionId) {
            this.unionId = unionId;
            return this;
        }

        public IdpUserDetailsBuilder providerId(final String providerId) {
            if (providerId == null) {
                throw new NullPointerException("providerId is marked non-null but is null");
            } else {
                this.providerId = providerId;
                return this;
            }
        }

        public IdpUserDetailsBuilder providerCode(final String providerCode) {
            if (providerCode == null) {
                throw new NullPointerException("providerCode is marked non-null but is null");
            } else {
                this.providerCode = providerCode;
                return this;
            }
        }

        public IdpUserDetailsBuilder providerType(final IdentityProviderType providerType) {
            if (providerType == null) {
                throw new NullPointerException("providerType is marked non-null but is null");
            } else {
                this.providerType = providerType;
                return this;
            }
        }

        public IdpUserDetailsBuilder additionalInfo(final Map<String, Object> additionalInfo) {
            this.additionalInfo = additionalInfo;
            return this;
        }

        public IdentityProviderUserDetails build() {
            return new IdentityProviderUserDetails(this.email, this.mobile, this.nickName,
                this.avatarUrl, this.openId, this.stateCode, this.unionId, this.providerId,
                this.providerCode, this.providerType, this.additionalInfo);
        }

    }
}
