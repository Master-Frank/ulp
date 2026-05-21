/*
 * ulp-protocol-jwt - United Login Platform
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
package cn.frank.ulp.protocol.jwt.authentication;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import cn.frank.ulp.protocol.jwt.endpoint.http.converter.JwtErrorHttpMessageConverter;
import cn.frank.ulp.protocol.jwt.exception.JwtAuthenticationException;
import cn.frank.ulp.protocol.jwt.exception.JwtError;
import cn.frank.ulp.protocol.jwt.exception.JwtErrorCodes;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static cn.frank.ulp.support.context.ServletContextService.isHtmlRequest;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/9/4 13:03
 */
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {
    /**
     * Called when an authentication attempt fails.
     *
     * @param request   the request during which the authentication attempt occurred.
     * @param response  the response.
     * @param exception the exception which was thrown to reject the authentication
     *                  request.
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException,
                                                                           ServletException {
        if (exception instanceof JwtAuthenticationException) {
            ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
            JwtError error = ((JwtAuthenticationException) exception).getError();
            boolean isHtmlRequest = isHtmlRequest(request);
            //JSON
            if (!isHtmlRequest) {
                if (error.getErrorCode().equals(JwtErrorCodes.SERVER_ERROR)) {
                    httpResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                }
                if (error.getErrorCode().equals(JwtErrorCodes.INVALID_REQUEST)) {
                    httpResponse.setStatusCode(HttpStatus.BAD_REQUEST);
                }
                errorHttpResponseConverter.write(error, null, httpResponse);
                return;
            }
            //Html
            response.sendError(HttpStatus.BAD_REQUEST.value(), error.toString());
        }
    }

    /**
     * 错误响应处理器
     */
    private final HttpMessageConverter<JwtError> errorHttpResponseConverter = new JwtErrorHttpMessageConverter();

}
