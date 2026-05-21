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
package cn.frank.ulp.authentication.common.constant;

/**
 * 认证常量
 *
 * @author Frank Zhang
 */
public final class AuthenticationConstants {

    /**
     * 提供商ID
     */
    public static final String PROVIDER_CODE                              = "provider_code";
    public static final String INVALID_STATE_PARAMETER_ERROR_CODE         = "invalid_state_parameter";
    public static final String INVALID_NONCE_PARAMETER_ERROR_CODE         = "invalid_nonce_parameter";
    public static final String INVALID_CODE_PARAMETER_ERROR_CODE          = "invalid_code_parameter";
    public static final String BIND_AFTER_AUTH                            = "bind_after_auth";
    public static final String AUTHORIZATION_REQUEST_NOT_FOUND_ERROR_CODE = "authorization_request_not_found";

    /**
     * Session Attribute
     */
    public static final String BIND_REDIRECT                              = "bind_redirect";

}
