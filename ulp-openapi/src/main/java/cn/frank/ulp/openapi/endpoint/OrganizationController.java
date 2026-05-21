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

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cn.frank.ulp.audit.annotation.Audit;
import cn.frank.ulp.audit.event.type.EventType;
import cn.frank.ulp.openapi.common.OpenApiResponse;
import cn.frank.ulp.openapi.pojo.result.OrganizationChildResult;
import cn.frank.ulp.openapi.pojo.result.OrganizationResult;
import cn.frank.ulp.openapi.pojo.save.OrganizationCreateParam;
import cn.frank.ulp.openapi.pojo.update.OrganizationUpdateParam;
import cn.frank.ulp.openapi.service.OrganizationService;
import cn.frank.ulp.support.demo.Preview;
import cn.frank.ulp.support.lock.Lock;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import static cn.frank.ulp.openapi.constant.OpenApiV1Constants.ORGANIZATION_PATH;

/**
 * 系统账户-组织架构
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/7/11 21:18
 */
@Validated
@Tag(name = "组织架构")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = ORGANIZATION_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class OrganizationController {

    /**
     * 创建组织架构
     *
     * @param param {@link OrganizationCreateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @PostMapping(value = "/create")
    @Audit(type = EventType.CREATE_ORG)
    @Operation(summary = "创建组织")
    public OpenApiResponse<Void> createOrganization(@RequestBody @Validated OrganizationCreateParam param) {
        organizationService.createOrg(param);
        return OpenApiResponse.success();
    }

    /**
     * 修改组织架构
     *
     * @param param {@link OrganizationUpdateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "修改组织")
    @Audit(type = EventType.UPDATE_ORG)
    @PutMapping(value = "/update")
    public OpenApiResponse<Void> updateOrganization(@RequestBody @Validated OrganizationUpdateParam param) {
        organizationService.updateOrg(param);
        return OpenApiResponse.success();
    }

    /**
     * 删除组织架构
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "删除组织")
    @Audit(type = EventType.DELETE_ORG)
    @DeleteMapping(value = "/delete/{id}")
    public OpenApiResponse<Void> deleteOrganization(@PathVariable(value = "id") String id) {
        organizationService.deleteOrg(id);
        return OpenApiResponse.success();
    }

    /**
     * 启用组织
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "启用组织")
    @PutMapping(value = "/enable/{id}")
    public OpenApiResponse<Void> enableOrganization(@PathVariable(value = "id") String id) {
        organizationService.updateStatus(id, Boolean.TRUE);
        return OpenApiResponse.success();
    }

    /**
     * 禁用组织
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "禁用组织")
    @PutMapping(value = "/disable/{id}")
    public OpenApiResponse<Void> disableOrganization(@PathVariable(value = "id") String id) {
        organizationService.updateStatus(id, Boolean.FALSE);
        return OpenApiResponse.success();
    }

    /**
     * 根据父ID查询子组织树
     *
     * @return {@link List}
     */
    @GetMapping(value = "/{parentId}/lst")
    @Operation(summary = "根据父ID查询子组织列表")
    public OpenApiResponse<List<OrganizationChildResult>> getChildOrganization(@PathVariable(value = "parentId") String parentId) {
        List<OrganizationChildResult> list = organizationService.getChildOrganization(parentId);
        return OpenApiResponse.success(list);
    }

    /**
     * 根据ID查询组织
     *
     * @param id {@link String}
     * @return {@link OrganizationResult}
     */
    @Operation(summary = "根据组织ID查询组织")
    @GetMapping(value = "/{id}")
    public OpenApiResponse<OrganizationResult> getOrganization(@PathVariable(value = "id") String id) {
        OrganizationResult result = organizationService.getOrganizationById(id);
        return OpenApiResponse.success(result);
    }

    /**
     * 根据外部ID获取组织ID
     *
     * @param externalId {@link String}
     * @return {@link String}
     */
    @Operation(summary = "根据外部ID获取组织ID")
    @GetMapping(value = "/{externalId}/id")
    public OpenApiResponse<String> getOrganizationIdByExternalId(@PathVariable(value = "externalId") String externalId) {
        return OpenApiResponse
            .success(organizationService.getOrganizationIdByExternalId(externalId));
    }

    /**
     * 组织架构服务实现类
     */
    private final OrganizationService organizationService;
}
