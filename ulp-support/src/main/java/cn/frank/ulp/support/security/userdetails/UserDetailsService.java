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
package cn.frank.ulp.support.security.userdetails;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 用户详情服务接口
 * 扩展Spring Security的UserDetailsService接口，提供更多用户相关操作
 */
public interface UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService {
   
   /**
    * 根据手机号加载用户
    *
    * @param phone 手机号
    * @return 用户详情
    * @throws UsernameNotFoundException 用户未找到异常
    */
   UserDetails loadUserByPhone(String phone) throws UsernameNotFoundException;

   /**
    * 修改密码
    *
    * @param userId 用户ID
    * @param newPassword 新密码
    */
   void changePassword(String userId, String newPassword);

   /**
    * 根据邮箱加载用户
    *
    * @param email 邮箱
    * @return 用户详情
    * @throws UsernameNotFoundException 用户未找到异常
    */
   UserDetails loadUserByEmail(String email) throws UsernameNotFoundException;

   /**
    * 修改密码
    *
    * @param userId 用户ID
    * @param oldPassword 旧密码
    * @param newPassword 新密码
    */
   void changePassword(String userId, String oldPassword, String newPassword);
}