/*
 * ulp-authentication-gitee - United Login Platform
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
package cn.frank.ulp.authentication.gitee.constant;

/**
 * Gitee 认证常量
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/12/9 22:19
 */
public final class GiteeAuthenticationConstants {

    public static final String AUTHORIZATION_REQUEST = "https://gitee.com/oauth/authorize";
    public static final String ACCESS_TOKEN          = "https://gitee.com/oauth/token";
    public static final String USER_INFO             = "https://gitee.com/api/v5/user";
    public static final String ERROR_CODE            = "error";
    public static final String USER_INFO_SCOPE       = "user_info";
    public static final String ID                    = "id";

}
