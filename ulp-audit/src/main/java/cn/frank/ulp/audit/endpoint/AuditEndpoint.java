/*
 * ulp-audit - United Login Platform
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
package cn.frank.ulp.audit.endpoint;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.frank.ulp.audit.endpoint.pojo.AuditListQuery;
import cn.frank.ulp.audit.endpoint.pojo.AuditListResult;
import cn.frank.ulp.audit.endpoint.pojo.DictResult;
import cn.frank.ulp.audit.service.AuditService;
import cn.frank.ulp.support.repository.page.domain.Page;
import cn.frank.ulp.support.repository.page.domain.PageModel;
import cn.frank.ulp.support.result.ApiRestResult;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import static cn.frank.ulp.support.constant.EiamConstants.V1_API_PATH;

/**
 * 系统审计
 *
 * @author Frank Zhang
 */
@Validated
@Tag(name = "系统审计")
@RestController
@RequestMapping(value = AuditEndpoint.AUDIT_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AuditEndpoint {

    /**
     * 系统审计API路径
     */
    public final static String AUDIT_PATH = V1_API_PATH + "/audit";

    /**
     * 审计列表查询
     *
     * @param query     {@link AuditListQuery}
     * @param pageModel {@link PageModel}
     * @return {@link ApiRestResult}
     */
    @Operation(description = "查询审计列表")
    @GetMapping(value = "/list")
    public ApiRestResult<Page<AuditListResult>> getAuditList(@Validated AuditListQuery query,
                                                             PageModel pageModel) {
        Page<AuditListResult> list = auditService.getAuditList(query, pageModel);
        return ApiRestResult.ok(list);
    }

    /**
     * 获取审计字典类型
     *
     * @return {@link ApiRestResult}
     */
    @Validated
    @Operation(description = "获取审计类型")
    @GetMapping(value = "/types/{user_type}")
    public ApiRestResult<List<DictResult>> getAuditDict(@PathVariable(name = "user_type") @NotNull(message = "用户类型不能为空！") String userType) {
        List<DictResult> dict = auditService.getAuditDict(userType);
        return ApiRestResult.ok(dict);
    }

    /**
     * AuditService
     */
    private final AuditService auditService;
}
