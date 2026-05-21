/*
 * eiam-protocol-jwt - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
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
