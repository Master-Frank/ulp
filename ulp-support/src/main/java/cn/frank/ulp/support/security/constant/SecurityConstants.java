/*
 * ulp-support - ULP support library
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
package cn.frank.ulp.support.security.constant;

/**
 * 安全常量类
 * 定义安全相关的常量
 */
public class SecurityConstants {
    /**
    * 表单登录路径
    */
    public static final String FORM_LOGIN                    = "/api/v1/login";

    /**
    * 登出路径
    */
    public static final String LOGOUT_PATH                   = "/api/v1/logout";

    /**
    * 登录路径
    */
    public static final String LOGIN_PATH                    = "/api/v1/login";

    /**
    * 需要重置密码标识
    */
    public static final String REQUIRE_RESET_PASSWORD        = "require_reset_password";

    /**
    * 重置密码路径
    */
    public static final String RESET_PASSWORD_PATH           = "/api/v1/reset_password";

    /**
    * 密码无效错误
    */
    public static final String PASSWORD_INVALID_ERROR        = "password_invalid_error";

    /**
    * 密码校验失败错误
    */
    public static final String PASSWORD_VALIDATED_FAIL_ERROR = "password_validated_fail_error";

    /**
    * 未知认证类型
    */
    public static final String UNKNOWN_AUTHENTICATION_TYPE   = "unknown_authentication_type";
}