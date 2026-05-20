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
