/*
 * ulp-portal - United Login Platform
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
package cn.frank.ulp.portal.service;

import java.util.List;

import cn.frank.ulp.application.AppAccount;
import cn.frank.ulp.portal.pojo.request.AppAccountRequest;

/**
 * 应用账户
 *
 * @author Frank Zhang
 */
public interface AppAccountService {

    /**
     * 新增应用账户
     *
     * @param param {@link AppAccountRequest}
     * @return {@link Boolean}
     */
    Boolean createAppAccount(AppAccountRequest param);

    /**
     * 删除应用账户
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    Boolean deleteAppAccount(String id);

    /**
     * 获取当前登陆者应用账户
     * @param appId {@link String}
     * @return {@link List <AppAccount>}
     */
    List<AppAccount> getAppAccountList(String appId);

    /**
     * 设置默认应用账户
     *
     * @param id {@link String}
     * @param defaulted {@link Boolean}
     * @return {@link Boolean}
     */
    Boolean updateAppAccountDefault(String id, Boolean defaulted);
}
