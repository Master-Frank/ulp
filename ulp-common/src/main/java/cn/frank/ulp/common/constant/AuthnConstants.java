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

import cn.frank.ulp.support.constant.EiamConstants;
import static cn.frank.ulp.support.security.constant.SecurityConstants.LOGIN_PATH;

/**
 * 认证常量
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/7/26 21:07
 */
public final class AuthnConstants {
    public final static String AUTHN_PATH   = EiamConstants.V1_API_PATH + "/authn";

    /**
     * 登录配置
     */
    public static final String LOGIN_CONFIG = LOGIN_PATH + "/config";

    /**
     * 前端登录路由
     */
    public static final String FE_LOGIN     = "/login";

}
