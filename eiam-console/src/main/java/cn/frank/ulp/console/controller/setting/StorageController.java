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
import cn.frank.ulp.console.pojo.result.setting.StorageProviderConfigResult;
import cn.frank.ulp.console.pojo.save.setting.StorageConfigSaveParam;
import cn.frank.ulp.console.service.setting.StorageSettingService;
import cn.frank.ulp.support.demo.Preview;
import cn.frank.ulp.support.lock.Lock;
import cn.frank.ulp.support.result.ApiRestResult;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import static cn.frank.ulp.common.constant.SettingConstants.SETTING_PATH;

/**
 * 存储配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/10/30 21:16
 */
@Validated
@Tag(name = "存储配置")
@RestController
@RequestMapping(value = SETTING_PATH + "/storage", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class StorageController {

    /**
     * 启用/禁用存储配置服务
     *
     * @return {@link ApiRestResult}
     */
    @Lock
    @Preview
    @Validated
    @Operation(summary = "禁用存储服务")
    @Audit(type = EventType.OFF_STORAGE_SERVICE)
    @PutMapping(value = "/disable")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> disableStorage() {
        Boolean result = storageSettingsService.disableStorage();
        return ApiRestResult.ok(result);
    }

    /**
     * 保存存储配置
     *
     * @param param {@link StorageConfigSaveParam}
     * @return {@link ApiRestResult}
     */
    @Lock
    @Preview
    @Validated
    @Operation(summary = "保存存储服务配置")
    @Audit(type = EventType.SAVE_STORAGE_SERVICE)
    @PostMapping(value = "/save")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> saveStorageConfig(@RequestBody StorageConfigSaveParam param) {
        Boolean result = storageSettingsService.saveStorageConfig(param);
        return ApiRestResult.ok(result);
    }

    /**
     * 获取存储配置
     *
     * @return {@link ApiRestResult}
     */
    @Operation(summary = "获取存储服务配置")
    @GetMapping(value = "/config")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<StorageProviderConfigResult> getStorageConfig() {
        StorageProviderConfigResult result = storageSettingsService.getStorageConfig();
        return ApiRestResult.ok(result);
    }

    /**
     * StorageSettingsService
     */
    private final StorageSettingService storageSettingsService;
}
