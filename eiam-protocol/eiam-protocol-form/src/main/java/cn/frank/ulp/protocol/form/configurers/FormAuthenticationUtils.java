/*
 * eiam-protocol-form - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.protocol.form.configurers;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import cn.frank.ulp.application.ApplicationServiceLoader;
import cn.frank.ulp.protocol.code.configurer.AuthenticationUtils;
import cn.frank.ulp.protocol.form.FormAuthorizationService;
import cn.frank.ulp.protocol.form.InMemoryFormAuthorizationService;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/8 23:19
 */
public class FormAuthenticationUtils {

    public static FormAuthorizationService getAuthorizationService(HttpSecurity httpSecurity) {
        FormAuthorizationService authorizationService = httpSecurity
            .getSharedObject(FormAuthorizationService.class);
        if (authorizationService == null) {
            authorizationService = AuthenticationUtils.getOptionalBean(httpSecurity,
                FormAuthorizationService.class);
            if (authorizationService == null) {
                ApplicationServiceLoader applicationServiceLoader = AuthenticationUtils
                    .getOptionalBean(httpSecurity, ApplicationServiceLoader.class);
                authorizationService = new InMemoryFormAuthorizationService(
                    applicationServiceLoader);
            }
            httpSecurity.setSharedObject(FormAuthorizationService.class, authorizationService);
        }
        return authorizationService;
    }
}
