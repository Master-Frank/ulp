/*
 * ulp-common - United Login Platform
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
package cn.frank.ulp.common.constant;

import lombok.Data;
import static com.nimbusds.openid.connect.sdk.op.OIDCProviderConfigurationRequest.OPENID_PROVIDER_WELL_KNOWN_PATH;

import static cn.frank.ulp.support.constant.EiamConstants.V1_API_PATH;

/**
 * 协议常量
 *
 * @author Frank Zhang
 */
public final class ProtocolConstants {
    /**
     * 应用code
     */
    public static final String APP_CODE          = "appCode";

    /**
     * 提供商变量
     */
    public static final String APP_CODE_VARIABLE = "{" + APP_CODE + "}";

    public final static String AUTHORIZE_PATH    = V1_API_PATH + "/authorize";

    /**
     * OIDC Endpoint config
     */
    @Data
    public static class OidcEndpointConstants {
        //@formatter:off
        /**
         * OIDC BASE 认证路径
         */
        public final static String OIDC_AUTHORIZE_BASE_PATH = AUTHORIZE_PATH + "/" + APP_CODE_VARIABLE;

        public final static String OIDC_AUTHORIZE_PATH = OIDC_AUTHORIZE_BASE_PATH +"/oidc";

        public final static String OAUTH2_AUTHORIZE_PATH = OIDC_AUTHORIZE_BASE_PATH +"/oauth2";

        /**
         * OpenID Provider metadata.
         */
        public static final String WELL_KNOWN_OPENID_CONFIGURATION   = OIDC_AUTHORIZE_PATH +OPENID_PROVIDER_WELL_KNOWN_PATH;

        /**
         * Jwk Set Endpoint
         */
        public static final String JWK_SET_ENDPOINT                  = OIDC_AUTHORIZE_PATH + "/jwks";

        /**
         * OIDC Client Registration Endpoint
         */
        public static final String OIDC_CLIENT_REGISTRATION_ENDPOINT = OIDC_AUTHORIZE_PATH + "/connect/register";

        /**
         * OIDC Logout Endpoint
         */
        public static final String OIDC_LOGOUT_ENDPOINT = OIDC_AUTHORIZE_PATH + "/connect/logout";

        /**
         * Authorization Endpoint
         */
        public static final String AUTHORIZATION_ENDPOINT            = OAUTH2_AUTHORIZE_PATH + "/auth";

        /**
         * Authorization Consent Endpoint
         */
        public static final String AUTHORIZATION_CONSENT_ENDPOINT            = AUTHORIZATION_ENDPOINT+"/consent";

        /**
         * Token Endpoint
         */
        public static final String TOKEN_ENDPOINT                    = OAUTH2_AUTHORIZE_PATH + "/token";

        /**
         * Jwk Revocation Endpoint
         */
        public static final String TOKEN_REVOCATION_ENDPOINT         = OAUTH2_AUTHORIZE_PATH + "/revoke";

        /**
         * Token Introspection Endpoint
         */
        public static final String TOKEN_INTROSPECTION_ENDPOINT      = OAUTH2_AUTHORIZE_PATH + "/introspect";

        /**
         * OIDC User Info Endpoint
         */
        public static final String OIDC_USER_INFO_ENDPOINT           = OAUTH2_AUTHORIZE_PATH + "/userinfo";

        /**
         * 设备模式授权端点
         */
        public static final String DEVICE_AUTHORIZATION_ENDPOINT = OAUTH2_AUTHORIZE_PATH+"/device_authorization";

        /**
         * 设备模式验证端点
         */
        public static final String DEVICE_VERIFICATION_ENDPOINT = OAUTH2_AUTHORIZE_PATH+"/device_verification";

        //@formatter:on
    }

    /**
     * Form Endpoint config
     */
    @Data
    public static class FormEndpointConstants {

        /**
         * FORM  认证路径
         */
        public final static String FORM_AUTHORIZE_BASE_PATH = AUTHORIZE_PATH + "/form/"
                                                              + APP_CODE_VARIABLE;

        /**
         * FORM_SSO
         */
        public static final String FORM_SSO_PATH            = FORM_AUTHORIZE_BASE_PATH + "/sso";

        /**
         * FORM IDP SSO 发起
         */
        public static final String IDP_FORM_SSO_INITIATOR   = FORM_AUTHORIZE_BASE_PATH
                                                              + "/initiator";
    }

    /**
     * Form Endpoint config
     */
    @Data
    public static class JwtEndpointConstants {

        /**
         * JWT  认证路径
         */
        public final static String JWT_AUTHORIZE_BASE_PATH = AUTHORIZE_PATH + "/jwt/"
                                                             + APP_CODE_VARIABLE;

        /**
         * JWT_SSO
         */
        public static final String JWT_SSO_PATH            = JWT_AUTHORIZE_BASE_PATH + "/sso";

        /**
         * JWT_SLO
         */
        public static final String JWT_SLO_PATH            = JWT_AUTHORIZE_BASE_PATH + "/slo";

        /**
         * JWT IDP SSO 发起
         */
        public static final String IDP_JWT_SSO_INITIATOR   = JWT_AUTHORIZE_BASE_PATH + "/initiator";
    }
}
