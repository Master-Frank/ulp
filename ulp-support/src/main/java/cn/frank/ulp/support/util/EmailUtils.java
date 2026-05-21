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

import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;

import jakarta.validation.ConstraintValidatorContext;

/**
 * 邮箱工具类
 * 提供邮箱相关的工具方法
 */
public class EmailUtils {

    /**
    * 字符串解密方法
    *
    * @param object 待解密的对象
    * @return 解密后的字符串
    */
    public static String decryptString(Object object) {
        int key1 = 4 << 4 ^ 2 << 2 ^ 5 >> 2;
        int key2 = 5 << 3 ^ 2;
        int key3 = (3 ^ 5) << 4 ^ 2 << 2 ^ 3 & 5;
        String str = (String) object;
        int length = str.length();
        char[] chars = new char[length];
        int index = length - (3 >> 1);
        char[] result = chars;
        int var4 = key3;
        int var5 = index;

        for (int var6 = key2; var5 >= 0; var5 = index) {
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
    * 验证邮箱格式是否正确
    *
    * @param email 邮箱地址
    * @return 邮箱格式是否正确
    */
    public static boolean isEmailValidate(String email) {
        return (new EmailValidator()).isValid(email, (ConstraintValidatorContext) null);
    }
}
