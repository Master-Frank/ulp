/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.support.web.decrypt;

import org.springframework.http.HttpStatus;

import cn.frank.ulp.support.exception.TopIamException;

/**
 * 加密密钥不存在异常类
 * 用于处理加密密钥不存在的异常情况
 */
public class EncryptSecretNotExistException extends TopIamException {
   
   /**
    * 构造函数，初始化异常信息
    */
   public EncryptSecretNotExistException() {
      super("ENCRYPT_SECRET_NOT_EXIST", "加密密钥不存在", HttpStatus.BAD_REQUEST);
   }
}
