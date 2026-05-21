/*
 * eiam-application-core - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.application.exception;

import cn.frank.ulp.support.exception.TopIamException;

/**
 * 应用不存在异常
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/7/8 22:23
 */
public class AppNotExistException extends TopIamException {
    public AppNotExistException() {
        super("app_not_exist", "应用不存在", DEFAULT_STATUS);
    }
}
