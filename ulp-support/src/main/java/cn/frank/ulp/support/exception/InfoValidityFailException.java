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
package cn.frank.ulp.support.exception;

import org.springframework.http.HttpStatus;

/**
 * 信息有效性验证失败异常
 * 当信息有效性验证失败时抛出此异常
 */
public class InfoValidityFailException extends UlpException {
    /**
     * 构造函数
     *
     * @param message 异常消息
     * @param httpStatus HTTP状态码
     */
    public InfoValidityFailException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    /**
     * 构造函数
     *
     * @param message 异常消息
     * @param cause 异常原因
     */
    public InfoValidityFailException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造函数
     *
     * @param errorCode 错误码
     * @param message 异常消息
     * @param httpStatus HTTP状态码
     */
    public InfoValidityFailException(String errorCode, String message, HttpStatus httpStatus) {
        super(errorCode, message, httpStatus);
    }

    /**
     * 构造函数
     *
     * @param message 异常消息
     */
    public InfoValidityFailException(String message) {
        super(message);
    }

    /**
     * 构造函数
     *
     * @param cause 异常原因
     * @param errorCode 错误码
     * @param message 异常消息
     * @param httpStatus HTTP状态码
     */
    public InfoValidityFailException(Throwable cause, String errorCode, String message,
                                     HttpStatus httpStatus) {
        super(cause, errorCode, message, httpStatus);
    }
}