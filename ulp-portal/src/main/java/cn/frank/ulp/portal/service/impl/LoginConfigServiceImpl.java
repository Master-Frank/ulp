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
package cn.frank.ulp.portal.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.frank.ulp.common.entity.authn.IdentityProviderEntity;
import cn.frank.ulp.common.repository.authentication.IdentityProviderRepository;
import cn.frank.ulp.portal.converter.LoginConfigConverter;
import cn.frank.ulp.portal.pojo.result.IdentityProviderResult;
import cn.frank.ulp.portal.pojo.result.LoginConfigResult;
import cn.frank.ulp.portal.service.LoginConfigService;

import lombok.AllArgsConstructor;

/**
 * LoginConfigService
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/3/25 21:50
 */
@Service
@AllArgsConstructor
public class LoginConfigServiceImpl implements LoginConfigService {

    /**
     * 获取登录配置
     *
     * @return {@link LoginConfigResult}
     */
    @Override
    public LoginConfigResult getLoginConfig() {
        LoginConfigResult.LoginConfigResultBuilder builder = LoginConfigResult.builder();
        //获取IDPS
        List<IdentityProviderEntity> list = identityProviderRepository
            .findByEnabledIsTrueAndDisplayedIsTrue();
        List<IdentityProviderResult> idps = loginConfigConverter
            .entityConverterToLoginConfigListResult(list);
        builder.idps(idps);
        return builder.build();
    }

    /**
     * AuthenticationConverter
     */
    private final LoginConfigConverter       loginConfigConverter;

    /**
     * AuthenticationSourceRepository
     */
    private final IdentityProviderRepository identityProviderRepository;
}
