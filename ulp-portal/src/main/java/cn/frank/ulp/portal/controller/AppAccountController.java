/*
 * ulp-portal - United Login Platform
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
package cn.frank.ulp.portal.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cn.frank.ulp.application.AppAccount;
import cn.frank.ulp.audit.annotation.Audit;
import cn.frank.ulp.audit.event.type.EventType;
import cn.frank.ulp.portal.pojo.request.AppAccountRequest;
import cn.frank.ulp.portal.service.AppAccountService;
import cn.frank.ulp.support.demo.Preview;
import cn.frank.ulp.support.lock.Lock;
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
     * 获取当前登陆者应用账户列表
     *
     * @param appId  {@link String}
     * @return {@link }
     */
    @Operation(summary = "获取当前登陆者应用账户")
    @GetMapping("/{appId}")
    public ApiRestResult<List<AppAccount>> getAppAccountList(@PathVariable String appId) {
        List<AppAccount> appAccounts = appAccountService.getAppAccountList(appId);
        return ApiRestResult.ok(appAccounts);
    }

    /**
     * 创建应用账户
     *
     * @param param {@link AppAccountRequest}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "创建应用账户")
    @Audit(type = EventType.ADD_APP_ACCOUNT)
    @PostMapping(value = "/create")
    public ApiRestResult<Boolean> createAppAccount(@RequestBody @Validated AppAccountRequest param) {
        return ApiRestResult.<Boolean> builder().result(appAccountService.createAppAccount(param))
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
    public ApiRestResult<Boolean> deleteAppAccount(@PathVariable(value = "id") String id) {
        return ApiRestResult.<Boolean> builder().result(appAccountService.deleteAppAccount(id))
            .build();
    }

    /**
     * AppAccountService
     */
    private final AppAccountService appAccountService;

}
