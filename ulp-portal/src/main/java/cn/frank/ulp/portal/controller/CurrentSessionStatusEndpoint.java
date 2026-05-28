/*
 * ulp-portal - United Login Platform
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
package cn.frank.ulp.portal.controller;

import java.io.Serializable;

import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.annotation.JSONField;

import cn.frank.ulp.authentication.common.authentication.IdentityProviderNotBindAuthentication;
import cn.frank.ulp.portal.authentication.AuthenticationTrustResolverImpl;
import cn.frank.ulp.support.result.ApiRestResult;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.media.Schema;
import static com.alibaba.fastjson2.JSONWriter.Feature.WriteEnumsUsingName;

import static cn.frank.ulp.common.constant.SessionConstants.CURRENT_STATUS;

/**
 * 会话状态
 *
 * @author Frank Zhang
 */
@Slf4j
@RestController
@RequestMapping
public class CurrentSessionStatusEndpoint {
    private final AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();

    @GetMapping(CURRENT_STATUS)
    protected ApiRestResult<CurrentStatusResult> getCurrentStatus() {
        CurrentStatusResult.CurrentStatusResultBuilder builder = CurrentStatusResult.builder();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 是否认证
        builder.authenticated(authentication.isAuthenticated());
        if (authenticationTrustResolver.isAnonymous(authentication)) {
            builder.authenticated(false);
        }
        // IDP
        if (authentication instanceof IdentityProviderNotBindAuthentication) {
            builder.status(Status.require_bind_idp);
        }
        //其他信息
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
         * 状态
         */
        @JSONField(serializeFeatures = WriteEnumsUsingName)
        private Status  status;
    }

    public enum Status {

                        /**
                         * 需要绑定IDP
                         */
                        require_bind_idp
    }
}
