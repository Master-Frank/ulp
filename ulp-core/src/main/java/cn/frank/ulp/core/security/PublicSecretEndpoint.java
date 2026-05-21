/*
 * ulp-core - United Login Platform
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
package cn.frank.ulp.core.security;

import java.io.Serial;
import java.io.Serializable;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.frank.ulp.support.constant.EiamConstants;
import cn.frank.ulp.support.context.ServletContextService;
import cn.frank.ulp.support.enums.SecretType;
import cn.frank.ulp.support.result.ApiRestResult;
import cn.frank.ulp.support.util.AesUtils;

import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 获取加密key
 *
 * @author Frank Zhang
 */
@RestController
@RequestMapping
public class PublicSecretEndpoint {

    public static final String PUBLIC_SECRET_PATH = EiamConstants.V1_API_PATH + "/public_secret";

    /**
     * 获取加密key
     *
     */
    @Validated
    @GetMapping(PUBLIC_SECRET_PATH)
    public ApiRestResult<PublicSecretResult> getPublicSecret(@RequestParam(value = "type", required = false) @NotNull(message = "秘钥类型不能为空") SecretType type) {
        //调用工具类生成秘钥
        String key = AesUtils.generateKey();
        //保存会话
        ServletContextService.getSession().setAttribute(type.getKey(), key);
        return ApiRestResult.ok(PublicSecretResult.builder().secret(key).build());
    }

    @Data
    @Builder
    public static class PublicSecretResult implements Serializable {

        @Serial
        private static final long serialVersionUID = 1868620270063512851L;

        /**
         * 秘钥
         */
        private String            secret;

    }
}
