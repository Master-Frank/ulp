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

import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;

import tools.jackson.core.Version;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import tools.jackson.databind.module.SimpleModule;

/**
 * OAuth2AuthorizationModule
 *
 * Jackson 3 默认类型校验在 mapper builder 上配置，模块不再主动启用 default typing。
 *
 * @author Frank Zhang
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class OAuth2AuthorizationModule extends SimpleModule {

    public OAuth2AuthorizationModule() {
        super(OAuth2AuthorizationModule.class.getName(), new Version(1, 0, 0, null, null, null));
    }

    @Override
    public void setupModule(SetupContext context) {
        context.setMixIn(OAuth2Authorization.class, OAuth2AuthorizationMixin.class);
        context.setMixIn(OAuth2AuthorizationCode.class, OAuth2AuthorizationCodeMixin.class);
        context.setMixIn(OAuth2Authorization.Token.class, TokenMixin.class);
        context.setMixIn(OAuth2AccessToken.class, OAuth2AccessTokenMixin.class);
        context.setMixIn(OAuth2RefreshToken.class, OAuth2RefreshTokenMixin.class);
        context.setMixIn(AuthorizationGrantType.class, AuthorizationGrantTypeMixin.class);
        context.setMixIn(OAuth2AccessToken.TokenType.class, TokenTypeMixin.class);
        context.setMixIn(OidcIdToken.class, OidcIdTokenMixin.class);
    }

    /**
     * 给外部调用方追加 OAuth2 相关类的 PolymorphicTypeValidator 白名单。
     */
    public static void configurePolymorphicTypeValidator(BasicPolymorphicTypeValidator.Builder builder) {
        builder.allowIfSubType("org.springframework.security.oauth2.")
            .allowIfSubType("org.springframework.security.")
            .allowIfSubType("java.util.")
            .allowIfSubType("java.time.");
    }

}
