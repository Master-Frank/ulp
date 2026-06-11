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
package cn.frank.ulp.support.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 字符串工具.
 */
public class StringUtils {

    public static final String   SPLIT_DEFAULT = ",";

    private static final Pattern BLANK_PATTERN = Pattern.compile("\\s*|\t|\r|\n");

    private static final char[]  HEX_DIGITS    = "0123456789ABCDEF".toCharArray();

    public StringUtils() {
    }

    public static String escapeLike(String input) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(input)) {
            return input;
        }
        return input.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
    }

    public static String byteToHexString(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(HEX_DIGITS[(b >> 4) & 0x0F]);
            sb.append(HEX_DIGITS[b & 0x0F]);
        }
        return sb.toString();
    }

    public static byte[] hexStringToBytes(String hex) {
        if (hex == null || hex.isEmpty()) {
            return null;
        }
        String upper = hex.toUpperCase();
        int len = upper.length() / 2;
        char[] chars = upper.toCharArray();
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) ((charToByte(chars[pos]) << 4) | charToByte(chars[pos + 1]));
        }
        return result;
    }

    public static String replaceBlank(String input) {
        if (input == null) {
            return "";
        }
        return BLANK_PATTERN.matcher(input).replaceAll("");
    }

    public static Map<Character, Long> statisticsCharCount(String input) {
        Map<Character, Long> result = new HashMap<>();
        if (input == null) {
            return result;
        }
        for (char c : input.toCharArray()) {
            result.merge(c, 1L, Long::sum);
        }
        return result;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
