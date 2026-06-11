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
package cn.frank.ulp.support.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 密钥无效异常类
 * 当密钥验证失败时抛出此异常
 */
public class SecretInvalidException extends AuthenticationException {

    /**
    * 构造函数
    *
    * @param message 异常消息
    */
    public SecretInvalidException(String message) {
        super(message);
    }

    /**
    * 构造函数
    *
    * @param message 异常消息
    * @param cause 异常原因
    */
    public SecretInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}