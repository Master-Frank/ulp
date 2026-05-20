/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.support.security.password;

import org.jetbrains.annotations.NotNull;

import cn.frank.ulp.support.security.password.exception.PasswordInvalidException;

/**
 * 密码策略管理器接口
 * 定义密码验证的方法
 */
public interface PasswordPolicyManager {
   
   /**
    * 验证密码
    *
    * @param userDetails 用户详情对象
    * @param password 密码
    * @throws PasswordInvalidException 密码无效异常
    */
   void validate(@NotNull Object userDetails, String password) throws PasswordInvalidException;

   /**
    * 验证密码
    *
    * @param username 用户名
    * @param password 密码
    * @throws PasswordInvalidException 密码无效异常
    */
   void validate(@NotNull String username, String password) throws PasswordInvalidException;
}
