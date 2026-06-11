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
package cn.frank.ulp.support.exception;

/**
 * 参数错误异常
 * 当传递的参数不符合要求时抛出此异常
 */
public class BadParamsException extends UlpException {
    /**
     * 构造函数
     *
     * @param message 异常消息
     */
    public BadParamsException(String message) {
        super(message);
    }

    /**
     * 默认构造函数
     */
    public BadParamsException() {
        super("参数错误");
    }

    /**
     * 构造函数
     *
     * @param message 异常消息
     * @param cause 异常原因
     */
    public BadParamsException(String message, Throwable cause) {
        super(message, cause);
    }
}