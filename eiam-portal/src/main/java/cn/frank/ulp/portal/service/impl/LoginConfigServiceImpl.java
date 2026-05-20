/*
 * eiam-portal - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
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
