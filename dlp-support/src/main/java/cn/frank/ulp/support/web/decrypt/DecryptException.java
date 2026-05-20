/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.support.web.decrypt;

import org.springframework.http.HttpStatus;

import cn.frank.ulp.support.exception.TopIamException;

/**
 * 解密异常类
 * 用于处理解密过程中发生的异常
 */
public class DecryptException extends TopIamException {
   
   /**
    * 构造函数
    *
    * @param throwable 异常
    */
   public DecryptException(Throwable throwable) {
      super(throwable, "DECRYPT_ERROR", "解密失败", HttpStatus.INTERNAL_SERVER_ERROR);
   }

   /**
    * 构造函数
    *
    * @param message 错误信息
    * @param httpStatus HTTP状态码
    */
   public DecryptException(String message, HttpStatus httpStatus) {
      super("DECRYPT_ERROR", message, httpStatus);
   }
}
