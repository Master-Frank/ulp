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
package cn.frank.ulp.support.web.controller;

import java.io.IOException;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import cn.frank.ulp.support.security.savedredirect.HttpSessionRedirectCache;
import cn.frank.ulp.support.security.savedredirect.RedirectCache;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 跳转控制器
 * 用于处理各种跳转请求
 */
@Controller
public class JumpController {
   
   /**
    * 重定向缓存
    */
   private final RedirectCache redirectCache = new HttpSessionRedirectCache();
   
   /**
    * 保存的请求属性名
    */
   public static final String SAVED_REQUEST = "SAVED_REQUEST";

   /**
    * 退出登录跳转
    * 
    * @param request HTTP请求
    * @param response HTTP响应
    * @throws IOException IO异常
    */
   @GetMapping({"/api/v1/logout"})
   public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
      try {
         request.logout();
      } catch (Exception e) {
         // 忽略异常
      }
      response.setStatus(HttpStatus.OK.value());
   }

   /**
    * 跳转到指定URL
    * 
    * @param request HTTP请求
    * @param code 跳转码
    * @return 重定向视图
    */
   @GetMapping({"/api/v1/jump"})
   public RedirectView jump(HttpServletRequest request, @RequestParam("code") String code) {
      String redirectUrl = (String) request.getSession().getAttribute(code);
      if (Objects.isNull(redirectUrl)) {
         redirectUrl = "/";
      } else {
         request.getSession().removeAttribute(code);
      }

      RedirectView redirectView = new RedirectView();
      redirectView.setUrl(redirectUrl);
      redirectView.setExpandUriTemplateVariables(Boolean.FALSE);
      return redirectView;
   }

   /**
    * 登录成功跳转
    * 
    * @param request HTTP请求
    * @return 重定向视图
    */
   @GetMapping({"/login/success"})
   public RedirectView loginSuccess(HttpServletRequest request) {
      String redirectUrl = (String) request.getSession().getAttribute(SAVED_REQUEST);
      if (Objects.isNull(redirectUrl)) {
         redirectUrl = "/";
      }

      request.getSession().removeAttribute(SAVED_REQUEST);
      RedirectView redirectView = new RedirectView();
      redirectView.setUrl(redirectUrl);
      redirectView.setExpandUriTemplateVariables(Boolean.FALSE);
      return redirectView;
   }
}
