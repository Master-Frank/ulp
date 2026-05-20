/*
 * eiam-common - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.common.repository.app;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.frank.ulp.common.entity.app.po.AppAccessPolicyPO;
import cn.frank.ulp.common.entity.app.query.AppAccessPolicyQueryParam;

/**
 * 应用访问策略 Repository Customized
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/5/26 23:40
 */
public interface AppAccessPolicyRepositoryCustomized {

    /**
     * 获取应用授权策略列表
     *
     * @param pageable {@link  Pageable}
     * @param query    {@link  AppAccessPolicyQueryParam}
     * @return {@link Page}
     */
    Page<AppAccessPolicyPO> getAppPolicyList(AppAccessPolicyQueryParam query, Pageable pageable);
}
