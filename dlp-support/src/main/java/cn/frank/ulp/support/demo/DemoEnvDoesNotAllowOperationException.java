/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.support.demo;

import org.springframework.http.HttpStatus;

import cn.frank.ulp.support.exception.TopIamException;

/**
 * 演示环境不允许操作异常
 * 在演示环境中执行不允许的操作时抛出此异常
 */
public class DemoEnvDoesNotAllowOperationException extends TopIamException {
    /**
     * 构造函数
     */
    public DemoEnvDoesNotAllowOperationException() {
        super(
            "演示环境不允许此操作", 
            HttpStatus.FORBIDDEN
        );
    }
}
