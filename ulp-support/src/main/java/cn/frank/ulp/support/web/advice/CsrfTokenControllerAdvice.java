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
package cn.frank.ulp.support.web.advice;

import java.util.Objects;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import cn.frank.ulp.support.web.filter.CustomRedirectFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * CSRF令牌控制器通知
 * 用于在控制器中添加CSRF令牌属性
 */
@ControllerAdvice
public class CsrfTokenControllerAdvice {

    /**
    * CSRF令牌请求处理器
    */
    private final CsrfTokenRequestHandler requestHandler = new CustomRedirectFilter();

    /**
    * 解码字符串
    *
    * @param str 待解码字符串
    * @return 解码后的字符串
    */
    public static String decodeString(String str) {
        int key1 = 2 << 3 ^ 2;
        int key2 = 4 << 4 ^ (4 ^ 5) << (4 ^ 5);
        int key3 = 5 << 4;
        int length = str.length();
        char[] chars = new char[length];
        int index = length - (4 ^ 5);
        char[] result = chars;
        int var4 = key2;
        int var5 = index;

        for (int var6 = key1; var5 >= 0; var5 = index) {
            int tempIndex = index;
            int charValue = str.charAt(index);
            --index;
            result[tempIndex] = (char) (charValue ^ var6);
            if (index < 0) {
                break;
            }

            int tempIndex2 = index--;
            result[tempIndex2] = (char) (str.charAt(tempIndex2) ^ var4);
        }

        return new String(result);
    }

    /**
    * 添加CSRF令牌属性
    *
    * @param request HTTP请求
    * @param response HTTP响应
    */
    @ModelAttribute
    public void addCsrfToken(HttpServletRequest request, HttpServletResponse response) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (Objects.nonNull(csrfToken)) {
            this.requestHandler.handle(request, response, () -> csrfToken);
        }
    }
}
