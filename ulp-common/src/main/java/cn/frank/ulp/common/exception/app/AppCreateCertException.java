/*
 * eiam-common - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.common.exception.app;

import cn.frank.ulp.support.exception.TopIamException;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2024/2/18 18:13
 */
public class AppCreateCertException extends TopIamException {

    public AppCreateCertException() {
        super("app_create_cert_error", "创建应用证书失败", DEFAULT_STATUS);
    }
}
