/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.console.service.app;

import java.util.List;

import cn.frank.ulp.common.enums.app.AppType;
import cn.frank.ulp.console.pojo.result.app.AppTemplateResult;

/**
 * 应用模板服务
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/11/29 22:22
 */
public interface AppTemplateService {
    /**
     * List
     *
     * @param name {@link String}
     * @param type {@link AppType}
     * @return {@link List}
     */
    List<AppTemplateResult> getAppTemplateList(AppType type, String name);

    /**
     * List
     *
     * @param code {@link AppTemplateResult}
     * @return {@link Object}
     */
    Object getAppTemplateFormSchema(String code);
}
