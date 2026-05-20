/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
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

public final class CustomSecurityConfiguration<H extends HttpSecurityBuilder<H>>
    extends AbstractHttpConfigurer<CustomSecurityConfiguration<H>, H> {

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
