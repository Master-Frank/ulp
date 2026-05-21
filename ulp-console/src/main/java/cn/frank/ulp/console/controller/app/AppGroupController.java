/*
 * ulp-console - United Login Platform
 * Copyright (c) 2022-Present Frank Zhang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.frank.ulp.console.controller.app;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.google.common.collect.Lists;

import cn.frank.ulp.audit.annotation.Audit;
import cn.frank.ulp.audit.event.type.EventType;
import cn.frank.ulp.common.entity.account.query.UserGroupMemberListQuery;
import cn.frank.ulp.console.pojo.query.app.AppGroupAssociationListQuery;
import cn.frank.ulp.console.pojo.query.app.AppGroupListQuery;
import cn.frank.ulp.console.pojo.result.app.AppGroupGetResult;
import cn.frank.ulp.console.pojo.result.app.AppGroupListResult;
import cn.frank.ulp.console.pojo.result.app.AppListResult;
import cn.frank.ulp.console.pojo.save.app.AppGroupCreateParam;
import cn.frank.ulp.console.pojo.update.app.AppGroupUpdateParam;
import cn.frank.ulp.console.service.app.AppGroupService;
import cn.frank.ulp.support.demo.Preview;
import cn.frank.ulp.support.lock.Lock;
import cn.frank.ulp.support.repository.page.domain.Page;
import cn.frank.ulp.support.repository.page.domain.PageModel;
import cn.frank.ulp.support.result.ApiRestResult;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import static cn.frank.ulp.common.constant.AppConstants.APP_PATH;

/**
 * 分组管理
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/8/31 15:35
 */
@Validated
@Tag(name = "应用分组管理")
@RestController
@AllArgsConstructor
@RequestMapping(value = APP_PATH + "/group", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppGroupController {

    /**
     * 获取应用分组列表
     *
     * @param page {@link PageModel}
     * @return {@link AppGroupListQuery}
     */
    @Operation(summary = "获取分组列表")
    @GetMapping(value = "/list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Page<AppGroupListResult>> getAppGroupList(PageModel page,
                                                                   AppGroupListQuery query) {
        Page<AppGroupListResult> list = appGroupService.getAppGroupList(page, query);
        return ApiRestResult.<Page<AppGroupListResult>> builder().result(list).build();
    }

    /**
     * 创建应用分组
     *
     * @param param {@link AppGroupCreateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "创建应用分组")
    @Audit(type = EventType.ADD_APP_GROUP)
    @PostMapping(value = "/create")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> createAppGroup(@RequestBody @Validated AppGroupCreateParam param) {
        return ApiRestResult.<Boolean> builder().result(appGroupService.createAppGroup(param))
            .build();
    }

    /**
     * 修改应用分组
     *
     * @param param {@link AppGroupUpdateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "修改应用分组")
    @Audit(type = EventType.UPDATE_APP_GROUP)
    @PutMapping(value = "/update")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> updateAppGroup(@RequestBody @Validated AppGroupUpdateParam param) {
        return ApiRestResult.<Boolean> builder().result(appGroupService.updateAppGroup(param))
            .build();
    }

    /**
     * 删除应用分组
     *
     * @param id {@link Long}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "删除应用分组")
    @Audit(type = EventType.DELETE_APP_GROUP)
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> deleteAppGroup(@PathVariable(value = "id") String id) {
        return ApiRestResult.<Boolean> builder().result(appGroupService.deleteAppGroup(id)).build();
    }

    /**
     * 获取应用分组信息
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Operation(summary = "获取应用分组信息")
    @GetMapping(value = "/get/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<AppGroupGetResult> getAppGroup(@PathVariable(value = "id") String id) {
        AppGroupGetResult result = appGroupService.getAppGroup(id);
        return ApiRestResult.<AppGroupGetResult> builder().result(result).build();
    }

    /**
     * 移除应用组关联
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "移除应用组关联")
    @Audit(type = EventType.REMOVE_APP_GROUP_ASSOCIATION)
    @DeleteMapping(value = "/remove_association/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> removeAssociation(@PathVariable(value = "id") String id,
                                                    @NotEmpty(message = "应用ID不能为空") @Parameter(description = "应用ID集合") String[] appIds) {
        return ApiRestResult.<Boolean> builder()
            .result(appGroupService.batchRemoveAssociation(id, Lists.newArrayList(appIds))).build();
    }

    /**
     * 获取应用组内应用
     *
     * @param query {@link UserGroupMemberListQuery} 参数
     * @return {@link Boolean}
     */
    @Operation(summary = "获取应用组内应用")
    @GetMapping(value = "/{id}/app_list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Page<AppListResult>> getAppGroupAssociationList(PageModel model,
                                                                         AppGroupAssociationListQuery query) {
        return ApiRestResult.<Page<AppListResult>> builder()
            .result(appGroupService.getAppGroupAssociationList(model, query)).build();
    }

    /**
     * AppGroupService
     */
    private final AppGroupService appGroupService;
}
