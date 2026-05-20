/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.support.security.password.exception;

import org.springframework.http.HttpStatus;

import cn.topiam.employee.support.exception.TopIamException;

public class PasswordInvalidException extends TopIamException {

    public PasswordInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordInvalidException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public PasswordInvalidException(String message, HttpStatus status) {
        super(message, status);
    }

    public PasswordInvalidException(Throwable cause, String errorCode, String message, HttpStatus status) {
        super(cause, errorCode, message, status);
    }

    public PasswordInvalidException(String errorCode, String message, HttpStatus status) {
        super(errorCode, message, status);
    }

    public PasswordInvalidException(String message, Throwable cause, String errorCode, HttpStatus status) {
        super(cause, errorCode, message, status);
    }
}

