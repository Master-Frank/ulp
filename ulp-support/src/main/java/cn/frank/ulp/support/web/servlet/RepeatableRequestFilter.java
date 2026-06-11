/*
 * ulp-support - ULP support library
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
package cn.frank.ulp.support.web.servlet;

import java.io.IOException;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 可重复请求过滤器类
 * 用于包装请求，使其可以被多次读取
 */
public class RepeatableRequestFilter extends OncePerRequestFilter {

    /**
    * 执行过滤
    *
    * @param httpServletRequest HTTP请求
    * @param httpServletResponse HTTP响应
    * @param filterChain 过滤器链
    * @throws ServletException Servlet异常
    * @throws IOException IO异常
    */
    @Override
    public void doFilterInternal(@NonNull HttpServletRequest httpServletRequest,
                                 @NonNull HttpServletResponse httpServletResponse,
                                 @NotNull FilterChain filterChain) throws ServletException,
                                                                   IOException {
        RepeatedlyRequestWrapper requestWrapper = null;
        if (StringUtils.startsWithIgnoreCase(httpServletRequest.getContentType(),
            "application/json")) {
            requestWrapper = new RepeatedlyRequestWrapper(httpServletRequest, httpServletResponse);
        }

        filterChain.doFilter(
            (ServletRequest) Objects.requireNonNullElse(requestWrapper, httpServletRequest),
            httpServletResponse);
    }
}
