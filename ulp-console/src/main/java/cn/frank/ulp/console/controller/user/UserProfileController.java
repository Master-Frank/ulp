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
package cn.frank.ulp.console.controller.user;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cn.frank.ulp.audit.annotation.Audit;
import cn.frank.ulp.audit.event.type.EventType;
import cn.frank.ulp.console.pojo.update.user.*;
import cn.frank.ulp.console.service.user.UserProfileService;
import cn.frank.ulp.support.result.ApiRestResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import static cn.frank.ulp.common.constant.UserConstants.*;
import static cn.frank.ulp.support.constant.EiamConstants.V1_API_PATH;

/**
 * 账户管理
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/12 21:39
 */
@RestController
@RequestMapping(value = V1_API_PATH + "/user/profile")
public class UserProfileController {

    /**
     * 修改账户信息
     *
     * @return {@link  ApiRestResult}
     */
    @Audit(type = EventType.MODIFY_ACCOUNT_INFO_PORTAL)
    @Operation(summary = "修改账户信息")
    @PutMapping("/change_info")
    public ApiRestResult<Boolean> changeInfo(@RequestBody @Validated UpdateUserInfoRequest param) {
        Boolean result = userProfileService.changeInfo(param);
        return ApiRestResult.ok(result);
    }

    /**
     * 修改密码
     *
     * @return {@link  ApiRestResult}
     */
    @Audit(type = EventType.MODIFY_USER_PASSWORD_PORTAL)
    @Operation(summary = "修改账户密码")
    @PutMapping("/change_password")
    public ApiRestResult<Boolean> changePassword(@RequestBody @Validated ChangePasswordRequest param) {
        return ApiRestResult.ok(userProfileService.changePassword(param));
    }

    /**
     * 准备修改手机
     *
     * @return {@link  ApiRestResult}
     */
    @Audit(type = EventType.PREPARE_MODIFY_PHONE)
    @Operation(summary = "准备修改手机")
    @PostMapping("/prepare_change_phone")
    public ApiRestResult<Boolean> prepareChangePhone(@RequestBody @Validated PrepareChangePhoneRequest param) {
        return ApiRestResult.ok(userProfileService.prepareChangePhone(param));
    }

    /**
     * 修改手机
     *
     * @return {@link  ApiRestResult}
     */
    @Audit(type = EventType.MODIFY_USER_PHONE_PORTAL)
    @Operation(summary = "修改手机")
    @PutMapping("/change_phone")
    public ApiRestResult<Boolean> changePhone(@RequestBody @Validated ChangePhoneRequest param) {
        return ApiRestResult.ok(userProfileService.changePhone(param));
    }

    /**
     * 准备修改邮箱
     *
     * @return {@link  ApiRestResult}
     */
    @Audit(type = EventType.PREPARE_MODIFY_EMAIL)
    @Operation(summary = "准备修改邮箱")
    @PostMapping("/prepare_change_email")
    public ApiRestResult<Boolean> prepareChangeEmail(@RequestBody @Validated PrepareChangeEmailRequest param) {
        return ApiRestResult.ok(userProfileService.prepareChangeEmail(param));
    }

    /**
     * 修改邮箱
     *
     * @return {@link  ApiRestResult}
     */
    @Audit(type = EventType.MODIFY_USER_EMAIL_PORTAL)
    @Operation(summary = "修改邮箱")
    @PutMapping("/change_email")
    public ApiRestResult<Boolean> changeEmail(@RequestBody @Validated ChangeEmailRequest param) {
        return ApiRestResult.ok(userProfileService.changeEmail(param));
    }

    /**
     * 忘记密码发送验证码
     *
     * @return {@link  ApiRestResult}
     */
    @Operation(summary = "忘记密码发送验证码")
    @GetMapping(FORGET_PASSWORD_CODE)
    public ApiRestResult<Boolean> forgetPasswordCode(@Parameter(description = "验证码接收者（邮箱/手机号）") @RequestParam String recipient) {
        return ApiRestResult.ok(userProfileService.forgetPasswordCode(recipient));
    }

    /**
     * 忘记密码预认证
     *
     * @return {@link  ApiRestResult}
     */
    @Operation(summary = "忘记密码预认证")
    @PostMapping(PREPARE_FORGET_PASSWORD)
    public ApiRestResult<Boolean> prepareForgetPassword(@RequestBody @Validated PrepareForgetPasswordRequest param) {
        return ApiRestResult
            .ok(userProfileService.prepareForgetPassword(param.getRecipient(), param.getCode()));
    }

    /**
     * 忘记密码
     *
     * @return {@link  ApiRestResult}
     */
    @Operation(summary = "忘记密码")
    @PutMapping(FORGET_PASSWORD)
    public ApiRestResult<Boolean> forgetPassword(@RequestBody @Validated ForgetPasswordRequest forgetPasswordRequest) {
        return ApiRestResult.ok(userProfileService.forgetPassword(forgetPasswordRequest));
    }

    /**
     * 用户资料service
     */
    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }
}
