/*
 * eiam-portal - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.portal.service;

import cn.frank.ulp.portal.pojo.result.LoginConfigResult;

/**
 * LoginConfigService
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/3/25 21:49
 */
public interface LoginConfigService {

    /**
     * 获取登录配置
     *
     * @return {@link LoginConfigResult}
     */
    LoginConfigResult getLoginConfig();
}
