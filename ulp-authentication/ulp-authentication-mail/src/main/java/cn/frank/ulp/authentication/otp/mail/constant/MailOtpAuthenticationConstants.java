/*
 * ulp-authentication-mail - United Login Platform
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
package cn.frank.ulp.authentication.otp.mail.constant;

import static cn.frank.ulp.support.security.constant.SecurityConstants.LOGIN_PATH;

/**
 * 验证码认证常量
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/12/19 23:19
 */
public final class MailOtpAuthenticationConstants {

    public static final String RECIPIENT_KEY = "recipient";
    public static final String CODE_KEY      = "code";
    /**
     * 验证码登录路径
     */
    public static final String OTP_LOGIN     = LOGIN_PATH + "/otp";
}