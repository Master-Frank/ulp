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
import java.util.Objects;
import java.util.function.Supplier;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import cn.frank.ulp.support.util.PhoneUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 自定义重定向过滤器
 * 用于处理特定路径的重定向逻辑
 */
public class CustomRedirectFilter extends GenericFilterBean implements CsrfTokenRequestHandler {
   
   /**
    * 重定向请求匹配器
    */
   private static final RequestMatcher REDIRECT_URL_MATCHER = new AntPathRequestMatcher("/redirect", "GET");

   /**
    * 过滤方法
    * 
    * @param servletRequest Servlet请求
    * @param servletResponse Servlet响应
    * @param filterChain 过滤器链
    * @throws IOException IO异常
    * @throws ServletException Servlet异常
    */
   public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
      HttpServletRequest request = (HttpServletRequest) servletRequest;
      HttpServletResponse response = (HttpServletResponse) servletResponse;
      if (REDIRECT_URL_MATCHER.matches(request)) {
         String redirectUrl = request.getParameter(PhoneUtils.decryptString("k\u001a\u001c\u0017\u0017\u0018\u0019\u0017"));
         if (Objects.nonNull(redirectUrl)) {
            response.sendRedirect(redirectUrl);
            return;
         }
      }

      filterChain.doFilter(servletRequest, servletResponse);
   }

   /**
    * 处理CSRF令牌请求
    * 
    * @param request HTTP请求
    * @param response HTTP响应
    * @param csrfTokenSupplier CSRF令牌提供者
    */
   public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfTokenSupplier) {
      CsrfToken csrfToken = csrfTokenSupplier.get();
      response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
   }
}
