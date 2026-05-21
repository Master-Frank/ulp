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
package cn.frank.ulp.support.web.decrypt;

import org.springframework.http.HttpStatus;

import cn.frank.ulp.support.exception.TopIamException;

/**
 * 加密密钥不存在异常类
 * 用于处理加密密钥不存在的异常情况
 */
public class EncryptSecretNotExistException extends TopIamException {
   
   /**
    * 构造函数，初始化异常信息
    */
   public EncryptSecretNotExistException() {
      super("ENCRYPT_SECRET_NOT_EXIST", "加密密钥不存在", HttpStatus.BAD_REQUEST);
   }
}
