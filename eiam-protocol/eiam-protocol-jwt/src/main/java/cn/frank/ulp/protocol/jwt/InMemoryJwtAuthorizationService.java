/*
 * eiam-protocol-jwt - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.protocol.jwt;

import cn.frank.ulp.application.ApplicationServiceLoader;

/**
 * 内存
 *
 * @author TopIAM
 * Created by support@topiam.cn
 */
public class InMemoryJwtAuthorizationService extends AbstractJwtAuthorizationService {

    public InMemoryJwtAuthorizationService(ApplicationServiceLoader applicationServiceLoader) {
        super(applicationServiceLoader);
    }

    @Override
    public void save(JwtAuthentication token) {

    }

    @Override
    public void remove(JwtAuthentication authorization) {

    }

    @Override
    public JwtAuthentication findById(String id) {
        return null;
    }

    @Override
    public JwtAuthentication findByToken(String token) {
        return null;
    }

}
