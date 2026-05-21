/*
 * ulp-support - ULP support library (replaces the former eiam-support private jar).
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
package cn.frank.ulp.support.error;

import java.util.Map;
import java.util.Set;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import com.google.common.collect.Maps;

import cn.frank.ulp.support.exception.UlpException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

public class CustomErrorAttributes extends DefaultErrorAttributes {

    public static final String           ERRORS     = "errors";
    public static final String           REQUEST_ID = "requestId";
    public static final String           SUCCESS    = "success";
    public static final String           MESSAGE    = "message";
    public static final String           STATUS     = "status";
    public static final String           EXCEPTION  = "exception";
    public static final String           TRACE      = "trace";
    public static final String           ERROR      = "error";
    public static final String           PATH       = "path";

    private final ErrorAttributesHandler errorAttributesHandler;

    public CustomErrorAttributes() {
        this.errorAttributesHandler = (throwable) -> Maps.newHashMap();
    }

    public CustomErrorAttributes(ErrorAttributesHandler errorAttributesHandler) {
        this.errorAttributesHandler = errorAttributesHandler;
    }

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest,
                                                  ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
        Throwable error = getError(webRequest);
        Map<String, Object> custom = this.errorAttributesHandler.getErrorAttributes(error);
        if (custom != null) {
            errorAttributes.putAll(custom);
        }
        errorAttributes.put(SUCCESS, Boolean.FALSE);
        errorAttributes.put(REQUEST_ID,
            webRequest.getAttribute("REQUEST_ID", RequestAttributes.SCOPE_REQUEST));

        if (error instanceof UlpException) {
            UlpException ulpException = (UlpException) error;
            errorAttributes.put(MESSAGE, ulpException.getMessage());
            errorAttributes.put(STATUS, ulpException.getHttpStatus().value());
        }

        if (error instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException) error;
            Set<ConstraintViolation<?>> violations = cve.getConstraintViolations();
            if (!violations.isEmpty()) {
                StringBuilder message = new StringBuilder();
                for (ConstraintViolation<?> violation : violations) {
                    message.append(violation.getMessage()).append(";");
                }
                errorAttributes.put(MESSAGE, message.substring(0, message.length() - 1));
            }
        }

        if (error instanceof BindException) {
            BindException bindException = (BindException) error;
            BindingResult bindingResult = bindException.getBindingResult();
            if (bindingResult.hasErrors()) {
                FieldError fieldError = bindingResult.getFieldError();
                if (fieldError != null) {
                    errorAttributes.put(MESSAGE, fieldError.getDefaultMessage());
                }
            }
        }

        if (error instanceof DataAccessException) {
            errorAttributes.put(MESSAGE, "数据访问异常");
        }

        if (options.isIncluded(ErrorAttributeOptions.Include.MESSAGE)
            && errorAttributes.get(MESSAGE) == null) {
            errorAttributes.put(MESSAGE, errorAttributes.get(ERROR));
        }

        return errorAttributes;
    }
}
