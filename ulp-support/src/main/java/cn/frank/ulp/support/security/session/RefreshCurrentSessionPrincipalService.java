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
package cn.frank.ulp.support.security.session;

import cn.frank.ulp.support.security.userdetails.UserDetails;

/**
 * 刷新当前会话主体服务接口
 * 定义获取当前会话主体的方法
 */
public interface RefreshCurrentSessionPrincipalService {
   
   /**
    * 根据会话ID获取主体信息
    *
    * @param sessionId 会话ID
    * @return 主体信息
    */
   UserDetails getPrincipal(String sessionId);
}