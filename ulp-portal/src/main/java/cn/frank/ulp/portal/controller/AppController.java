/*
 * eiam-portal - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.portal.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.frank.ulp.portal.pojo.query.GetAppGroupListQuery;
import cn.frank.ulp.portal.pojo.query.GetAppListQuery;
import cn.frank.ulp.portal.pojo.result.AppGroupListResult;
import cn.frank.ulp.portal.pojo.result.GetAppListResult;
import cn.frank.ulp.portal.service.AppService;
import cn.frank.ulp.support.repository.page.domain.Page;
import cn.frank.ulp.support.repository.page.domain.PageModel;
import cn.frank.ulp.support.result.ApiRestResult;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import static cn.frank.ulp.common.constant.AppConstants.APP_PATH;

/**
 * 应用
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/12 21:39
 */
@Tag(name = "应用管理")
@RestController
@RequestMapping(value = APP_PATH)
@AllArgsConstructor
public class AppController {

    /**
     * 获取应用数量
     *
     * @return {@link GetAppListResult}
     */
    @Operation(summary = "获取应用数量")
    @GetMapping(value = "/count")
    public ApiRestResult<String> getAppCount(String groupId) {
        Long count;
        if (StringUtils.isEmpty(groupId)) {
            count = appService.getAppCount();
        } else {
            count = appService.getAppCount(groupId);
        }
        return ApiRestResult.ok(count.toString());
    }

    /**
     * 获取应用列表
     *
     * @return {@link GetAppListResult}
     */
    @Operation(summary = "获取应用列表")
    @GetMapping(value = "/list")
    public ApiRestResult<Page<GetAppListResult>> getAppList(GetAppListQuery query,
                                                            PageModel pageModel) {
        Page<GetAppListResult> list = appService.getAppList(query, pageModel);
        return ApiRestResult.ok(list);
    }

    /**
     * 获取分组应用列表
     *
     * @return {@link AppGroupListResult}
     */
    @Operation(summary = "获取分组应用列表")
    @GetMapping(value = "/group_list")
    public ApiRestResult<List<AppGroupListResult>> getAppGroupList(GetAppGroupListQuery appGroupQuery) {
        List<AppGroupListResult> list = appService.getAppGroupList(appGroupQuery);
        return ApiRestResult.ok(list);
    }

    /**
     * AppService
     */
    private final AppService appService;

}
