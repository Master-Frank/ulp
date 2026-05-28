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
package cn.frank.ulp.support.security.configurer;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import cn.frank.ulp.support.security.password.PasswordPolicyManager;
import cn.frank.ulp.support.security.userdetails.UserDetailsService;
import cn.frank.ulp.support.security.web.CustomAuthenticationFilter;

public final class CustomSecurityConfiguration<H extends HttpSecurityBuilder<H>> extends
                                              AbstractHttpConfigurer<CustomSecurityConfiguration<H>, H> {

    @Override
    public void configure(H builder) {
        UserDetailsService userDetailsService = getBean(builder, UserDetailsService.class);
        PasswordPolicyManager passwordPolicyManager = getBean(builder, PasswordPolicyManager.class);
        SessionRegistry sessionRegistry = getBean(builder, SessionRegistry.class);
        AsyncConfigurer asyncConfigurer = getBean(builder, AsyncConfigurer.class);

        CustomAuthenticationFilter filter = new CustomAuthenticationFilter(userDetailsService,
            passwordPolicyManager, sessionRegistry, asyncConfigurer);
        builder.addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class);
    }

    private <T> T getBean(H builder, Class<T> clazz) {
        T sharedObject = builder.getSharedObject(clazz);
        if (sharedObject != null) {
            return sharedObject;
        }
        return getBeanFromContext(builder, clazz);
    }

    private <T> T getBeanFromContext(H builder, Class<T> clazz) {
        ApplicationContext applicationContext = builder.getSharedObject(ApplicationContext.class);
        if (applicationContext == null) {
            return null;
        }
        try {
            return applicationContext.getBean(clazz);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }
}
