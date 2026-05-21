/*
 * ulp-openapi - United Login Platform
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
package cn.frank.ulp.openapi.constant;

import static cn.frank.ulp.support.constant.EiamConstants.V1_API_PATH;

/**
 * Open API 常量
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/9/5 21:58
 */
public class OpenApiV1Constants {
    /**
     * OpenAPI 路径
     */
    public final static String  OPEN_API_V1_PATH        = V1_API_PATH;

    public final static Integer ACCESS_TOKEN_EXPIRES_IN = 7200;

    /**
     * 组名称
     */
    public static final String  OPEN_API_NAME           = "开放接口";

    /**
     * 访问凭证
     */
    public final static String  AUTH_PATH               = OPEN_API_V1_PATH + "/auth";

    /**
     * 账户
     */
    public final static String  ACCOUNT_PATH            = OPEN_API_V1_PATH + "/account";

    /**
     * 用户
     */
    public final static String  USER_PATH               = ACCOUNT_PATH + "/user";

    /**
     * 组织
     */
    public final static String  ORGANIZATION_PATH       = ACCOUNT_PATH + "/organization";

    /**
     * 组织
     */
    public final static String  APP_ACCOUNT_PATH        = ACCOUNT_PATH + "/app/account";
}
