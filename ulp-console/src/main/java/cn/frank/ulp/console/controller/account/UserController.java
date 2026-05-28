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
package cn.frank.ulp.console.controller.account;

import java.io.Serializable;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cn.frank.ulp.audit.annotation.Audit;
import cn.frank.ulp.audit.event.type.EventType;
import cn.frank.ulp.common.entity.account.query.UserListNotInGroupQuery;
import cn.frank.ulp.common.entity.account.query.UserListQuery;
import cn.frank.ulp.common.enums.CheckValidityType;
import cn.frank.ulp.common.enums.MessageNoticeChannel;
import cn.frank.ulp.common.enums.UserStatus;
import cn.frank.ulp.console.pojo.result.account.*;
import cn.frank.ulp.console.pojo.result.app.UserIdpBindListResult;
import cn.frank.ulp.console.pojo.save.account.UserCreateParam;
import cn.frank.ulp.console.pojo.update.account.ResetPasswordParam;
import cn.frank.ulp.console.pojo.update.account.UserUpdateParam;
import cn.frank.ulp.console.service.account.UserService;
import cn.frank.ulp.console.service.app.UserIdpBindService;
import cn.frank.ulp.core.security.otp.OtpContextHelp;
import cn.frank.ulp.support.demo.Preview;
import cn.frank.ulp.support.lock.Lock;
import cn.frank.ulp.support.repository.page.domain.Page;
import cn.frank.ulp.support.repository.page.domain.PageModel;
import cn.frank.ulp.support.result.ApiRestResult;

import lombok.AllArgsConstructor;
import lombok.Data;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import static cn.frank.ulp.common.constant.AccountConstants.USER_PATH;

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
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Page<UserListResult>> getUserList(PageModel page,
                                                           @Validated UserListQuery query) {
        return ApiRestResult.<Page<UserListResult>> builder()
            .result(userService.getUserList(page, query)).build();
    }

    /**
     * 获取用户列表（不在指定用户组）
     *
     * @param query {@link UserListNotInGroupQuery} 分组ID
     * @return {@link Boolean}
     */
    @Operation(summary = "获取用户列表（不在指定用户组）")
    @GetMapping(value = "/notin_group_list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Page<UserListResult>> getUserListNotInGroup(PageModel model,
                                                                     @Validated UserListNotInGroupQuery query) {
        return ApiRestResult.<Page<UserListResult>> builder()
            .result(userService.getUserListNotInGroup(model, query)).build();
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
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> createUser(@RequestBody @Validated UserCreateParam param) {
        return ApiRestResult.<Boolean> builder().result(userService.createUser(param)).build();
    }

    /**
     * 更改系统用户
     *
     * @param param {@link UserCreateParam}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "修改用户")
    @Audit(type = EventType.UPDATE_USER)
    @PutMapping(value = "/update")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> updateUser(@RequestBody @Validated UserUpdateParam param) {
        return ApiRestResult.<Boolean> builder().result(userService.updateUser(param)).build();
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
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> deleteUser(@PathVariable(value = "id") String id) {
        return ApiRestResult.<Boolean> builder().result(userService.deleteUser(id)).build();
    }

    /**
     * 批量删除用户
     *
     * @param ids {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "批量删除用户")
    @Audit(type = EventType.DELETE_USER)
    @DeleteMapping(value = "/batch_delete")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> batchDeleteUser(String[] ids) {
        return ApiRestResult.<Boolean> builder().result(userService.batchDeleteUser(ids)).build();
    }

    /**
     * 获取用户
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Operation(summary = "获取用户信息")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    @GetMapping(value = "/get/{id}")
    public ApiRestResult<UserResult> getUser(@PathVariable(value = "id") String id) {
        return ApiRestResult.<UserResult> builder().result(userService.getUser(id)).build();
    }

    /**
     * 批量获取用户信息
     *
     * @param ids {@link List}
     * @return {@link BatchOrganizationResult}
     */
    @Validated
    @Operation(summary = "批量获取用户信息")
    @GetMapping(value = "/batch_get")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<List<BatchUserResult>> batchGetUser(@RequestParam(value = "ids", required = false) @NotNull(message = "用户ID不能为空") List<String> ids) {
        List<BatchUserResult> result = userService.batchGetUser(ids.stream().toList());
        return ApiRestResult.<List<BatchUserResult>> builder().result(result).build();
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
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> enableUser(@PathVariable(value = "id") String id) {
        Boolean result = userService.changeUserStatus(id, UserStatus.ENABLED);
        return ApiRestResult.<Boolean> builder().result(result).build();
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
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> disableUser(@PathVariable(value = "id") String id) {
        Boolean result = userService.changeUserStatus(id, UserStatus.DISABLED);
        return ApiRestResult.<Boolean> builder().result(result).build();
    }

    /**
     * 重置用户密码
     *
     * @param param {@link ResetPasswordParam}
     * @return Boolean
     */
    @Lock
    @Preview
    @Operation(summary = "重置用户密码")
    @Audit(type = EventType.MODIFY_USER_PASSWORD)
    @PutMapping(value = "/reset_password")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> resetUserPassword(@Validated @RequestBody ResetPasswordParam param) {
        return ApiRestResult.<Boolean> builder().result(userService.resetUserPassword(param))
            .build();
    }

    /**
     * 解锁用户
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "解锁用户")
    @Audit(type = EventType.UNLOCK_USER)
    @PutMapping(value = "/unlock/{id}")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> unlockUser(@PathVariable(value = "id") String id) {
        Boolean result = userService.changeUserStatus(id, UserStatus.ENABLED);
        return ApiRestResult.<Boolean> builder().result(result).build();
    }

    /**
     * 参数有效性验证
     *
     * @return {@link Boolean}
     */
    @Operation(summary = "参数有效性验证")
    @GetMapping(value = "/param_check")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> userParamCheck(@Parameter(description = "验证类型") @NotNull(message = "验证类型不能为空") CheckValidityType type,
                                                 @Parameter(description = "值") @NotEmpty(message = "验证值不能为空") String value,
                                                 @Parameter(description = "ID") String id) {
        Boolean result = userService.userParamCheck(type, value, id);
        //返回
        return ApiRestResult.<Boolean> builder().result(result).build();
    }

    /**
     * 查询用户登录审计列表
     *
     * @param id     {@link Long}
     * @param pageModel {@link PageModel}
     * @return {@link ApiRestResult}
     */
    @Operation(description = "查询用户登录审计列表")
    @GetMapping(value = "/login_audit/list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Page<UserLoginAuditListResult>> getUserLoginAuditList(@Parameter(description = "ID") @RequestParam(value = "userId", required = false) @NotNull(message = "用户ID不能为空") String id,
                                                                               PageModel pageModel) {
        Page<UserLoginAuditListResult> list = userService.findUserLoginAuditList(id, pageModel);
        return ApiRestResult.ok(list);
    }

    /**
     * 查询用户身份提供商绑定
     *
     * @param userId {@link String}
     * @return {@link UserIdpBindListResult}
     */
    @Operation(summary = "查询用户身份提供商绑定")
    @GetMapping(value = "/idp_bind")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<List<UserIdpBindListResult>> getUserIdpBindList(@Validated @RequestParam(value = "userId", required = false) @Parameter(description = "ID") @NotBlank(message = "用户ID不能为空") String userId) {
        return ApiRestResult.<List<UserIdpBindListResult>> builder()
            .result(userIdpBindService.getUserIdpBindList(userId)).build();
    }

    /**
     * 删除用户身份提供商绑定
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "删除用户身份提供商绑定")
    @Audit(type = EventType.DELETE_USER_IDP_BIND)
    @DeleteMapping(value = "/unbind_idp")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> unbindUserIdp(@Validated @RequestParam(value = "id", required = false) @Parameter(description = "ID") @NotBlank(message = "绑定ID不能为空") String id) {
        return ApiRestResult.<Boolean> builder().result(userIdpBindService.unbindUserIdpBind(id))
            .build();
    }

    /**
     * 发送 OPT
     *
     * @return {@link ApiRestResult}
     */
    @PostMapping("/otp")
    @Lock(namespaces = "otp")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ResponseEntity<ApiRestResult<Boolean>> send(@Validated SendOptRequest request) {
        otpContextHelp.sendOtp(request.getTarget(), request.getType(), request.getChannel());
        return ResponseEntity.ok(ApiRestResult.ok());
    }

    /**
     * 发送 OTP 请求
     */
    @Data
    public static class SendOptRequest implements Serializable {
        /**
         * 发送场景
         */
        @Parameter(description = "type")
        @NotNull(message = "发送场景不能为空")
        private String               type;

        /**
         * 渠道
         */
        @Parameter(description = "channel")
        @NotNull(message = "消息渠道不能为空")
        private MessageNoticeChannel channel;

        /**
         * 目标
         */
        @Parameter(description = "target")
        private String               target;
    }

    /**
     * 用户服务类
     */
    private final UserService        userService;

    /**
     * OTP
     */
    private final OtpContextHelp     otpContextHelp;

    /**
     * UserIdpBindService
     */
    private final UserIdpBindService userIdpBindService;
}
