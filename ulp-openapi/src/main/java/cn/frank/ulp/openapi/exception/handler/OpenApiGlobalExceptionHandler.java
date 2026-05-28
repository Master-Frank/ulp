/*
 * ulp-openapi - United Login Platform
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
package cn.frank.ulp.openapi.exception.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import cn.frank.ulp.openapi.common.OpenApiResponse;
import cn.frank.ulp.openapi.constant.OpenApiStatus;
import cn.frank.ulp.openapi.exception.OpenApiException;

import lombok.AllArgsConstructor;

import jakarta.validation.ConstraintViolationException;

/**
 * 全局异常处理
 *
 * @author Frank Zhang
 */
@AllArgsConstructor
@RestControllerAdvice
public class OpenApiGlobalExceptionHandler {
    /**
     * Exception
     *
     * @return {@link OpenApiResponse}
     */
    @ExceptionHandler(value = Exception.class)
    public OpenApiResponse exception(Exception e) {
        return new OpenApiResponse(OpenApiStatus.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
    }

    /**
     * ConstraintViolationException
     *
     * @return {@link ModelAndView}
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public OpenApiResponse constraintViolationException(ConstraintViolationException e) {
        return new OpenApiResponse(OpenApiStatus.INVALID_PARAMETER.getCode(), e.getMessage());
    }

    /**
     * OpenApiException
     *
     * @return {@link OpenApiResponse}
     */
    @ExceptionHandler(value = OpenApiException.class)
    public OpenApiResponse exception(OpenApiException e) {
        return new OpenApiResponse(e.getCode(), e.getMessage());
    }
}
