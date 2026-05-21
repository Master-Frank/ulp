/*
 * ulp-portal - United Login Platform
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
package cn.frank.ulp.portal.authentication;

import java.io.IOException;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;

import cn.frank.ulp.core.context.ContextService;
import cn.frank.ulp.support.result.ApiRestResult;
import cn.frank.ulp.support.security.web.AbstractAuthenticationEntryPoint;
import cn.frank.ulp.support.util.HttpResponseUtils;
import cn.frank.ulp.support.web.useragent.UserAgentParser;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import static cn.frank.ulp.support.context.ServletContextService.isHtmlRequest;

/**
 * 认证入口点
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/9/2 22:11
 */
@SuppressWarnings("DuplicatedCode")
public class PortalAuthenticationEntryPoint extends AbstractAuthenticationEntryPoint {
    /**
     * 日志
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Commences an authentication scheme.
     * <p>
     * <code>ExceptionTranslationFilter</code> will populate the <code>HttpSession</code>
     * attribute named
     * <code>AbstractAuthenticationProcessingFilter.SPRING_SECURITY_SAVED_REQUEST_KEY</code>
     * with the requested target URL before calling this method.
     * <p>
     * Implementations should modify the headers on the <code>ServletResponse</code> as
     * necessary to commence the authentication process.
     *
     * @param request       that resulted in an <code>AuthenticationException</code>
     * @param response      so that the user agent can begin authentication
     * @param authException that caused the invocation
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException,
                                                                ServletException {
        super.commence(request, response, authException);
        //判断请求
        boolean isHtmlRequest = isHtmlRequest(request);
        //JSON
        if (!isHtmlRequest) {
            ApiRestResult<Object> result = ApiRestResult.builder()
                .status(String.valueOf(UNAUTHORIZED.value()))
                .message(
                    Objects.toString(authException.getMessage(), UNAUTHORIZED.getReasonPhrase()))
                .build();
            HttpResponseUtils.flushResponseJson(response, UNAUTHORIZED.value(), result);
        }
        // HTML
        else {
            //跳转前端SESSION过期路由
            response.sendRedirect(ContextService.getPortalLoginUrl());
        }
    }

    public PortalAuthenticationEntryPoint(UserAgentParser userAgentParser) {
        super(userAgentParser);
    }
}
