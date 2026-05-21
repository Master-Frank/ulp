/*
 * ulp-authentication-qq - United Login Platform
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
package cn.frank.ulp.authentication.qq.constant;

/**
 * 企业微信
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/12/9 22:19
 */
public final class QqAuthenticationConstants {
    /**
     * 获取授权码地址
     */
    public static final String URL_AUTHORIZE        = "https://graph.qq.com/oauth2.0/authorize";
    /**
     * 获取令牌地址
     */
    public static final String URL_GET_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";
    /**
     * 获取 openId 的地址
     */
    public static final String URL_GET_OPEN_ID      = "https://graph.qq.com/oauth2.0/me";
    /**
     * 获取用户信息的地址
     */
    public static final String URL_GET_USER_INFO    = "https://graph.qq.com/user/get_user_info";

}
