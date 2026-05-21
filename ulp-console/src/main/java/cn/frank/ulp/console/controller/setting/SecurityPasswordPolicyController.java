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

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cn.frank.ulp.audit.annotation.Audit;
import cn.frank.ulp.audit.event.type.EventType;
import cn.frank.ulp.console.pojo.result.setting.PasswordPolicyConfigResult;
import cn.frank.ulp.console.pojo.result.setting.WeakPasswordLibListResult;
import cn.frank.ulp.console.pojo.save.setting.PasswordPolicySaveParam;
import cn.frank.ulp.console.service.setting.PasswordPolicyService;
import cn.frank.ulp.support.demo.Preview;
import cn.frank.ulp.support.lock.Lock;
import cn.frank.ulp.support.result.ApiRestResult;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import static cn.frank.ulp.common.constant.SettingConstants.SECURITY_PATH;

/**
 * 密码策略配置 Controller
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/05/05 21:18
 */
@Validated
@Tag(name = "密码策略配置")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = SECURITY_PATH
                        + "/password_policy", produces = MediaType.APPLICATION_JSON_VALUE)
public class SecurityPasswordPolicyController {

    /**
     * 密码策略配置
     *
     * @return {@link List}
     */
    @GetMapping(value = "/config")
    @Operation(summary = "获取密码策略")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<PasswordPolicyConfigResult> config() {
        PasswordPolicyConfigResult result = passwordPolicyService.getPasswordPolicyConfig();
        return ApiRestResult.<PasswordPolicyConfigResult> builder().result(result).build();
    }

    /**
     * 保存密码策略
     *
     * @return {@link List}
     */
    @Lock
    @Preview
    @PostMapping(value = "/save")
    @Audit(type = EventType.SAVE_PASSWORD_POLICY)
    @Operation(summary = "保存密码策略")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> save(@Validated @RequestBody PasswordPolicySaveParam param) {
        return ApiRestResult.<Boolean> builder()
            .result(passwordPolicyService.savePasswordPolicyConfig(param)).build();
    }

    /**
     * 系统弱密码库
     *
     * @return {@link List}
     */
    @GetMapping(value = "/weak_password_lib")
    @Operation(summary = "获取系统弱密码库")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<List<WeakPasswordLibListResult>> getWeakPasswordLibList() {
        List<WeakPasswordLibListResult> result = passwordPolicyService.getWeakPasswordLibList();

        return ApiRestResult.<List<WeakPasswordLibListResult>> builder().result(result).build();
    }

    /**
     * 密码策略实现类
     */
    private final PasswordPolicyService passwordPolicyService;
}
