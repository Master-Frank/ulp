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
import cn.frank.ulp.console.pojo.query.app.AppAccountQuery;
import cn.frank.ulp.console.pojo.result.app.AppAccountListResult;
import cn.frank.ulp.console.pojo.save.app.AppAccountCreateParam;
import cn.frank.ulp.console.service.app.AppAccountService;
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
 * 应用账户资源
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/6/4 21:06
 */
@Validated
@Tag(name = "应用账户")
@RestController
@AllArgsConstructor
@RequestMapping(value = APP_PATH + "/account", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppAccountController {

    /**
     * 获取应用账户列表
     *
     * @param page  {@link PageModel}
     * @param query {@link AppAccountQuery}
     * @return {@link AppAccountListResult}
     */
    @Operation(summary = "获取应用账户列表")
    @GetMapping(value = "/list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Page<AppAccountListResult>> getAppAccountList(PageModel page,
                                                                       @Validated AppAccountQuery query) {
        return ApiRestResult.<Page<AppAccountListResult>> builder()
            .result(appAccountService.getAppAccountList(page, query)).build();
    }

    /**
     * 创建应用账户
     *
     * @param param {@link AppAccountCreateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "创建应用账户")
    @Audit(type = EventType.ADD_APP_ACCOUNT)
    @PostMapping(value = "/create")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> createAppAccount(@RequestBody @Validated AppAccountCreateParam param) {
        return ApiRestResult.<Boolean> builder().result(appAccountService.createAppAccount(param))
            .build();
    }

    /**
     * 删除应用账户
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "删除应用账户")
    @Audit(type = EventType.DELETE_APP_ACCOUNT)
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> deleteAppAccount(@PathVariable(value = "id") String id) {
        return ApiRestResult.<Boolean> builder().result(appAccountService.deleteAppAccount(id))
            .build();
    }

    /**
     * 设置应用账户是否默认
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "设置应用账户为默认")
    @Audit(type = EventType.UPDATE_DEFAULT_APP_ACCOUNT)
    @PutMapping(value = "/activate_default/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> updateAppAccountSetDefault(@PathVariable(value = "id") String id) {
        return ApiRestResult.<Boolean> builder()
            .result(appAccountService.updateAppAccountDefault(id, true)).build();
    }

    /**
     * 设置应用账户是否默认
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "取消应用账户为默认")
    @Audit(type = EventType.UPDATE_DEFAULT_APP_ACCOUNT)
    @PutMapping(value = "/deactivate_default/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> updateAppAccountUnsetDefault(@PathVariable(value = "id") String id) {
        return ApiRestResult.<Boolean> builder()
            .result(appAccountService.updateAppAccountDefault(id, false)).build();
    }

    /**
     * AppAccountService
     */
    private final AppAccountService appAccountService;

}
