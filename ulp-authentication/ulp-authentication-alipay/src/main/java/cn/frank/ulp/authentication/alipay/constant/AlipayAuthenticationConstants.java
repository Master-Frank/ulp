/*
 * ulp-authentication-alipay - United Login Platform
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
package cn.frank.ulp.authentication.alipay.constant;

/**
 * 支付宝 认证常量
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/8/19 15:18
 */
public class AlipayAuthenticationConstants {

    public static final String AUTHORIZATION_REQUEST = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm";

    public static final String USER_INFO_SCOPE       = "auth_user";

    public static final String APP_ID                = "app_id";

    public static final String AUTH_CODE             = "auth_code";

    public static final String SUCCESS_CODE          = "200";

}
