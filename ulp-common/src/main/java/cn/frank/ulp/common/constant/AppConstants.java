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

import static cn.frank.ulp.support.constant.EiamConstants.COLON;
import static cn.frank.ulp.support.constant.EiamConstants.V1_API_PATH;

/**
 * 应用管理常量
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/7/26 21:07
 */
public final class AppConstants {

    /**
     * 应用管理API路径
     */
    public final static String APP_PATH                    = V1_API_PATH + "/app";

    /**
     * APP 配置缓存前缀
     */
    public static final String APP_CACHE_NAME_PREFIX       = "app" + COLON;

    /**
     * 分组配置缓存前缀
     */
    public static final String APP_GROUP_CACHE_NAME_PREFIX = "app_group" + COLON;

    /**
     * APP 应用基本信息
     */
    public static final String APP_CACHE_NAME              = APP_CACHE_NAME_PREFIX + "basic";

    /**
     * 应用账户缓存名称
     */
    public static final String APP_ACCOUNT_CACHE_NAME      = APP_CACHE_NAME_PREFIX + "account";

    /**
     * OIDC 配置缓存名称
     */
    public static final String OIDC_CONFIG_CACHE_NAME      = APP_CACHE_NAME_PREFIX + "oidc";

    /**
     * APP Cert
     */
    public static final String APP_CERT_CACHE_NAME         = APP_CACHE_NAME_PREFIX + "cert";

    /**
     * FORM 配置缓存名称
     */
    public static final String FORM_CONFIG_CACHE_NAME      = APP_CACHE_NAME_PREFIX + "form";

    /**
     * JWT 配置缓存名称
     */
    public static final String JWT_CONFIG_CACHE_NAME       = APP_CACHE_NAME_PREFIX + "jwt";

}
