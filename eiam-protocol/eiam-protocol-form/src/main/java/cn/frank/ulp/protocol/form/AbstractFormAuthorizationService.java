/*
 * eiam-protocol-form - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.protocol.form;

import org.springframework.util.Assert;

import cn.frank.ulp.application.AppAccount;
import cn.frank.ulp.application.ApplicationServiceLoader;
import cn.frank.ulp.application.form.FormApplicationService;
import cn.frank.ulp.protocol.code.LoginAccount;
import cn.frank.ulp.protocol.form.client.FormRegisteredClient;
import cn.frank.ulp.support.security.userdetails.UserDetails;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2024/1/7 21:43
 */
public abstract class AbstractFormAuthorizationService implements FormAuthorizationService {

    private final ApplicationServiceLoader applicationServiceLoader;

    protected AbstractFormAuthorizationService(ApplicationServiceLoader applicationServiceLoader) {
        Assert.notNull(applicationServiceLoader, "applicationServiceLoader cannot be null");
        this.applicationServiceLoader = applicationServiceLoader;
    }

    /**
     * 用户是否具有登录权限
     *
     * @param client {@link FormRegisteredClient}
     * @param user   {@link UserDetails}
     * @return {@link Boolean}
     */
    @Override
    public Boolean hasLoginPermission(FormRegisteredClient client, UserDetails user) {
        return null;
    }

    /**
     * 根据客户端&用户ID查询默认应用账户
     *
     * @param client {@link String} 客户端
     * @param user   {@link UserDetails} 用户
     * @return {@link String}
     */
    @Override
    public LoginAccount getDefaultLoginAccount(FormRegisteredClient client, UserDetails user) {
        FormApplicationService applicationService = (FormApplicationService) applicationServiceLoader
            .getApplicationServiceByAppCode(client.getCode());
        AppAccount appAccount = applicationService.getDefaultAppAccount(client.getId(),
            user.getId());
        return LoginAccount.builder().appId(appAccount.getAppId()).userId(appAccount.getUserId())
            .username(appAccount.getAccount()).password(appAccount.getPassword())
            .isDefault(appAccount.getDefaulted()).build();
    }
}
