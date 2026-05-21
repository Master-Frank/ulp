/*
 * ulp-support - ULP support library (replaces the former eiam-support private jar).
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
package cn.frank.ulp.support.web.filter;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import cn.frank.ulp.support.util.VersionUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 版本头部过滤器
 * 用于在响应中添加版本信息头部
 */
public class VersionHeaderFilter extends OncePerRequestFilter {

    /**
    * 版本头部名称
    */
    public static final String VERSION_HEADER_NAME = "X-Application-Version";

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
        response.addHeader(VERSION_HEADER_NAME, VersionUtils.getVersion());
        filterChain.doFilter(request, response);
    }
}
