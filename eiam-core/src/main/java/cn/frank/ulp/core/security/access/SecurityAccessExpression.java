/*
 * eiam-core - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.core.security.access;

import java.util.Objects;

import cn.frank.ulp.support.security.userdetails.UserType;
import cn.frank.ulp.support.security.util.SecurityUtils;

/**
 * 安全访问表达式
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/5/25 22:11
 */
public class SecurityAccessExpression {

    /**
     * 判断是否有相应权限
     *
     * @param authority 权限
     * @return {boolean}
     */
    public boolean hasAuthority(Object authority) {
        if (Objects.isNull(authority)) {
            return false;
        }
        UserType userType = SecurityUtils.getCurrentUserType();
        return userType.equals(authority);
    }
}
