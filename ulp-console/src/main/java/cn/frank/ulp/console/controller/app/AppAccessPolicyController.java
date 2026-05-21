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

import cn.frank.ulp.audit.annotation.Audit;
import cn.frank.ulp.audit.event.type.EventType;
import cn.frank.ulp.console.pojo.query.app.AppAccessPolicyQuery;
import cn.frank.ulp.console.pojo.result.app.AppAccessPolicyResult;
import cn.frank.ulp.console.pojo.save.app.AppAccessPolicyCreateParam;
import cn.frank.ulp.console.pojo.save.app.AppAccountCreateParam;
import cn.frank.ulp.console.service.app.AppAccessPolicyService;
import cn.frank.ulp.support.demo.Preview;
import cn.frank.ulp.support.lock.Lock;
import cn.frank.ulp.support.repository.page.domain.Page;
import cn.frank.ulp.support.repository.page.domain.PageModel;
import cn.frank.ulp.support.result.ApiRestResult;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import static cn.frank.ulp.common.constant.AppConstants.APP_PATH;

/**
 * 应用访问授权策略
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/6/4 21:58
 */
@Validated
@Tag(name = "应用访问授权策略")
@RestController
@AllArgsConstructor
@RequestMapping(value = APP_PATH + "/access_policy", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppAccessPolicyController {

    /**
     * 获取应用访问授权策略列表
     *
     * @param page  {@link PageModel}
     * @param query {@link AppAccessPolicyQuery}
     * @return {@link AppAccessPolicyResult}
     */
    @Operation(summary = "获取应用访问授权策略列表")
    @GetMapping(value = "/list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Page<AppAccessPolicyResult>> getAppAccessPolicyList(PageModel page,
                                                                             @Validated AppAccessPolicyQuery query) {
        return ApiRestResult.<Page<AppAccessPolicyResult>> builder()
            .result(appAccessPolicyService.getAppAccessPolicyList(page, query)).build();
    }

    /**
     * 创建应用访问授权策略
     *
     * @param param {@link AppAccountCreateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "创建应用访问授权")
    @Audit(type = EventType.APP_AUTHORIZATION)
    @PostMapping(value = "/create")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> createAppAccessPolicy(@RequestBody @Validated AppAccessPolicyCreateParam param) {
        return ApiRestResult.<Boolean> builder()
            .result(appAccessPolicyService.createAppAccessPolicy(param)).build();
    }

    /**
     * 删除应用访问授权策略
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "取消应用访问授权")
    @Audit(type = EventType.APP_CANCEL_ACCESS_POLICY)
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> deleteAppAccessPolicy(@PathVariable(value = "id") String id) {
        return ApiRestResult.<Boolean> builder()
            .result(appAccessPolicyService.deleteAppAccessPolicy(id)).build();
    }

    /**
     * 启用应用访问授权
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "启用应用访问授权")
    @Audit(type = EventType.ENABLE_APP_ACCESS_POLICY)
    @PutMapping(value = "/enable/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> enableAppAccessPolicy(@PathVariable(value = "id") String id) {
        Boolean result = appAccessPolicyService.enableAppAccessPolicy(id);
        return ApiRestResult.<Boolean> builder().result(result).build();
    }

    /**
     * 禁用应用访问授权
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "禁用应用访问授权")
    @Audit(type = EventType.DISABLE_APP_ACCESS_POLICY)
    @PutMapping(value = "/disable/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> disableAppAccessPolicy(@PathVariable(value = "id") String id) {
        Boolean result = appAccessPolicyService.disableAppAccessPolicy(id);
        return ApiRestResult.<Boolean> builder().result(result).build();
    }

    /**
     * AppPolicyService
     */
    private final AppAccessPolicyService appAccessPolicyService;
}
