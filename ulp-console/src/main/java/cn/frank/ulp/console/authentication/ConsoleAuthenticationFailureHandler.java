/*
 * ulp-console - United Login Platform
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
package cn.frank.ulp.console.authentication;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import cn.frank.ulp.support.enums.SecretType;
import cn.frank.ulp.support.result.ApiRestResult;
import cn.frank.ulp.support.security.exception.SecretInvalidException;
import cn.frank.ulp.support.util.HttpResponseUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static cn.frank.ulp.support.constant.EiamConstants.CAPTCHA_CODE_SESSION;
import static cn.frank.ulp.support.exception.enums.ExceptionStatus.*;

/**
 * 认证失败
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/9/2 22:11
 */
public class ConsoleAuthenticationFailureHandler implements AuthenticationFailureHandler {

    /**
     * Called when an authentication attempt fails.
     *
     * @param request   the request during which the authentication attempt occurred.
     * @param response  the response.
     * @param exception the exception which was thrown to reject the authentication
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) {
        //@formatter:off
        ApiRestResult.RestResultBuilder<String> builder = ApiRestResult.<String> builder().status(EX000101.getCode()).message(Objects.toString(exception.getMessage(),EX000101.getMessage()));
        if (exception instanceof SecretInvalidException){
            builder = ApiRestResult.<String> builder().status(EX900005.getCode());
        }
        //@formatter:on
        request.getSession().removeAttribute(SecretType.LOGIN.getKey());
        request.getSession().removeAttribute(CAPTCHA_CODE_SESSION);
        HttpResponseUtils.flushResponseJson(response, HttpStatus.BAD_REQUEST.value(),
            builder.build());
    }
}
