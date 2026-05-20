/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.console.controller.setting;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cn.frank.ulp.audit.annotation.Audit;
import cn.frank.ulp.audit.event.type.EventType;
import cn.frank.ulp.console.pojo.result.setting.SecurityDefensePolicyConfigResult;
import cn.frank.ulp.console.pojo.save.setting.SecurityDefensePolicyParam;
import cn.frank.ulp.console.service.setting.SecurityDefensePolicyService;
import cn.frank.ulp.support.demo.Preview;
import cn.frank.ulp.support.lock.Lock;
import cn.frank.ulp.support.result.ApiRestResult;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import static cn.frank.ulp.common.constant.SettingConstants.SETTING_PATH;

/**
 * 安全防御策略
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023-03-09
 */
@Validated
@Tag(name = "安全防御策略")
@RestController
@AllArgsConstructor
@RequestMapping(value = SETTING_PATH
                        + "/security/defense_policy", produces = MediaType.APPLICATION_JSON_VALUE)
public class SecurityDefensePolicyController {

    /**
     * 保存安全策略
     *
     * @param param {@link SecurityDefensePolicyParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "保存安全防御策略")
    @Audit(type = EventType.SAVE_SECURITY_POLICY_SETTINGS)
    @PostMapping(value = "/save")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> saveSecurityDefenseStrategyConfig(@RequestBody @Validated SecurityDefensePolicyParam param) {
        Boolean result = securityDefenseStrategyService.saveSecurityDefensePolicyConfig(param);
        return ApiRestResult.<Boolean> builder().result(result).build();
    }

    /**
     * 获取安全策略
     *
     * @return {@link SecurityDefensePolicyConfigResult}
     */
    @Operation(summary = "获取安全策略")
    @GetMapping(value = "/config")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<SecurityDefensePolicyConfigResult> getSecurityPolicyConfig() {
        SecurityDefensePolicyConfigResult result = securityDefenseStrategyService
            .getSecurityPolicyConfig();
        //返回
        return ApiRestResult.<SecurityDefensePolicyConfigResult> builder().result(result).build();
    }

    /**
     * SecurityDefenseStrategy
     */
    private final SecurityDefensePolicyService securityDefenseStrategyService;
}
