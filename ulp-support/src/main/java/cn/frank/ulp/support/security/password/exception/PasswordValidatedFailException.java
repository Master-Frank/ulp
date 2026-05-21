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
package cn.frank.ulp.support.security.password.exception;

import org.springframework.http.HttpStatus;

/**
 * 密码验证失败异常
 * 当密码验证失败时抛出此异常
 */
public class PasswordValidatedFailException extends PasswordInvalidException {
   
   /**
    * 构造函数
    */
   public PasswordValidatedFailException() {
      super("密码验证失败", "PASSWORD_VALIDATED_FAIL", HttpStatus.BAD_REQUEST);
   }

   /**
    * 构造函数
    *
    * @param message 消息
    */
   public PasswordValidatedFailException(String message) {
      super("密码验证失败", message, HttpStatus.BAD_REQUEST);
   }
}
