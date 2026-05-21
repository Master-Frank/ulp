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
package cn.frank.ulp.protocol.oidc.constant;

import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import static cn.frank.ulp.protocol.code.constant.ProtocolConstants.PROTOCOL_CACHE_PREFIX;
import static cn.frank.ulp.support.constant.EiamConstants.COLON;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/4 21:19
 */
public class OidcProtocolConstants {

    /**
     * 协议缓存前缀
     */
    public static final String          OIDC_PROTOCOL_CACHE_PREFIX = PROTOCOL_CACHE_PREFIX + "oidc"
                                                                     + COLON;

    public static final OAuth2TokenType ID_TOKEN                   = new OAuth2TokenType(
        "id_token");

    public static final String          OIDC_ERROR_URI             = "https://eiam.topiam.cn/docs/application/oidc/faq/#%E9%94%99%E8%AF%AF%E7%A0%81";

    public final class ConfigurationSettingNames {
        private static final String SETTINGS_NAMESPACE = "settings.";

        private ConfigurationSettingNames() {
        }

        /**
         * The names for token configuration settings.
         */
        public static final class Token {
            private static final String TOKEN_SETTINGS_NAMESPACE = SETTINGS_NAMESPACE
                .concat("token.");

            /**
             * id_token 扩展字段
             */
            public static final String  ID_TOKEN_CUSTOM_CLAIMS   = TOKEN_SETTINGS_NAMESPACE
                .concat("id-token-custom-claims");

            private Token() {
            }

        }

    }
}
