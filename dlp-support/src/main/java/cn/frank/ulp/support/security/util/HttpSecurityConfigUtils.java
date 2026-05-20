/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.support.security.util;

import java.util.Map;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

public class HttpSecurityConfigUtils {

    public HttpSecurityConfigUtils() {
    }

    public static PasswordEncoder getPasswordEncoder(HttpSecurity httpSecurity) {
        PasswordEncoder passwordEncoder = httpSecurity.getSharedObject(PasswordEncoder.class);
        if (passwordEncoder == null) {
            passwordEncoder = getOptionalBean(httpSecurity, PasswordEncoder.class);
            httpSecurity.setSharedObject(PasswordEncoder.class, passwordEncoder);
        }
        return passwordEncoder;
    }

    public static UserDetailsService getUserDetailsService(HttpSecurity httpSecurity) {
        UserDetailsService userDetailsService = httpSecurity.getSharedObject(UserDetailsService.class);
        if (userDetailsService == null) {
            userDetailsService = getOptionalBean(httpSecurity, UserDetailsService.class);
            httpSecurity.setSharedObject(UserDetailsService.class, userDetailsService);
        }
        return userDetailsService;
    }

    public static <T> T getOptionalBean(HttpSecurity httpSecurity, Class<T> type) {
        ApplicationContext applicationContext = httpSecurity.getSharedObject(ApplicationContext.class);
        Map<String, T> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, type);
        if (beans.size() > 1) {
            throw new NoUniqueBeanDefinitionException(type, beans.size(),
                "Expected single matching bean of type '" + type.getName() + "' but found "
                    + beans.size() + ": "
                    + StringUtils.collectionToCommaDelimitedString(beans.keySet()));
        }
        return beans.isEmpty() ? null : beans.values().iterator().next();
    }

    @SuppressWarnings("unchecked")
    public static <T> T getOptionalBean(HttpSecurity httpSecurity, ResolvableType type) {
        ApplicationContext applicationContext = httpSecurity.getSharedObject(ApplicationContext.class);
        String[] names = applicationContext.getBeanNamesForType(type);
        if (names.length > 1) {
            throw new NoUniqueBeanDefinitionException(type, names);
        }
        if (names.length == 1) {
            return (T) applicationContext.getBean(names[0]);
        }
        return null;
    }
}
