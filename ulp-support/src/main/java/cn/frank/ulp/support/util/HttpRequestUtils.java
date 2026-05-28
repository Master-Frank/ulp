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
package cn.frank.ulp.support.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

public class HttpRequestUtils {

    public HttpRequestUtils() {
    }

    public static MultiValueMap<String, String> getFormParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        LinkedMultiValueMap<String, String> result = new LinkedMultiValueMap<>();
        String queryString = StringUtils.hasText(request.getQueryString())
            ? request.getQueryString()
            : "";
        parameterMap.forEach((name, values) -> {
            if (!queryString.contains(name)) {
                for (String value : values) {
                    result.add(name, value);
                }
            }
        });
        return result;
    }

    public static MultiValueMap<String, String> getQueryParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        LinkedMultiValueMap<String, String> result = new LinkedMultiValueMap<>();
        String queryString = StringUtils.hasText(request.getQueryString())
            ? request.getQueryString()
            : "";
        parameterMap.forEach((name, values) -> {
            if (queryString.contains(name)) {
                for (String value : values) {
                    result.add(name, value);
                }
            }
        });
        return result;
    }

    public static Map<String, String> getRequestHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>(16);
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            headers.put(name, request.getHeader(name));
        }
        return headers;
    }

    public static Map<String, String> getRequestParameters(HttpServletRequest request) {
        Map<String, String> result = new HashMap<>(16);
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            String[] values = entry.getValue();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < values.length; i++) {
                sb.append(values[i]);
                if (i != values.length - 1) {
                    sb.append(",");
                }
            }
            result.put(entry.getKey(), sb.toString());
        }
        return result;
    }
}
