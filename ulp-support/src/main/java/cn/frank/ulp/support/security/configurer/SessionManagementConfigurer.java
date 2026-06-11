/*
 * ulp-support - ULP support library
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
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.context.SecurityContextRepository;

import cn.frank.ulp.support.security.session.RefreshCurrentSessionPrincipalService;
import cn.frank.ulp.support.security.web.SessionRefreshFilter;

/**
 * 会话管理配置器
 * 用于配置会话相关的安全过滤器
 *
 * @param <H> HttpSecurityBuilder的子类
 */
public final class SessionManagementConfigurer<H extends HttpSecurityBuilder<H>> extends
                                              AbstractHttpConfigurer<SessionManagementConfigurer<H>, H> {

    /**
    * 从应用上下文中获取指定类型的Bean
    *
    * @param builder HttpSecurity构建器
    * @param clazz Bean类型
    * @param <T> 泛型类型
    * @return Bean实例或null
    */
    private <T> T getBeanFromContext(H builder, Class<T> clazz) {
        ApplicationContext applicationContext = (ApplicationContext) builder
            .getSharedObject(ApplicationContext.class);
        if (applicationContext == null) {
            return null;
        } else {
            try {
                return (T) applicationContext.getBean(clazz);
            } catch (NoSuchBeanDefinitionException e) {
                return null;
            }
        }
    }

    /**
    * 获取指定类型的Bean实例
    *
    * @param builder HttpSecurity构建器
    * @param clazz Bean类型
    * @param <C> 泛型类型
    * @return Bean实例
    */
    private <C> C getBean(H builder, Class<C> clazz) {
        Object sharedObject = builder.getSharedObject(clazz);
        return (C) (sharedObject != null ? sharedObject : this.getBeanFromContext(builder, clazz));
    }

    /**
    * 配置方法
    *
    * @param builder HttpSecurity构建器
    */
    public void configure(H builder) {
        RefreshCurrentSessionPrincipalService refreshCurrentSessionPrincipalService = this
            .getBean(builder, RefreshCurrentSessionPrincipalService.class);
        SecurityContextRepository securityContextRepository = this.getBean(builder,
            SecurityContextRepository.class);
        if (securityContextRepository == null) {
            securityContextRepository = new HttpSessionSecurityContextRepository();
        }

        SessionRefreshFilter filter = new SessionRefreshFilter(
            refreshCurrentSessionPrincipalService);
        filter.setSecurityContextRepository(securityContextRepository);
        builder.addFilterAfter(filter, SecurityContextHolderFilter.class);
    }
}
