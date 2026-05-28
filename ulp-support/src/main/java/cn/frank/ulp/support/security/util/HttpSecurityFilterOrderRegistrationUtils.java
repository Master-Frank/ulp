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
package cn.frank.ulp.support.security.util;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import jakarta.servlet.Filter;

/**
 * HttpSecurity 过滤器顺序工具.
 *
 * <p>简化实现：直接复用 Spring Security 的 {@link HttpSecurity#addFilterAfter} /
 * {@link HttpSecurity#addFilterBefore} —— 这些方法在 Spring Security 已知 Filter 类型时即可正确排序，
 * 自定义 Filter 也会被注册。如未来发现自定义 Filter 触发
 * {@code IllegalArgumentException: The Filter class XXX does not have a registered order}，
 * 再考虑反射调用内部 {@code FilterOrderRegistration#put} 的方案。</p>
 */
public final class HttpSecurityFilterOrderRegistrationUtils {

    public HttpSecurityFilterOrderRegistrationUtils() {
    }

    public static void putFilterAfter(HttpSecurity http, Filter filter,
                                      Class<? extends Filter> registeredFilter) {
        http.addFilterAfter(filter, registeredFilter);
    }

    public static void putFilterBefore(HttpSecurity http, Filter filter,
                                       Class<? extends Filter> registeredFilter) {
        http.addFilterBefore(filter, registeredFilter);
    }
}
