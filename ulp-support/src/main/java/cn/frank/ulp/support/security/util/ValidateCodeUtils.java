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
package cn.frank.ulp.support.security.util;

import java.security.SecureRandom;
import java.util.Random;

/**
 * 验证码工具.
 */
public final class ValidateCodeUtils {

    private static final Random RANDOM = new SecureRandom();

    public ValidateCodeUtils() {
    }

    public static Integer generateValidateCode(int length) {
        if (length == 4) {
            int n = RANDOM.nextInt(9999);
            return n < 1000 ? n + 1000 : n;
        }
        if (length == 6) {
            int n = RANDOM.nextInt(999999);
            return n < 100000 ? n + 100000 : n;
        }
        throw new RuntimeException("只能生成 4 位 / 6 位验证码");
    }

    public static String generateValidateCode4String(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }
}
