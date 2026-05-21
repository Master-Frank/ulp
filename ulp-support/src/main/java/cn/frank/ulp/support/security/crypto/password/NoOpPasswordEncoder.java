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
package cn.frank.ulp.support.security.crypto.password;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 无操作密码编码器
 * 不对密码进行任何编码操作，直接返回原密码
 */
public final class NoOpPasswordEncoder implements PasswordEncoder {
   /**
    * 单例实例
    */
   private static final PasswordEncoder INSTANCE = new NoOpPasswordEncoder();

   /**
    * 编码密码
    *
    * @param rawPassword 原始密码
    * @return 编码后的密码
    */
   @Override
   public String encode(CharSequence rawPassword) {
      return rawPassword.toString();
   }

   /**
    * 匹配密码
    *
    * @param rawPassword 原始密码
    * @param encodedPassword 编码后的密码
    * @return 是否匹配
    */
   @Override
   public boolean matches(CharSequence rawPassword, String encodedPassword) {
      return rawPassword.toString().equals(encodedPassword);
   }

   /**
    * 获取单例实例
    *
    * @return 密码编码器实例
    */
   public static PasswordEncoder getInstance() {
      return INSTANCE;
   }

   /**
    * 私有构造函数
    */
   private NoOpPasswordEncoder() {
   }
}