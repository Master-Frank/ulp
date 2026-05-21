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
package cn.frank.ulp.console.controller.authn;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cn.frank.ulp.audit.annotation.Audit;
import cn.frank.ulp.audit.event.type.EventType;
import cn.frank.ulp.common.constant.AuthnConstants;
import cn.frank.ulp.console.pojo.query.authn.IdentityProviderListQuery;
import cn.frank.ulp.console.pojo.result.authn.IdentityProviderCreateResult;
import cn.frank.ulp.console.pojo.result.authn.IdentityProviderListResult;
import cn.frank.ulp.console.pojo.result.authn.IdentityProviderResult;
import cn.frank.ulp.console.pojo.save.authn.IdentityProviderCreateParam;
import cn.frank.ulp.console.pojo.update.authn.IdpUpdateParam;
import cn.frank.ulp.console.service.authn.IdentityProviderService;
import cn.frank.ulp.support.demo.Preview;
import cn.frank.ulp.support.lock.Lock;
import cn.frank.ulp.support.repository.page.domain.Page;
import cn.frank.ulp.support.repository.page.domain.PageModel;
import cn.frank.ulp.support.result.ApiRestResult;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 身份提供商
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/7/11 21:18
 */
@Tag(name = "身份提供商")
@Validated
@RestController
@RequestMapping(value = AuthnConstants.AUTHN_PATH
                        + "/idp", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class IdentityProviderController {

    /**
     * 获取列表
     *
     * @return {@link IdentityProviderListResult}
     */
    @Operation(summary = "提供商列表")
    @GetMapping(value = "/list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Page<IdentityProviderListResult>> getIdentityProviderList(PageModel pageModel,
                                                                                   IdentityProviderListQuery query) {
        Page<IdentityProviderListResult> results = identityProviderService
            .getIdentityProviderList(pageModel, query);
        return ApiRestResult.<Page<IdentityProviderListResult>> builder().result(results).build();
    }

    /**
     * 创建提供商
     *
     * @param param {@link IdentityProviderCreateParam}
     * @return {@link IdentityProviderCreateResult}
     */
    @Lock
    @Preview
    @Operation(summary = "创建提供商")
    @Audit(type = EventType.ADD_IDP)
    @PostMapping(value = "/create")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<IdentityProviderCreateResult> createIdentityProvider(@RequestBody @Validated IdentityProviderCreateParam param) {
        IdentityProviderCreateResult result = identityProviderService.createIdp(param);
        return ApiRestResult.<IdentityProviderCreateResult> builder().result(result).build();
    }

    /**
     * 修改提供商
     *
     * @param param {@link IdentityProviderCreateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "修改提供商")
    @Audit(type = EventType.UPDATE_IDP)
    @PutMapping(value = "/update")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> updateIdentityProvider(@RequestBody @Validated IdpUpdateParam param) {
        boolean success = identityProviderService.updateIdentityProvider(param);
        return ApiRestResult.<Boolean> builder().result(success).build();
    }

    /**
     * 详情
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Operation(summary = "获取提供商信息")
    @GetMapping(value = "/get/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<IdentityProviderResult> getIdentityProvider(@PathVariable(value = "id") String id) {
        IdentityProviderResult result = identityProviderService.getIdentityProvider(id);
        return ApiRestResult.<IdentityProviderResult> builder().result(result).build();
    }

    /**
     * 启用提供商
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "启用提供商")
    @Audit(type = EventType.ENABLE_IDP)
    @PutMapping(value = "/enable/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> enableIdentityProvider(@PathVariable(value = "id") String id) {
        boolean result = identityProviderService.updateIdentityProviderStatus(id, true);
        return ApiRestResult.<Boolean> builder().result(result).build();
    }

    /**
     * 禁用提供商
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "禁用提供商")
    @Audit(type = EventType.DISABLE_IDP)
    @PutMapping(value = "/disable/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> disableIdentityProvider(@PathVariable(value = "id") String id) {
        boolean result = identityProviderService.updateIdentityProviderStatus(id, false);
        return ApiRestResult.<Boolean> builder().result(result).build();
    }

    /**
     * 删除提供商
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "删除提供商")
    @Audit(type = EventType.DELETE_IDP)
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> deleteIdentityProvider(@PathVariable(value = "id") String id) {
        boolean result = identityProviderService.deleteIdentityProvider(id);
        return ApiRestResult.<Boolean> builder().result(result).build();
    }

    /**
     * 身份提供商服务
     */
    private final IdentityProviderService identityProviderService;
}
