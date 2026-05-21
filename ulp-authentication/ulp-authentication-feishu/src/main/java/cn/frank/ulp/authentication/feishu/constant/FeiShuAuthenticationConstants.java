/*
 * ulp-authentication-feishu - United Login Platform
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
package cn.frank.ulp.authentication.feishu.constant;

/**
 * 飞书认证常量
 *
 * @author Frank Zhang
 */
public final class FeiShuAuthenticationConstants {

    public static final String AUTHORIZATION_REQUEST = "https://passport.feishu.cn/suite/passport/oauth/authorize";
    public static final String ACCESS_TOKEN          = "https://passport.feishu.cn/suite/passport/oauth/token";
    public static final String USER_INFO             = "https://passport.feishu.cn/suite/passport/oauth/userinfo";

    public static final String CLIENT_ID             = "client_id";
    public static final String CLIENT_SECRET         = "client_secret";
    public static final String OPEN_ID               = "open_id";

    public static final String CODE                  = "code";
    public static final String HREF                  = "href";

}