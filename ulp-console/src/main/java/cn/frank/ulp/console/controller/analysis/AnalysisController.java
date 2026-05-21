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
package cn.frank.ulp.console.controller.analysis;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.frank.ulp.audit.repository.result.AuthnQuantityResult;
import cn.frank.ulp.audit.repository.result.AuthnZoneResult;
import cn.frank.ulp.console.pojo.query.analysis.AnalysisQuery;
import cn.frank.ulp.console.pojo.result.analysis.AppVisitRankResult;
import cn.frank.ulp.console.pojo.result.analysis.AuthnHotProviderResult;
import cn.frank.ulp.console.pojo.result.analysis.OverviewResult;
import cn.frank.ulp.console.service.analysis.AnalysisService;
import cn.frank.ulp.support.result.ApiRestResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import static cn.frank.ulp.common.constant.AnalysisConstants.ANALYSIS_PATH;

/**
 * 统计分析
 *
 * @author Frank Zhang
 */
@Validated
@Tag(name = "统计分析")
@RestController
@RequestMapping(ANALYSIS_PATH)
public class AnalysisController {

    /**
     * 概述
     *
     * @return {@link OverviewResult}
     */
    @GetMapping("/overview")
    @Operation(summary = "概述")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<OverviewResult> overview() {
        return ApiRestResult.ok(analysisService.overview());
    }

    /**
     * 认证量
     *
     * @param query {@link AnalysisQuery}
     * @return {@link AuthnQuantityResult}
     */
    @GetMapping("/authn/quantity")
    @Operation(summary = "认证量")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<List<AuthnQuantityResult>> authnQuantity(@Validated AnalysisQuery query) {
        return ApiRestResult.ok(analysisService.authnQuantity(query));
    }

    /**
     * 热门认证提供商
     *
     * @return {@link List}
     */
    @GetMapping("/authn/hot_provider")
    @Operation(summary = "热门认证提供商")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<List<AuthnHotProviderResult>> authnHotProvider(@Validated AnalysisQuery query) {
        return ApiRestResult.ok(analysisService.authnHotProvider(query));
    }

    /**
     * 登录区域
     */
    @GetMapping("/authn/zone")
    @Operation(summary = "登录区域")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<List<AuthnZoneResult>> authnZone(@Validated AnalysisQuery query) {
        return ApiRestResult.ok(analysisService.authnZone(query));
    }

    /**
     * 访问应用排名
     *
     * @param query {@link AnalysisQuery}
     * @return {@link AuthnQuantityResult}
     */
    @GetMapping("/app/visit_rank")
    @Operation(summary = "访问应用排名")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<List<AppVisitRankResult>> appVisitRank(@Validated AnalysisQuery query) {
        return ApiRestResult.ok(analysisService.appVisitRank(query));
    }

    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

}
