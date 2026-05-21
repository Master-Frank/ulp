/*
 * ulp-authentication-wechat - United Login Platform
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
package cn.frank.ulp.authentication.wechat.constant;

/**
 * 微信认证常量
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/12/9 22:19
 */
public final class WeChatAuthenticationConstants {
    /**
     * 微信扫码登录常量
     */
    public static class QrConnect {

        public static final String QR_CONNECT_AUTHORIZATION_REQUEST = "https://open.weixin.qq.com/connect/qrconnect";
        public static final String ACCESS_TOKEN                     = "https://api.weixin.qq.com/sns/oauth2/access_token";
        public static final String USER_INFO                        = "https://api.weixin.qq.com/sns/userinfo";
        public static final String HEADIMGURL                       = "headimgurl";
        public static final String NICK_NAME                        = "nickname";
        public static final String UNION_ID                         = "unionid";
        public static final String LANG                             = "lang";
        public static final String APP_ID                           = "appId";
        public static final String SNSAPI_LOGIN                     = "snsapi_login";
        public static final String ERROR_CODE                       = "errcode";
        public static final String SECRET                           = "secret";
        public static final String HREF                             = "href";
    }

    public static class WebPage {

        /**
         * 微信公众号webpage登录
         */
        public static final String WEB_PAGE_AUTHORIZATION_REQUEST = "https://open.weixin.qq.com/connect/oauth2/authorize";
        public static final String ACCESS_TOKEN                   = "https://api.weixin.qq.com/sns/oauth2/access_token";
        public static final String USER_INFO                      = "https://api.weixin.qq.com/sns/userinfo";

        public static final String APP_ID                         = "appId";

        public static final String SNSAPI_BASE                    = "snsapi_base";
        public static final String SNSAPI_USERINFO                = "snsapi_userinfo";
        /**
         * 无论直接打开还是做页面302重定向时候，必须带此参数
         */
        public static final String WECHAT_REDIRECT                = "wechat_redirect";
    }

}
