/*
 * eiam-identity-source-core - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.identitysource.core;

/**
 * 身份源客户端配置验证器
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/4/10 00:23
 */
public interface IdentitySourceConfigValidator<T extends IdentitySourceConfig> {
    /**
     * validate
     *
     * @param config {@link  T}
     * @return {@link  Boolean}
     */
    Boolean validate(T config);
}
