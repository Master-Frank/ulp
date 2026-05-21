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

import java.io.Serializable;

import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.frank.ulp.console.authentication.AuthenticationTrustResolverImpl;
import cn.frank.ulp.support.result.ApiRestResult;
import cn.frank.ulp.support.security.password.authentication.NeedChangePasswordAuthenticationToken;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.media.Schema;
import static cn.frank.ulp.common.constant.SessionConstants.CURRENT_STATUS;
import static cn.frank.ulp.support.security.constant.SecurityConstants.REQUIRE_RESET_PASSWORD;

/**
 * 会话状态
 *
 * @author Frank Zhang
 */
@Slf4j
@RestController
@RequestMapping
public class CurrentSessionStatusEndpoint {
    private final AuthenticationTrustResolver delegate = new AuthenticationTrustResolverImpl();

    /**
     * 获取当前登录状态
     *
     * @return {@link CurrentStatusResult}
     */
    @GetMapping(value = CURRENT_STATUS)
    public ApiRestResult<CurrentStatusResult> getCurrentStatus() {
        CurrentStatusResult.CurrentStatusResultBuilder builder = CurrentStatusResult.builder();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        builder.authenticated(authentication.isAuthenticated());
        if (delegate.isAnonymous(authentication)) {
            builder.authenticated(false);
        }
        if (authentication instanceof NeedChangePasswordAuthenticationToken) {
            builder.status(REQUIRE_RESET_PASSWORD);
        }
        return ApiRestResult.ok(builder.build());
    }

    /**
     * 当前状态返回结果
     *
     * @author Frank Zhang
     */
    @Data
    @Builder
    @Schema(description = "当前状态响应")
    public static class CurrentStatusResult implements Serializable {
        /**
         * 是否认证
         */
        private Boolean authenticated;

        /**
         * status
         */
        private String  status;
    }
}
