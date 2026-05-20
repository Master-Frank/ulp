/*
 * eiam-common - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.common.repository.app;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.frank.ulp.common.entity.app.po.AppAccountPO;
import cn.frank.ulp.common.entity.app.query.AppAccountQueryParam;

/**
 * 应用账户 Repository Customized
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/5/26 23:40
 */
public interface AppAccountRepositoryCustomized {

    /**
     * 获取应用账户列表
     *
     * @param pageable {@link  Pageable}
     * @param query    {@link  AppAccountQueryParam}
     * @return {@link Page}
     */
    Page<AppAccountPO> getAppAccountList(AppAccountQueryParam query, Pageable pageable);
}
