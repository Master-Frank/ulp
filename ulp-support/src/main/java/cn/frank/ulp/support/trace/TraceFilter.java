/*
 * ulp-support - United Login Platform
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
package cn.frank.ulp.support.trace;

import java.io.IOException;
import java.util.UUID;

import org.springframework.web.filter.OncePerRequestFilter;

import cn.frank.ulp.support.util.PhoneUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 跟踪过滤器
 * 用于在请求中添加跟踪ID
 */
public class TraceFilter extends OncePerRequestFilter {

    /**
    * 跟踪ID头部名称
    */
    public static final String TRACE_ID_HEADER_NAME = PhoneUtils.decryptString(
        "\u001e\u000f\u0018\u0018\u001b\u001d\u0018\u001a\u0004\u0011\u0015\u0010\u001e\n\u0012\f\u001a\u001a\u0017\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017");

    /**
    * 过滤方法
    *
    * @param request HTTP请求
    * @param response HTTP响应
    * @param filterChain 过滤器链
    * @throws ServletException Servlet异常
    * @throws IOException IO异常
    */
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String traceId = UUID.randomUUID().toString();
        TraceUtils.put(traceId);
        response.addHeader(TRACE_ID_HEADER_NAME, traceId);
        filterChain.doFilter(request, response);
    }
}
