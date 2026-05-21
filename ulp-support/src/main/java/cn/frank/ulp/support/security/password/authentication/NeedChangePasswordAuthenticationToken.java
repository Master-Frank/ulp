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
package cn.frank.ulp.support.security.password.authentication;

import java.util.Collections;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 需要更改密码认证令牌类
 * 用于表示需要更改密码的认证令牌
 */
public class NeedChangePasswordAuthenticationToken extends AbstractAuthenticationToken {
   /**
    * 是否验证旧密码
    */
   private final Boolean verifyOldPassword;
   
   /**
    * 认证信息
    */
   private final Authentication first;
   
   private static final long serialVersionUID = -1506897701981698420L;

   /**
    * 获取主体
    *
    * @return 主体
    */
   @Override
   public Object getPrincipal() {
      return this.first.getPrincipal();
   }

   /**
    * 获取凭证
    *
    * @return 凭证
    */
   @Override
   public Object getCredentials() {
      return "";
   }

   /**
    * 获取第一个认证信息
    *
    * @return 认证信息
    */
   @JsonProperty("first")
   public Authentication getFirst() {
      return this.first;
   }

   /**
    * 构造函数
    *
    * @param first 认证信息
    */
   public NeedChangePasswordAuthenticationToken(Authentication first) {
      this(first, Boolean.TRUE);
   }

   /**
    * 构造函数
    *
    * @param first 认证信息
    * @param verifyOldPassword 是否验证旧密码
    */
   public NeedChangePasswordAuthenticationToken(Authentication first, Boolean verifyOldPassword) {
      super(Collections.emptyList());
      this.first = first;
      this.verifyOldPassword = verifyOldPassword;
      this.setAuthenticated(false);
      this.setDetails(first.getDetails());
   }

   /**
    * 获取是否验证旧密码
    *
    * @return 是否验证旧密码
    */
   @JsonProperty("verifyOldPassword")
   public Boolean getVerifyOldPassword() {
      return this.verifyOldPassword;
   }
}
