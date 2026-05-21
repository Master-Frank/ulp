/*
 * ulp-protocol-jwt - United Login Platform
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
package cn.frank.ulp.protocol.jwt.configurers;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import cn.frank.ulp.application.ApplicationServiceLoader;
import cn.frank.ulp.protocol.code.configurer.AuthenticationUtils;
import cn.frank.ulp.protocol.jwt.InMemoryJwtAuthorizationService;
import cn.frank.ulp.protocol.jwt.JwtAuthorizationService;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/8 23:19
 */
public class JwtAuthenticationUtils {

    public static JwtAuthorizationService getAuthorizationService(HttpSecurity httpSecurity) {
        JwtAuthorizationService authorizationService = httpSecurity
            .getSharedObject(JwtAuthorizationService.class);
        if (authorizationService == null) {
            authorizationService = AuthenticationUtils.getOptionalBean(httpSecurity,
                JwtAuthorizationService.class);
            if (authorizationService == null) {
                ApplicationServiceLoader applicationServiceLoader = AuthenticationUtils
                    .getOptionalBean(httpSecurity, ApplicationServiceLoader.class);
                authorizationService = new InMemoryJwtAuthorizationService(
                    applicationServiceLoader);
            }
            httpSecurity.setSharedObject(JwtAuthorizationService.class, authorizationService);
        }
        return authorizationService;
    }
}
