/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.console.service.app;

import cn.frank.ulp.common.entity.app.query.AppAccountQueryParam;
import cn.frank.ulp.console.pojo.query.app.AppAccountQuery;
import cn.frank.ulp.console.pojo.result.app.AppAccountListResult;
import cn.frank.ulp.console.pojo.save.app.AppAccountCreateParam;
import cn.frank.ulp.support.repository.page.domain.Page;
import cn.frank.ulp.support.repository.page.domain.PageModel;

/**
 * 应用账户
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/6/4 21:07
 */
public interface AppAccountService {

    /**
     * 查询应用账户
     *
     * @param pageModel {@link PageModel}
     * @param query     {@link  AppAccountQueryParam}
     * @return {@link Page}
     */
    Page<AppAccountListResult> getAppAccountList(PageModel pageModel, AppAccountQuery query);

    /**
     * 新增应用账户
     *
     * @param param {@link AppAccountCreateParam}
     * @return {@link Boolean}
     */
    Boolean createAppAccount(AppAccountCreateParam param);

    /**
     * 删除应用账户
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    Boolean deleteAppAccount(String id);

    /**
     * 设置默认应用账户
     *
     * @param id {@link String}
     * @param defaulted {@link Boolean}
     * @return {@link Boolean}
     */
    Boolean updateAppAccountDefault(String id, Boolean defaulted);
}
