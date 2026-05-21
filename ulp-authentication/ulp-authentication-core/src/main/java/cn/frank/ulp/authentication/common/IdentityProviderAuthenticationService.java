/*
 * ulp-authentication-core - United Login Platform
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
package cn.frank.ulp.authentication.common;

import cn.frank.ulp.authentication.common.authentication.IdentityProviderUserDetails;
import cn.frank.ulp.support.security.userdetails.UserDetails;

/**
 * UserIdpService
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/12/9 21:54
 */
public interface IdentityProviderAuthenticationService {

    /**
     * 检查用户已绑定对应平台
     *
     * @param openId     {@link  String}
     * @param providerId {@link  String}
     * @return {@link  Boolean}
     */
    Boolean checkIdpUserIsExistBind(String openId, String providerId);

    /**
     * 绑定
     *
     * @param accountId   {@link  String} 账户ID
     * @param identityProviderUserDetails   {@link  IdentityProviderUserDetails} 用户信息
     * @return {@link  Boolean}
     */
    Boolean bindUserIdp(String accountId, IdentityProviderUserDetails identityProviderUserDetails);

    /**
     * 更新账户信息
     *
     * @param identityProviderUserDetails   {@link IdentityProviderUserDetails} 用户信息
     * @param providerId {@link  String} 提供商ID
     * @return {@link  Boolean}
     */
    Boolean updateThirdPartyUser(IdentityProviderUserDetails identityProviderUserDetails,
                                 String providerId);

    /**
     * 获取用户详情
     *
     * @param openId     {@link String}
     * @param providerId {@link String}
     * @return {@link UserDetails}
     */
    UserDetails getUserDetails(String openId, String providerId);
}
