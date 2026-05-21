/*
 * eiam-portal - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.portal.service;

import java.util.List;

import cn.frank.ulp.portal.pojo.query.GetAppGroupListQuery;
import cn.frank.ulp.portal.pojo.query.GetAppListQuery;
import cn.frank.ulp.portal.pojo.result.AppGroupListResult;
import cn.frank.ulp.portal.pojo.result.GetAppListResult;
import cn.frank.ulp.support.repository.page.domain.Page;
import cn.frank.ulp.support.repository.page.domain.PageModel;

/**
 * AppService
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/7/6 22:35
 */
public interface AppService {
    /**
     * 获取应用列表
     *
     * @param query     {@link GetAppListQuery}
     * @param pageModel {@link PageModel}
     * @return {@link Page}
     */
    Page<GetAppListResult> getAppList(GetAppListQuery query,
                                      PageModel pageModel);

    /**
     * 查询应用分组
     *
     * @param appGroupQuery {@link GetAppGroupListQuery}
     * @return {@link AppGroupListResult}
     */
    List<AppGroupListResult> getAppGroupList(GetAppGroupListQuery appGroupQuery);

    /**
     * 获取应用数量
     *
     * @param groupId {@link String}
     * @return {@link Long}
     */
    Long getAppCount(String groupId);

    /**
     * 获取所有应用
     *
     * @return {@link Long}
     */
    Long getAppCount();
}
