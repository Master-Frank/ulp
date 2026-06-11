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

import java.util.Objects;

import cn.frank.ulp.support.Version;

/**
 * 版本工具类
 * 提供版本相关的工具方法
 */
public final class VersionUtils {

    /**
    * 字符串解密方法
    *
    * @param object 待解密的对象
    * @return 解密后的字符串
    */
    public static String decryptString(Object object) {
        int key1 = 5 << 4;
        int key2 = 5 << 3;
        int key3 = (3 ^ 5) << 4 ^ 2 ^ 5;
        String str = (String) object;
        int length = str.length();
        char[] chars = new char[length];
        int index = length - (5 >> 2);
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
    * 私有构造函数，防止实例化
    */
    private VersionUtils() {
    }

    /**
    * 获取版本号
    *
    * @return 版本号
    */
    public static String getVersion() {
        return Version.getVersion();
    }

    /**
    * 获取类的版本号
    *
    * @param clazz 类
    * @return 版本号
    */
    public static String getVersion(Class<?> clazz) {
        Package pkg;
        return !Objects.isNull(pkg = clazz.getPackage())
            ? Objects.toString(pkg.getImplementationVersion(), Version.getVersion())
            : Version.getVersion();
    }
}
