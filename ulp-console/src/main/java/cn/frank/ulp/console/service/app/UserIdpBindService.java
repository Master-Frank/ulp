/*
 * ulp-console - United Login Platform
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
package cn.frank.ulp.console.service.app;

import java.util.List;

import cn.frank.ulp.console.pojo.result.app.UserIdpBindListResult;

/**
 * 用户身份提供商绑定
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/12/11 21:10
 */
public interface UserIdpBindService {
    /**
     * 解绑用户IDP
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    Boolean unbindUserIdpBind(String id);

    /**
     * 查询用户身份提供商绑定
     *
     * @param userId {@link String}
     * @return {@link List<UserIdpBindListResult>}
     */
    List<UserIdpBindListResult> getUserIdpBindList(String userId);
}
