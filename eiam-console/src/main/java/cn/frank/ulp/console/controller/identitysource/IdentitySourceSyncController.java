/*
 * eiam-console - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.console.controller.identitysource;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cn.frank.ulp.audit.annotation.Audit;
import cn.frank.ulp.audit.event.type.EventType;
import cn.frank.ulp.console.pojo.query.identity.IdentitySourceSyncHistoryListQuery;
import cn.frank.ulp.console.pojo.query.identity.IdentitySourceSyncRecordListQuery;
import cn.frank.ulp.console.pojo.result.identitysource.IdentitySourceSyncHistoryListResult;
import cn.frank.ulp.console.pojo.result.identitysource.IdentitySourceSyncRecordListResult;
import cn.frank.ulp.console.service.identitysource.IdentitySourceSyncService;
import cn.frank.ulp.support.demo.Preview;
import cn.frank.ulp.support.lock.Lock;
import cn.frank.ulp.support.repository.page.domain.Page;
import cn.frank.ulp.support.repository.page.domain.PageModel;
import cn.frank.ulp.support.result.ApiRestResult;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import static cn.frank.ulp.common.constant.AccountConstants.IDENTITY_SOURCE_PATH;

/**
 * 身份源同步记录
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/7/11 21:18
 */
@Validated
@Tag(name = "身份源同步")
@RestController
@AllArgsConstructor
@RequestMapping(value = IDENTITY_SOURCE_PATH + "/sync", produces = MediaType.APPLICATION_JSON_VALUE)
public class IdentitySourceSyncController {

    /**
     * 执行同步
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "执行身份源同步")
    @Audit(type = EventType.IDENTITY_RESOURCE_SYNC)
    @PostMapping(value = "/execute/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Void> executeIdentitySourceSync(@PathVariable String id) {
        identitySourceSyncService.executeIdentitySourceSync(id);
        return ApiRestResult.ok();
    }

    /**
     * 同步历史列表
     *
     * @return {@link IdentitySourceSyncHistoryListResult}
     */
    @Operation(summary = "同步历史列表")
    @GetMapping(value = "/history_list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Page<IdentitySourceSyncHistoryListResult>> getIdentitySourceSyncHistoryList(PageModel pageModel,
                                                                                                     IdentitySourceSyncHistoryListQuery query) {
        Page<IdentitySourceSyncHistoryListResult> results = identitySourceSyncService
            .getIdentitySourceSyncHistoryList(query, pageModel);
        return ApiRestResult.<Page<IdentitySourceSyncHistoryListResult>> builder().result(results)
            .build();
    }

    /**
     * 同步记录列表
     *
     * @return {@link IdentitySourceSyncRecordListResult}
     */
    @Operation(summary = "同步记录列表")
    @GetMapping(value = "/record_list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Page<IdentitySourceSyncRecordListResult>> getIdentitySourceSyncRecordList(PageModel pageModel,
                                                                                                   @Validated IdentitySourceSyncRecordListQuery query) {
        Page<IdentitySourceSyncRecordListResult> results = identitySourceSyncService
            .getIdentitySourceSyncRecordList(query, pageModel);
        return ApiRestResult.<Page<IdentitySourceSyncRecordListResult>> builder().result(results)
            .build();
    }

    /**
     * 身份源同步Service
     */
    private final IdentitySourceSyncService identitySourceSyncService;
}
