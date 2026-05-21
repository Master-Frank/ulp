/*
 * ulp-authentication-dingtalk - United Login Platform
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
package cn.frank.ulp.authentication.dingtalk.constant;

/**
 * 钉钉认证常量
 *
 * @author Frank Zhang
 */
public final class DingTalkAuthenticationConstants {
    public static final String APP_ID                  = "appid";
    public static final String AUTH_CODE               = "authCode";
    public static final String CORP_ID                 = "corpId";
    public static final String GET_USERINFO_BY_CODE    = "https://oapi.dingtalk.com/sns/getuserinfo_bycode";
    public static final String SCAN_CODE_URL_AUTHORIZE = "https://oapi.dingtalk.com/connect/oauth2/sns_authorize";
    public static final String URL_AUTHORIZE           = "https://login.dingtalk.com/oauth2/auth";

    public static final String GET_USERID_BY_UNIONID   = "https://oapi.dingtalk.com/user/getUseridByUnionid";
    public static final String GET_USERINFO_BY_USERID  = "https://oapi.dingtalk.com/topapi/v2/user/get";
}
