/*
 * ulp-core - United Login Platform
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
package cn.frank.ulp.core.message;

import java.io.Serializable;

/**
 * 内置变量
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/25 21:52
 */
public final class MsgVariable implements Serializable {

    /**
     * 客户名称
     */
    public static final String CLIENT_NAME        = "client_name";
    /**
     * 时间点
     */
    public static final String TIME               = "time";
    /**
     * 客户描述
     */
    public static final String CLIENT_DESCRIPTION = "client_description";
    /**
     * 用户邮箱
     */
    public static final String USER_EMAIL         = "user_email";
    /**
     * 验证码
     */
    public static final String VERIFY_CODE        = "verify_code";

    /**
     * 剩余天数
     */
    public static final String EXPIRE_DAYS        = "expire_days";

    /**
     * 密码
     */
    public static final String PASSWORD           = "password";

    public static final String TEST               = "test";

    /**
     * 失效时间key
     */
    public static final String EXPIRE_TIME_KEY    = "expire_time";
    /**
     * 发送间隔时间
     */
    public static final int    TIME_TO_LIVE       = 1;
}
