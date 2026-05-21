/*
 * ulp-protocol-oidc - United Login Platform
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
package cn.frank.ulp.protocol.oidc.authentication;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.http.converter.OAuth2ErrorHttpMessageConverter;

import cn.frank.ulp.support.security.web.AbstractAuthenticationEntryPoint;
import cn.frank.ulp.support.web.useragent.UserAgentParser;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static org.springframework.security.oauth2.core.OAuth2ErrorCodes.ACCESS_DENIED;

import static cn.frank.ulp.protocol.oidc.constant.OidcProtocolConstants.OIDC_ERROR_URI;

/**
 * 需要客户端身份验证
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/12/9 18:18
 */
public class ClientAuthenticationRequiredEntryPoint extends AbstractAuthenticationEntryPoint {

    public ClientAuthenticationRequiredEntryPoint(UserAgentParser userAgentParser) {
        super(userAgentParser);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException,
                                                                ServletException {
        super.commence(request, response, authException);
        //@formatter:off
        OAuth2Error error=new OAuth2Error(ACCESS_DENIED,authException.getMessage(),OIDC_ERROR_URI);
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
        OAuth2Error responseError=new OAuth2Error(error.getErrorCode(),error.getDescription(),OIDC_ERROR_URI);
        errorHttpResponseConverter.write(responseError, null, httpResponse);
        //@formatter:on
    }

    private final HttpMessageConverter<OAuth2Error> errorHttpResponseConverter = new OAuth2ErrorHttpMessageConverter();

}
