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

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.frank.ulp.console.pojo.query.app.AppCertQuery;
import cn.frank.ulp.console.pojo.result.app.AppCertListResult;
import cn.frank.ulp.console.service.app.AppCertService;
import cn.frank.ulp.support.result.ApiRestResult;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import static cn.frank.ulp.common.constant.AppConstants.APP_PATH;

/**
 * 应用证书
 *
 * @author Frank Zhang
 */
@Validated
@Tag(name = "应用证书")
@RestController
@AllArgsConstructor
@RequestMapping(value = APP_PATH + "/cert", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppCertController {

    /**
     * 获取应用证书列表
     *
     * @param query {@link AppCertQuery}
     * @return {@link AppCertListResult}
     */
    @Operation(summary = "获取应用证书列表")
    @GetMapping(value = "/list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<List<AppCertListResult>> getAppCertListResult(@Validated AppCertQuery query) {
        List<AppCertListResult> list = appCertService.getAppCertListResult(query);
        return ApiRestResult.<List<AppCertListResult>> builder().result(list).build();
    }

    private final AppCertService appCertService;
}
