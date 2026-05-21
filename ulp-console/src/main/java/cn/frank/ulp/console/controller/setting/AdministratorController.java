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
package cn.frank.ulp.console.controller.setting;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cn.frank.ulp.audit.annotation.Audit;
import cn.frank.ulp.audit.event.type.EventType;
import cn.frank.ulp.common.enums.CheckValidityType;
import cn.frank.ulp.common.enums.UserStatus;
import cn.frank.ulp.console.pojo.query.setting.AdministratorListQuery;
import cn.frank.ulp.console.pojo.result.setting.AdministratorListResult;
import cn.frank.ulp.console.pojo.result.setting.AdministratorResult;
import cn.frank.ulp.console.pojo.save.setting.AdministratorCreateParam;
import cn.frank.ulp.console.pojo.update.setting.AdministratorUpdateParam;
import cn.frank.ulp.console.service.setting.AdministratorService;
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
import jakarta.validation.constraints.NotNull;
import static cn.frank.ulp.common.constant.SettingConstants.SETTING_PATH;

/**
 * 管理员
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/13 22:09
 */
@Validated
@Tag(name = "系统管理员")
@RestController
@AllArgsConstructor
@RequestMapping(value = SETTING_PATH
                        + "/administrator", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdministratorController {
    /**
     * 获取管理员列表
     *
     * @param model {@link PageModel}
     * @return {@link AdministratorListResult}
     */
    @Operation(summary = "获取管理员列表")
    @GetMapping(value = "/list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Page<AdministratorListResult>> getAdministratorList(PageModel model,
                                                                             AdministratorListQuery query) {
        Page<AdministratorListResult> result = administratorService.getAdministratorList(model,
            query);
        return ApiRestResult.<Page<AdministratorListResult>> builder().result(result).build();
    }

    /**
     * 创建管理员
     *
     * @param param {@link AdministratorCreateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "创建管理员")
    @Audit(type = EventType.ADD_ADMINISTRATOR)
    @PostMapping(value = "/create")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> createAdministrator(@RequestBody @Validated AdministratorCreateParam param) {
        Boolean result = administratorService.createAdministrator(param);
        return ApiRestResult.<Boolean> builder().result(result).build();
    }

    /**
     * 修改管理员
     *
     * @param param {@link AdministratorUpdateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "修改管理员")
    @Audit(type = EventType.UPDATE_ADMINISTRATOR)
    @PutMapping(value = "/update")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> updateAdministrator(@RequestBody @Validated AdministratorUpdateParam param) {
        Boolean result = administratorService.updateAdministrator(param);
        return ApiRestResult.<Boolean> builder().result(result).build();
    }

    /**
     * 删除管理员
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "删除管理员")
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    @Audit(type = EventType.DELETE_ADMINISTRATOR)
    public ApiRestResult<Boolean> deleteAdministrator(@PathVariable(value = "id") String id) {
        Boolean result = administratorService.deleteAdministrator(id);
        return ApiRestResult.<Boolean> builder().result(result).build();
    }

    /**
     * 启用管理员
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "启用管理员")
    @Audit(type = EventType.ENABLE_ADMINISTRATOR)
    @PutMapping(value = "/enable/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> enableAdministrator(@PathVariable(value = "id") String id) {
        Boolean result = administratorService.updateAdministratorStatus(id, UserStatus.ENABLED);
        return ApiRestResult.<Boolean> builder().result(result).build();
    }

    /**
     * 禁用管理员
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "禁用管理员")
    @Audit(type = EventType.DISABLE_ADMINISTRATOR)
    @PutMapping(value = "/disable/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> disableAdministrator(@PathVariable(value = "id") String id) {
        Boolean result = administratorService.updateAdministratorStatus(id, UserStatus.DISABLED);
        return ApiRestResult.<Boolean> builder().result(result).build();
    }

    /**
     * 重置管理员密码
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Validated
    @Operation(summary = "重置管理员密码")
    @Audit(type = EventType.RESET_ADMINISTRATOR_PASSWORD)
    @PutMapping(value = "/reset_password")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> resetAdministratorPassword(@NotEmpty(message = "ID不能为空") @Parameter(description = "ID") String id,
                                                             @NotEmpty(message = "密码不能为空") @Parameter(description = "密码") String password) {
        Boolean result = administratorService.resetAdministratorPassword(id, password);
        return ApiRestResult.<Boolean> builder().result(result).build();
    }

    /**
     * 根据ID获取管理员
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Operation(summary = "获取管理员信息")
    @GetMapping(value = "/get/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<AdministratorResult> getAdministrator(@PathVariable(value = "id") String id) {
        AdministratorResult result = administratorService.getAdministrator(id);
        //返回
        return ApiRestResult.<AdministratorResult> builder().result(result).build();
    }

    /**
     * 参数有效性验证
     *
     * @return {@link Boolean}
     */
    @Operation(summary = "参数有效性验证")
    @GetMapping(value = "/param_check")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> administratorParamCheck(@Parameter(description = "验证类型") @NotNull(message = "验证类型不能为空") CheckValidityType type,
                                                          @Parameter(description = "值") @NotEmpty(message = "验证值不能为空") String value,
                                                          @Parameter(description = "ID") String id) {
        Boolean result = administratorService.administratorParamCheck(type, value, id);
        //返回
        return ApiRestResult.<Boolean> builder().result(result).build();
    }

    /**
     * AdministratorService
     */
    private final AdministratorService administratorService;
}
