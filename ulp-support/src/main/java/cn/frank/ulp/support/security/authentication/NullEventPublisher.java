/*
 * ulp-support - United Login Platform
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
package cn.frank.ulp.support.security.authentication;

import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * 空事件发布器
 * 用于不发布认证事件的场景
 */
public final class NullEventPublisher implements AuthenticationEventPublisher {

    /**
    * 发布认证成功事件
    *
    * @param authentication 认证信息
    */
    @Override
    public void publishAuthenticationSuccess(Authentication authentication) {
        // 空实现，不发布事件
    }

    /**
    * 发布认证失败事件
    *
    * @param exception 认证异常
    * @param authentication 认证信息
    */
    @Override
    public void publishAuthenticationFailure(AuthenticationException exception,
                                             Authentication authentication) {
        // 空实现，不发布事件
    }
}