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
package cn.frank.ulp.support.util;

import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;

import lombok.NonNull;

/**
 * URL 工具.
 */
public class UrlUtils {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UrlUtils.class);

    public UrlUtils() {
    }

    /**
     * 规范化 URL：把多余的斜杠合并为单斜杠（保留协议部分）.
     */
    public static String format(String url) {
        if (url == null) {
            return null;
        }
        // 不要把 "http://" / "https://" 中的斜杠合并掉
        return url.replaceAll("(?<!:)/{2,}", "/");
    }

    public static boolean testUrlWithTimeOut(String url, int timeoutMillis) {
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.setConnectTimeout(timeoutMillis);
            connection.connect();
            return true;
        } catch (Exception e) {
            log.error("URL [{}] 连通性测试失败: {}", url, e.getMessage());
            return false;
        }
    }

    public static String encodeUriComponent(String input) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(input)) {
            return input;
        }
        StringBuilder sb = new StringBuilder(input.length() * 3);
        String safe = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()";
        for (int i = 0; i < input.length(); i++) {
            String ch = input.substring(i, i + 1);
            if (safe.contains(ch)) {
                sb.append(ch);
            } else {
                byte[] bytes = ch.getBytes(StandardCharsets.UTF_8);
                for (byte b : bytes) {
                    sb.append('%');
                    sb.append(String.format("%02X", b & 0xFF));
                }
            }
        }
        return sb.toString();
    }

    public static String integrate(@NonNull String basicUrl, Map<String, String> parameters) {
        if (basicUrl == null) {
            throw new NullPointerException("basicUrl is marked non-null but is null");
        }
        StringBuilder sb = new StringBuilder(basicUrl).append("?");
        if (MapUtils.isNotEmpty(parameters)) {
            parameters.forEach((k, v) -> sb.append("&").append(k).append("=").append(v));
        }
        // 去除位置 1 处多余的 '&' (basicUrl?&k=v -> basicUrl?k=v)
        int amp = sb.indexOf("?&");
        if (amp >= 0) {
            sb.deleteCharAt(amp + 1);
        }
        return sb.toString();
    }
}
