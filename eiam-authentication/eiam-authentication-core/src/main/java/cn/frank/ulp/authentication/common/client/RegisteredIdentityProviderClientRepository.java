/*
 * eiam-authentication-core - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.authentication.common.client;

import java.util.Optional;

/**
 * 身份提供商客户端服务
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2024/3/24 18:30
 */
public interface RegisteredIdentityProviderClientRepository {

    /**
     * 根据code查询身份提供商配置
     *
     * @param code {@link String}
     * @return {@link RegisteredIdentityProviderClient}
     */
    <T extends IdentityProviderConfig> Optional<RegisteredIdentityProviderClient<T>> findByCode(String code);
}
