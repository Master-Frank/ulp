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
package cn.frank.ulp.support.web.decrypt;

import org.springframework.http.HttpStatus;

import cn.frank.ulp.support.exception.UlpException;

/**
 * 解密异常类
 * 用于处理解密过程中发生的异常
 */
public class DecryptException extends UlpException {

    /**
    * 构造函数
    *
    * @param throwable 异常
    */
    public DecryptException(Throwable throwable) {
        super(throwable, "DECRYPT_ERROR", "解密失败", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
    * 构造函数
    *
    * @param message 错误信息
    * @param httpStatus HTTP状态码
    */
    public DecryptException(String message, HttpStatus httpStatus) {
        super("DECRYPT_ERROR", message, httpStatus);
    }
}
