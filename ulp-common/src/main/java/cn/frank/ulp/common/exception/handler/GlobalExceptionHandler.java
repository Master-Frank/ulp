/*
 * ulp-common - United Login Platform
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
package cn.frank.ulp.common.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.webmvc.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import cn.frank.ulp.support.exception.UlpException;

import lombok.AllArgsConstructor;

import jakarta.validation.ConstraintViolationException;

/**
 * 全局异常处理
 *
 * @author Frank Zhang
 */
@AllArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Exception
     *
     * @return {@link ModelAndView}
     */
    @ExceptionHandler(value = Exception.class)
    public ModelAndView exception(WebRequest request, Exception e) {
        request.setAttribute(WebUtils.ERROR_STATUS_CODE_ATTRIBUTE,
            HttpStatus.INTERNAL_SERVER_ERROR.value(), WebRequest.SCOPE_REQUEST);
        setExceptionAttribute(request, e);
        logger.error("Global exception catch", e);
        return new ModelAndView(webProperties.getError().getPath());
    }

    /**
     * UlpException
     *
     * @return {@link ModelAndView}
     */
    @ExceptionHandler(value = UlpException.class)
    public ModelAndView ulpException(WebRequest request, UlpException e) {
        request.setAttribute(WebUtils.ERROR_STATUS_CODE_ATTRIBUTE, e.getHttpStatus().value(),
            WebRequest.SCOPE_REQUEST);
        logger.error("Global exception catch", e);
        return new ModelAndView(webProperties.getError().getPath());
    }

    /**
     * BindException
     * HandlerExceptionResolver 默认处理为400状态码，这里设置为500，并转发到异常处理页面。
     *
     * @return {@link ModelAndView}
     */
    @ExceptionHandler(value = BindException.class)
    public ModelAndView bindException(WebRequest request, BindException e) {
        request.setAttribute(WebUtils.ERROR_STATUS_CODE_ATTRIBUTE, HttpStatus.BAD_REQUEST.value(),
            WebRequest.SCOPE_REQUEST);
        logger.error("Global exception catch", e);
        return new ModelAndView(webProperties.getError().getPath());
    }

    /**
     * ConstraintViolationException
     * HandlerExceptionResolver 默认处理为400状态码，这里设置为500，并转发到异常处理页面。
     *
     * @return {@link ModelAndView}
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ModelAndView constraintViolationException(WebRequest request,
                                                     ConstraintViolationException e) {
        request.setAttribute(WebUtils.ERROR_STATUS_CODE_ATTRIBUTE, HttpStatus.BAD_REQUEST.value(),
            WebRequest.SCOPE_REQUEST);
        logger.error("Global exception catch", e);
        return new ModelAndView(webProperties.getError().getPath());
    }

    private void setExceptionAttribute(WebRequest request, Exception exception) {
        request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, exception,
            WebRequest.SCOPE_REQUEST);
        request.setAttribute(DefaultErrorAttributes.class.getName() + ".ERROR", exception,
            WebRequest.SCOPE_REQUEST);
        request.setAttribute(WebUtils.ERROR_EXCEPTION_TYPE_ATTRIBUTE, exception.getClass(),
            WebRequest.SCOPE_REQUEST);
    }

    /**
     * WebProperties
     */
    private final WebProperties webProperties;
}
