/*
 * ulp-support - United Login Platform
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
package cn.frank.ulp.support.security.password;

import org.jetbrains.annotations.NotNull;

import cn.frank.ulp.support.security.password.exception.PasswordInvalidException;

/**
 * 密码策略管理器接口
 * 定义密码验证的方法
 */
public interface PasswordPolicyManager {

    /**
    * 验证密码
    *
    * @param userDetails 用户详情对象
    * @param password 密码
    * @throws PasswordInvalidException 密码无效异常
    */
    void validate(@NotNull Object userDetails, String password) throws PasswordInvalidException;

    /**
    * 验证密码
    *
    * @param username 用户名
    * @param password 密码
    * @throws PasswordInvalidException 密码无效异常
    */
    void validate(@NotNull String username, String password) throws PasswordInvalidException;
}
