/*
 * eiam-openapi - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.openapi.authorization;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.frank.ulp.openapi.constant.OpenApiStatus;
import cn.frank.ulp.support.security.web.AbstractAuthenticationEntryPoint;
import cn.frank.ulp.support.util.HttpResponseUtils;
import cn.frank.ulp.support.web.useragent.UserAgentParser;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/6/25 21:55
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
