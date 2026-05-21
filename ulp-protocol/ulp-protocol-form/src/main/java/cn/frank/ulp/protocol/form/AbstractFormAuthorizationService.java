/*
 * ulp-protocol-form - United Login Platform
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
package cn.frank.ulp.protocol.form;

import org.springframework.util.Assert;

import cn.frank.ulp.application.AppAccount;
import cn.frank.ulp.application.ApplicationServiceLoader;
import cn.frank.ulp.application.form.FormApplicationService;
import cn.frank.ulp.protocol.code.LoginAccount;
import cn.frank.ulp.protocol.form.client.FormRegisteredClient;
import cn.frank.ulp.support.security.userdetails.UserDetails;

/**
 * @author Frank Zhang
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
