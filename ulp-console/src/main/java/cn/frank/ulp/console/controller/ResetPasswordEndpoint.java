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
package cn.frank.ulp.console.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cn.frank.ulp.audit.annotation.Audit;
import cn.frank.ulp.audit.event.type.EventType;
import cn.frank.ulp.common.entity.setting.AdministratorEntity;
import cn.frank.ulp.common.repository.setting.AdministratorRepository;
import cn.frank.ulp.console.pojo.request.ResetPasswordRequest;
import cn.frank.ulp.console.service.setting.AdministratorService;
import cn.frank.ulp.support.result.ApiRestResult;
import cn.frank.ulp.support.security.password.PasswordPolicyManager;
import cn.frank.ulp.support.security.password.authentication.NeedChangePasswordAuthenticationToken;
import cn.frank.ulp.support.security.password.exception.PasswordInvalidException;
import cn.frank.ulp.support.security.password.exception.PasswordValidatedFailException;
import cn.frank.ulp.support.web.decrypt.DecryptRequestBody;

import lombok.RequiredArgsConstructor;
import static cn.frank.ulp.support.security.constant.SecurityConstants.PASSWORD_INVALID_ERROR;
import static cn.frank.ulp.support.security.constant.SecurityConstants.PASSWORD_VALIDATED_FAIL_ERROR;
import static cn.frank.ulp.support.security.constant.SecurityConstants.RESET_PASSWORD_PATH;
import static cn.frank.ulp.support.security.constant.SecurityConstants.UNKNOWN_AUTHENTICATION_TYPE;

/**
 * 重置密码端点（POST /api/v1/reset_password）
 *
 * @author Frank Zhang
 */
@RestController
@RequiredArgsConstructor
public class ResetPasswordEndpoint {

    private final AdministratorService    administratorService;

    private final AdministratorRepository administratorRepository;

    private final PasswordPolicyManager   passwordPolicyManager;

    @Audit(type = EventType.MODIFY_USER_PASSWORD)
    @PostMapping(RESET_PASSWORD_PATH)
    public ApiRestResult<Boolean> resetPassword(@DecryptRequestBody @RequestBody @Validated ResetPasswordRequest param) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof NeedChangePasswordAuthenticationToken token)) {
            return ApiRestResult.<Boolean> builder().status(UNKNOWN_AUTHENTICATION_TYPE)
                .message("当前会话无需重置密码").build();
        }

        Object principal = token.getFirst() == null ? null : token.getFirst().getPrincipal();
        if (!(principal instanceof UserDetails userDetails)) {
            return ApiRestResult.<Boolean> builder().status(UNKNOWN_AUTHENTICATION_TYPE)
                .message("无法识别的认证主体").build();
        }

        if (!param.getPassword().equals(param.getConfirmPassword())) {
            return ApiRestResult.<Boolean> builder().status(PASSWORD_INVALID_ERROR)
                .message("两次输入的密码不一致").build();
        }

        AdministratorEntity admin = administratorRepository
            .findByUsername(userDetails.getUsername()).orElse(null);
        if (admin == null) {
            return ApiRestResult.<Boolean> builder().status(UNKNOWN_AUTHENTICATION_TYPE)
                .message("管理员不存在").build();
        }

        try {
            passwordPolicyManager.validate(admin.getId(), param.getPassword());
            administratorService.forceResetAdministratorPassword(admin.getUsername(),
                param.getPassword());
        } catch (PasswordValidatedFailException e) {
            return ApiRestResult.<Boolean> builder().status(PASSWORD_VALIDATED_FAIL_ERROR)
                .message(e.getMessage()).build();
        } catch (PasswordInvalidException e) {
            return ApiRestResult.<Boolean> builder().status(PASSWORD_INVALID_ERROR)
                .message(e.getMessage()).build();
        }

        SecurityContextHolder.clearContext();
        return ApiRestResult.ok(Boolean.TRUE);
    }
}
