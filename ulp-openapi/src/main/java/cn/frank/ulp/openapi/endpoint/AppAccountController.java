/*
 * ulp-openapi - United Login Platform
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
package cn.frank.ulp.openapi.endpoint;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cn.frank.ulp.audit.annotation.Audit;
import cn.frank.ulp.audit.event.type.EventType;
import cn.frank.ulp.openapi.common.OpenApiResponse;
import cn.frank.ulp.openapi.pojo.query.OapiV1AppAccountQuery;
import cn.frank.ulp.openapi.pojo.result.AppAccountListResult;
import cn.frank.ulp.openapi.pojo.save.AppAccountCreateParam;
import cn.frank.ulp.openapi.service.AppAccountService;
import cn.frank.ulp.support.demo.Preview;
import cn.frank.ulp.support.lock.Lock;
import cn.frank.ulp.support.repository.page.domain.Page;
import cn.frank.ulp.support.repository.page.domain.PageModel;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import static cn.frank.ulp.openapi.constant.OpenApiV1Constants.APP_ACCOUNT_PATH;

/**
 * 应用账户
 *
 * @author xlsea
 * @since 2024-01-15
 */
@Validated
@Tag(name = "应用账户")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = APP_ACCOUNT_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class AppAccountController {

    /**
     * 获取应用账户列表
     *
     * @param page  {@link PageModel}
     * @param query {@link OapiV1AppAccountQuery}
     * @return {@link AppAccountListResult}
     */
    @Operation(summary = "获取应用账户列表")
    @GetMapping(value = "/list")
    public OpenApiResponse<Page<AppAccountListResult>> getAppAccountList(PageModel page,
                                                                         @Validated OapiV1AppAccountQuery query) {
        return OpenApiResponse.success((appAccountService.getAppAccountList(page, query)));
    }

    /**
     * 创建应用账户
     *
     * @param param {@link AppAccountCreateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @PostMapping(value = "/create")
    @Audit(type = EventType.ADD_APP_ACCOUNT)
    @Operation(summary = "创建应用账户")
    public OpenApiResponse<Void> createAppAccount(@RequestBody @Validated AppAccountCreateParam param) {
        appAccountService.createAppAccount(param);
        return OpenApiResponse.success();
    }

    /**
     * 删除应用账户
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "删除应用账户")
    @Audit(type = EventType.DELETE_APP_ACCOUNT)
    @DeleteMapping(value = "/delete/{id}")
    public OpenApiResponse<Void> deleteAppAccount(@PathVariable(value = "id") String id) {
        appAccountService.deleteAppAccount(id);
        return OpenApiResponse.success();
    }

    /**
     * 应用账户服务类
     */
    private final AppAccountService appAccountService;
}
