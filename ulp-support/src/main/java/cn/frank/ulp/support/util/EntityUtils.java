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

import java.lang.reflect.Field;

/**
 * 实体反射工具.
 */
public class EntityUtils {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory
        .getLogger(EntityUtils.class);

    public EntityUtils() {
    }

    public static boolean isHasField(Object obj, String fieldName) {
        if (obj == null || fieldName == null) {
            return false;
        }
        Class<?> clazz = obj.getClass();
        while (clazz != null && clazz != Object.class) {
            for (Field f : clazz.getDeclaredFields()) {
                if (f.getName().equals(fieldName)) {
                    return true;
                }
            }
            clazz = clazz.getSuperclass();
        }
        return false;
    }

    public static Object getPropertyValue(Object obj, String fieldName) {
        Class<?> clazz = obj.getClass();
        Class<?> cur = clazz;
        while (cur != null && cur != Object.class) {
            try {
                Field f = cur.getDeclaredField(fieldName);
                f.setAccessible(true);
                return f.get(obj);
            } catch (NoSuchFieldException nsfe) {
                cur = cur.getSuperclass();
            } catch (IllegalAccessException iae) {
                log.error("反射获取字段 {} 在类 {} 上失败: {}", fieldName, clazz.getName(), iae.getMessage());
                throw new RuntimeException(iae);
            }
        }
        log.error("类 {} 上不存在字段 {}", clazz.getName(), fieldName);
        throw new RuntimeException(new NoSuchFieldException(fieldName));
    }
}
