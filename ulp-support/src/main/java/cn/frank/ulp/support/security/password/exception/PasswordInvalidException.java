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
package cn.frank.ulp.support.security.password.exception;

import org.springframework.http.HttpStatus;

import cn.frank.ulp.support.exception.UlpException;

public class PasswordInvalidException extends UlpException {

    public PasswordInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordInvalidException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public PasswordInvalidException(String message, HttpStatus status) {
        super(message, status);
    }

    public PasswordInvalidException(Throwable cause, String errorCode, String message,
                                    HttpStatus status) {
        super(cause, errorCode, message, status);
    }

    public PasswordInvalidException(String errorCode, String message, HttpStatus status) {
        super(errorCode, message, status);
    }

    public PasswordInvalidException(String message, Throwable cause, String errorCode,
                                    HttpStatus status) {
        super(cause, errorCode, message, status);
    }
}
