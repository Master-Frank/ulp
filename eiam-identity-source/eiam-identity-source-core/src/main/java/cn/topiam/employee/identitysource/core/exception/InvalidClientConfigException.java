/*
 * eiam-identity-source-core - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.identitysource.core.exception;

import cn.topiam.employee.support.exception.TopIamException;

/**
 * 无效配置异常
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/4/10 00:22
 */
public class InvalidClientConfigException extends TopIamException {
    public InvalidClientConfigException(String msg, Throwable t) {
        super(msg, t);
    }

    public InvalidClientConfigException(String msg) {
        super(msg);
    }
}
