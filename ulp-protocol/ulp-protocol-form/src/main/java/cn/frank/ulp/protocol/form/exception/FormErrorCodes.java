/*
 * ulp-protocol-form - United Login Platform
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
package cn.frank.ulp.protocol.form.exception;

/**
 * 异常CODE
 *
 * @author Frank Zhang
 */
public final class FormErrorCodes {

    /**
     * 无效请求
     */
    public static final String INVALID_REQUEST       = "invalid_request";

    /**
     * 应用账户不存在
     */
    public static final String APP_ACCOUNT_NOT_EXIST = "app_account_not_exist";

    /**
     * 无效授权
     */
    public static final String INVALID_GRANT         = "invalid_grant";

    /**
     * 服务器异常
     */
    public static final String SERVER_ERROR          = "server_error";

}
