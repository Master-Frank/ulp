/*
 * ulp-protocol-jwt - United Login Platform
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
package cn.frank.ulp.protocol.jwt.constant;

import static cn.frank.ulp.protocol.code.constant.ProtocolConstants.PROTOCOL_CACHE_PREFIX;
import static cn.frank.ulp.support.constant.EiamConstants.COLON;

/**
 * 协议常量
 *
 * @author Frank Zhang
 */
public class JwtProtocolConstants {

    /**
     * 协议缓存前缀
     */
    public static final String JWT_PROTOCOL_CACHE_PREFIX = PROTOCOL_CACHE_PREFIX + "jwt" + COLON;

    public static final String TARGET_URL                = "target_url";
    public static final String ID_TOKEN                  = "id_token";
    public static final String NONCE                     = "nonce";

    public static final String URL                       = "url";
    public static final String BINDING_TYPE              = "binding_type";

    public static final String JWT_ERROR_URI             = "";

    public static final String POST_LOGOUT_REDIRECT_URI  = "post_logout_redirect_uri";

}
