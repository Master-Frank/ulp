/*
 * ulp-portal - United Login Platform
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
package cn.frank.ulp.portal.constant;

import static cn.frank.ulp.support.constant.EiamConstants.V1_API_PATH;

/**
 * 认证常量
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/12/8 21:29
 */
public class PortalConstants {

    public static final String ACCOUNT_PATH            = V1_API_PATH + "/account";

    /**
     * 忘记密码预认证
     */
    public static final String PREPARE_FORGET_PASSWORD = "/prepare_forget_password";

    /**
     * 忘记密码
     */
    public static final String FORGET_PASSWORD         = "/forget_password";

    /**
     * 忘记密码发送验证码
     */
    public static final String FORGET_PASSWORD_CODE    = "/forget_password_code";
}
