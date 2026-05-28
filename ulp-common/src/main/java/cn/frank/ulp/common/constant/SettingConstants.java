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

import cn.frank.ulp.support.constant.UlpConstants;

/**
 * 设置常量
 *
 * @author Frank Zhang
 */
public final class SettingConstants {
    /**
     * 系统设置API路径
     */
    public final static String SETTING_PATH       = UlpConstants.V1_API_PATH + "/setting";

    /**
     * 安全设置API路径
     */
    public final static String SECURITY_PATH      = SETTING_PATH + "/security";

    /**
     * 系统设置缓存 cacheName
     */
    public static final String SETTING_CACHE_NAME = "setting";

    /**
     * admin 缓存 cacheName
     */
    public static final String ADMIN_CACHE_NAME   = "admin";

    /**
     * 应用AES秘钥
     */
    public static final String AES_SECRET         = "security.aes_secret";

}
