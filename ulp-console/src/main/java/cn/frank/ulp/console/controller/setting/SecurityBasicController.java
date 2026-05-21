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
import cn.frank.ulp.console.pojo.result.setting.SecurityBasicConfigResult;
import cn.frank.ulp.console.pojo.save.setting.SecurityBasicSaveParam;
import cn.frank.ulp.console.service.setting.SecuritySettingService;
import cn.frank.ulp.support.demo.Preview;
import cn.frank.ulp.support.lock.Lock;
import cn.frank.ulp.support.result.ApiRestResult;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import static cn.frank.ulp.common.constant.SettingConstants.SECURITY_PATH;

/**
 * 安全设置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/10/4 21:37
 */
@Validated
@Tag(name = "安全基础设置")
@RestController
@RequestMapping(value = SECURITY_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class SecurityBasicController {

    /**
     * 获取高级配置
     *
     * @return {@link SecurityBasicConfigResult}
     */
    @Operation(summary = "获取基础配置")
    @GetMapping(value = "/basic/config")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<SecurityBasicConfigResult> getBasicConfig() {
        SecurityBasicConfigResult result = securitySettingService.getBasicConfig();
        return ApiRestResult.<SecurityBasicConfigResult> builder().result(result).build();
    }

    /**
     * 保存高级配置
     *
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "保存基础配置")
    @Audit(type = EventType.SAVE_LOGIN_SECURITY_BASIC_SETTINGS)
    @PostMapping(value = "/basic/save")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> save(@Validated @RequestBody SecurityBasicSaveParam param) {
        return ApiRestResult.<Boolean> builder()
            .result(securitySettingService.saveBasicConfig(param)).build();
    }

    /**
     * SecuritySettingService
     */
    private final SecuritySettingService securitySettingService;
}
