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
package cn.frank.ulp.console.controller.identitysource;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.frank.ulp.console.pojo.query.identity.IdentitySourceEventRecordListQuery;
import cn.frank.ulp.console.pojo.result.identitysource.IdentitySourceEventRecordListResult;
import cn.frank.ulp.console.service.identitysource.IdentitySourceEventRecordService;
import cn.frank.ulp.support.repository.page.domain.Page;
import cn.frank.ulp.support.repository.page.domain.PageModel;
import cn.frank.ulp.support.result.ApiRestResult;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import static cn.frank.ulp.common.constant.AccountConstants.IDENTITY_SOURCE_PATH;

/**
 * 身份源事件记录
 *
 * @author Frank Zhang
 */
@Validated
@Tag(name = "身份源事件")
@RestController
@AllArgsConstructor
@RequestMapping(value = IDENTITY_SOURCE_PATH
                        + "/event", produces = MediaType.APPLICATION_JSON_VALUE)
public class IdentitySourceEventController {

    /**
     * 身份源事件列表
     *
     * @return {@link IdentitySourceEventRecordListResult}
     */
    @Operation(summary = "身份源事件列表")
    @GetMapping(value = "/record_list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Page<IdentitySourceEventRecordListResult>> getIdentitySourceSynchronizeRecordList(PageModel pageModel,
                                                                                                           IdentitySourceEventRecordListQuery query) {
        Page<IdentitySourceEventRecordListResult> results = identitySourceEventRecordService
            .getIdentitySourceEventRecordList(query, pageModel);
        return ApiRestResult.<Page<IdentitySourceEventRecordListResult>> builder().result(results)
            .build();
    }

    /**
     * 身份源同步Service
     */
    private final IdentitySourceEventRecordService identitySourceEventRecordService;
}
