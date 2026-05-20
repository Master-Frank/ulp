/*
 * eiam-portal - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.portal.service;

import java.util.Optional;

import cn.frank.ulp.common.entity.account.UserEntity;
import cn.frank.ulp.support.security.userdetails.UserDetails;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2024/3/2 22:21
 */
public interface UserService {
    /**
     * 获取用户详情
     *
     * @return {@link UserDetails}
     */
    UserDetails getUserDetails(String userId);

    /**
     * 获取用户详情
     *
     * @return {@link UserEntity}
     */
    UserDetails getUserDetails(UserEntity user);

    /**
     * 根据用户名、手机号、邮箱查询用户
     *
     * @return {@link UserEntity}
     */
    Optional<UserEntity> findByUsernameOrPhoneOrEmail(String keyword);
}
