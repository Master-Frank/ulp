/*
 * eiam-application-form - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.application.form;

import cn.frank.ulp.application.ApplicationService;
import cn.frank.ulp.application.form.model.FormProtocolConfig;

/**
 * 应用接口
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/8/20 23:20
 */
public interface FormApplicationService extends ApplicationService {

    /**
     * 获取协议配置
     *
     * @param appCode {@link String}
     * @return {@link FormProtocolConfig}
     */
    FormProtocolConfig getProtocolConfig(String appCode);
}
