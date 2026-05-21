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
package cn.frank.ulp.support.security.jackjson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;

import cn.frank.ulp.support.security.authentication.AuthenticationProvider;
import cn.frank.ulp.support.security.authentication.WebAuthenticationDetails;
import cn.frank.ulp.support.security.core.GrantedAuthority;
import cn.frank.ulp.support.security.password.authentication.NeedChangePasswordAuthenticationToken;
import cn.frank.ulp.support.security.savedredirect.SavedRedirect;
import cn.frank.ulp.support.security.userdetails.Application;
import cn.frank.ulp.support.security.userdetails.DataOrigin;
import cn.frank.ulp.support.security.userdetails.Group;
import cn.frank.ulp.support.security.userdetails.Organization;
import cn.frank.ulp.support.security.userdetails.UserDetails;
import cn.frank.ulp.support.security.userdetails.UserType;

/**
 * 安全Jackson模块类
 * 用于配置安全相关类的Jackson序列化/反序列化
 */
public class SecurityJacksonModule extends Module {
   
   /**
    * 获取模块版本
    *
    * @return 模块版本
    */
   @Override
   public Version version() {
      return new Version(1, 1, 1, null, null, null);
   }

   /**
    * 获取模块名称
    *
    * @return 模块名称
    */
   @Override
   public String getModuleName() {
      return SecurityJacksonModule.class.getName();
   }

   /**
    * 设置模块配置
    *
    * @param context 配置上下文
    */
   @Override
   public void setupModule(SetupContext context) {
      // 配置各类安全相关类的Mixin注解
      context.setMixInAnnotations(Long.class, Object.class);
      context.setMixInAnnotations(SavedRedirect.class, Object.class);
      context.setMixInAnnotations(UserDetails.class, Object.class);
      context.setMixInAnnotations(UserType.class, Object.class);
      context.setMixInAnnotations(DataOrigin.class, Object.class);
      context.setMixInAnnotations(Group.class, Object.class);
      context.setMixInAnnotations(Organization.class, Object.class);
      context.setMixInAnnotations(Application.class, Object.class);
      context.setMixInAnnotations(Application.ApplicationGroup.class, Object.class);
      context.setMixInAnnotations(WebAuthenticationDetails.class, Object.class);
      context.setMixInAnnotations(AuthenticationProvider.class, Object.class);
      context.setMixInAnnotations(GrantedAuthority.class, GrantedAuthorityMixin.class);
      context.setMixInAnnotations(NeedChangePasswordAuthenticationToken.class, Object.class);
   }
}
