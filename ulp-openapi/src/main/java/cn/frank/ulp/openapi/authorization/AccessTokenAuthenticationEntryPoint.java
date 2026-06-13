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
package cn.frank.ulp.openapi.authorization;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

import com.fasterxml.jackson.annotation.JsonProperty;

import cn.frank.ulp.openapi.constant.OpenApiStatus;
import cn.frank.ulp.support.security.web.AbstractAuthenticationEntryPoint;
import cn.frank.ulp.support.util.HttpResponseUtils;
import cn.frank.ulp.support.web.useragent.UserAgentParser;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

/**
 *
 * @author Frank Zhang
 */
public final class AccessTokenAuthenticationEntryPoint extends AbstractAuthenticationEntryPoint {

    public AccessTokenAuthenticationEntryPoint(UserAgentParser userAgentParser) {
        super(userAgentParser);
    }

    /**
     * Collect error details from the provided parameters and format according to RFC
     * 6750, specifically {@code error}, {@code error_description}, {@code error_uri}, and
     * {@code scope}.
     * @param request that resulted in an <code>AuthenticationException</code>
     * @param httpServletResponse so that the user agent can begin authentication
     * @param authException that caused the invocation
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse httpServletResponse,
                         AuthenticationException authException) throws ServletException,
                                                                IOException {
        super.commence(request, httpServletResponse, authException);
        Response response = new Response();
        response.setCode(OpenApiStatus.INVALID_ACCESS_TOKEN.getCode());
        response.setMsg(OpenApiStatus.INVALID_ACCESS_TOKEN.getDesc());
        HttpResponseUtils.flushResponseJson(httpServletResponse, HttpStatus.OK.value(),
            objectMapper, response);
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Data
    public static class Response {

        /**
         * code
         */
        @JsonProperty(value = "code")
        @Schema(name = "code")
        private String code;

        /**
         * msg
         */
        @JsonProperty(value = "msg")
        @Schema(name = "msg")
        private String msg;
    }

}
