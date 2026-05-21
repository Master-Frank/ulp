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
package cn.frank.ulp.portal.service.userdetail;

import org.springframework.stereotype.Service;

import cn.frank.ulp.portal.service.UserService;
import cn.frank.ulp.support.security.session.RefreshCurrentSessionPrincipalService;
import cn.frank.ulp.support.security.userdetails.UserDetails;

/**
 * 刷新当前用户会话 Principal 实现
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2024/3/2 14:06
 */
@Service
public class RefreshCurrentSessionPrincipalServiceImpl implements
                                                       RefreshCurrentSessionPrincipalService {
    /**
     * 获取 Principal
     *
     * @param userId {@link String}
     * @return {@link UserDetails}
     */
    @Override
    public UserDetails getPrincipal(String userId) {
        return userService.getUserDetails(userId);
    }

    private final UserService userService;

    public RefreshCurrentSessionPrincipalServiceImpl(UserService userService) {
        this.userService = userService;
    }
}
