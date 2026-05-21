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
package cn.frank.ulp.support.web.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.frank.ulp.support.util.PhoneUtils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * CSRF令牌控制器
 * 用于提供CSRF令牌相关接口
 */
@Controller
public class CsrfTokenController {

    /**
    * CSRF令牌请求处理器
    */
    private final CsrfTokenRequestHandler requestHandler = new CsrfTokenRequestAttributeHandler();

    /**
    * 获取CSRF令牌
    *
    * @param request HTTP请求
    * @return CSRF令牌
    */
    @GetMapping({ "/api/v1/csrf" })
    @ResponseBody
    public CsrfToken csrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute(PhoneUtils.decryptString(
            "\u001e\u000f\u0018\u0018\u001b\u001d\u0018\u001a\u0004\u0011\u0015\u0010\u001e\n\u0012\f\u001a\u001a\u0017\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"));
    }
}
