/*
 * ulp-console - United Login Platform
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
package cn.frank.ulp.console.service.user;

import cn.frank.ulp.console.pojo.update.user.*;

/**
 * 账户服务
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/4/3 22:20
 */
public interface UserProfileService {

    /**
     * 更新用户
     *
     * @param param {@link UpdateUserInfoRequest}
     * @return {@link Boolean}
     */
    Boolean changeInfo(UpdateUserInfoRequest param);

    /**
     * 修改密码
     *
     * @param param   {@link ChangePasswordRequest}
     * @return Boolean
     */
    Boolean changePassword(ChangePasswordRequest param);

    /**
     * 修改手机
     *
     * @param param      {@link PrepareChangePhoneRequest}
     * @return {@link Boolean}
     */
    Boolean prepareChangePhone(PrepareChangePhoneRequest param);

    /**
     * 修改手机
     *
     * @param param {@link ChangePhoneRequest}
     * @return {@link Boolean}
     */
    Boolean changePhone(ChangePhoneRequest param);

    /**
     * 准备修改邮箱
     *
     * @param param {@link PrepareChangeEmailRequest}
     * @return {@link Boolean}
     */
    Boolean prepareChangeEmail(PrepareChangeEmailRequest param);

    /**
     * 修改邮箱
     *
     * @param param      {@link ChangeEmailRequest}
     * @return {@link Boolean}
     */
    Boolean changeEmail(ChangeEmailRequest param);

    /**
     * 忘记密码发送验证码
     *
     * @param recipient {@link String} 验证码接收者（邮箱/手机号）
     * @return {@link Boolean}
     */
    Boolean forgetPasswordCode(String recipient);

    /**
     * 忘记密码预认证
     *
     * @param recipient {@link String} 验证码接收者（邮箱/手机号）
     * @param code {@link String} 验证码
     * @return {@link Boolean} 忘记密码 Token
     */
    Boolean prepareForgetPassword(String recipient, String code);

    /**
     * 忘记密码
     *
     * @param forgetPasswordRequest {@link ForgetPasswordRequest}
     * @return {@link Boolean}
     */
    Boolean forgetPassword(ForgetPasswordRequest forgetPasswordRequest);

}
