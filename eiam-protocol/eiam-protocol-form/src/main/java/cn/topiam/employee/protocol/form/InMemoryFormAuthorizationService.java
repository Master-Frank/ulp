/*
 * eiam-protocol-form - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.protocol.form;

import cn.topiam.employee.application.ApplicationServiceLoader;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/8 23:30
 */
public class InMemoryFormAuthorizationService extends AbstractFormAuthorizationService {

    public InMemoryFormAuthorizationService(ApplicationServiceLoader applicationServiceLoader) {
        super(applicationServiceLoader);
    }
}
