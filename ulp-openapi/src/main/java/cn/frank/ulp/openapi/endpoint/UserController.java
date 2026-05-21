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
import cn.frank.ulp.common.entity.account.query.UserListQuery;
import cn.frank.ulp.common.enums.UserStatus;
import cn.frank.ulp.openapi.common.OpenApiResponse;
import cn.frank.ulp.openapi.pojo.result.UserListResult;
import cn.frank.ulp.openapi.pojo.result.UserResult;
import cn.frank.ulp.openapi.pojo.save.UserCreateParam;
import cn.frank.ulp.openapi.pojo.update.UserUpdateParam;
import cn.frank.ulp.openapi.service.UserService;
import cn.frank.ulp.support.demo.Preview;
import cn.frank.ulp.support.lock.Lock;
import cn.frank.ulp.support.repository.page.domain.Page;
import cn.frank.ulp.support.repository.page.domain.PageModel;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import static cn.frank.ulp.openapi.constant.OpenApiV1Constants.USER_PATH;

/**
 * 系统账户-用户
 *
 * @author Frank Zhang
 */
@Validated
@Tag(name = "用户管理")
@RestController
@AllArgsConstructor
@RequestMapping(value = USER_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    /**
     * 获取用户列表
     *
     * @param page {@link PageModel}
     * @return {@link UserListQuery}
     */
    @Operation(summary = "获取用户列表")
    @GetMapping(value = "/list")
    public OpenApiResponse<Page<UserListResult>> getUserList(PageModel page,
                                                             @Validated UserListQuery query) {
        return OpenApiResponse.success((userService.getUserList(page, query)));
    }

    /**
     * 获取用户
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Operation(summary = "获取用户信息")
    @GetMapping(value = "/{id}")
    public OpenApiResponse<UserResult> getUser(@PathVariable(value = "id") String id) {
        return OpenApiResponse.success(userService.getUser(id));
    }

    /**
     * 获取用户id
     *
     * @param externalId {@link String}
     * @param phoneNumber {@link String}
     * @param email {@link String}
     * @param username {@link String}
     * @return {@link String}
     */
    @Operation(summary = "获取用户id", description = "必须且只能有一个参数传入")
    @GetMapping(value = "/user_id")
    public OpenApiResponse<String> getUserIdByParams(@RequestParam(required = false) String externalId,
                                                     @RequestParam(required = false) String phoneNumber,
                                                     @RequestParam(required = false) String email,
                                                     @RequestParam(required = false) String username) {
        return OpenApiResponse
            .success(userService.getUserIdByParams(externalId, phoneNumber, email, username));
    }

    /**
     * 创建用户
     *
     * @param param {@link UserCreateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "创建用户")
    @Audit(type = EventType.CREATE_USER)
    @PostMapping(value = "/create")
    public OpenApiResponse<Void> createUser(@RequestBody @Validated UserCreateParam param) {
        userService.createUser(param);
        return OpenApiResponse.success();
    }

    /**
     * 更新用户
     *
     * @param param {@link UserCreateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "修改用户")
    @Audit(type = EventType.UPDATE_USER)
    @PutMapping(value = "/update")
    public OpenApiResponse<Void> updateUser(@RequestBody @Validated UserUpdateParam param) {
        userService.updateUser(param);
        return OpenApiResponse.success();
    }

    /**
     * 删除用户
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "删除用户")
    @Audit(type = EventType.DELETE_USER)
    @DeleteMapping(value = "/delete/{id}")
    public OpenApiResponse<Void> deleteUser(@PathVariable(value = "id") String id) {
        userService.deleteUser(id);
        return OpenApiResponse.success();
    }

    /**
     * 启用用户
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Operation(summary = "启用用户")
    @Audit(type = EventType.ENABLE_USER)
    @PutMapping(value = "/enable/{id}")
    public OpenApiResponse<Void> enableUser(@PathVariable(value = "id") String id) {
        userService.changeUserStatus(id, UserStatus.ENABLED);
        return OpenApiResponse.success();
    }

    /**
     * 禁用用户
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "禁用用户")
    @Audit(type = EventType.DISABLE_USER)
    @PutMapping(value = "/disable/{id}")
    public OpenApiResponse<Void> disableUser(@PathVariable(value = "id") String id) {
        userService.changeUserStatus(id, UserStatus.DISABLED);
        return OpenApiResponse.success();
    }

    /**
     * 用户服务类
     */
    private final UserService userService;
}
