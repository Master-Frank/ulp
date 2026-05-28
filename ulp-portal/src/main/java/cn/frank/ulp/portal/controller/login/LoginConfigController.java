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
package cn.frank.ulp.portal.controller.login;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.frank.ulp.portal.pojo.result.LoginConfigResult;
import cn.frank.ulp.portal.service.LoginConfigService;
import cn.frank.ulp.support.result.ApiRestResult;

import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.tags.Tag;
import static cn.frank.ulp.common.constant.AuthnConstants.LOGIN_CONFIG;

/**
 * 登录配置 Endpoint
 *
 * @author Frank Zhang
 */
@Tag(name = "登录配置")
@Slf4j
@RestController
@RequestMapping(value = LOGIN_CONFIG, method = RequestMethod.GET)
public class LoginConfigController {

    @GetMapping
    public ApiRestResult<LoginConfigResult> getLoginConfig() {
        return ApiRestResult.ok(loginConfigService.getLoginConfig());
    }

    /**
     * IdpService
     */
    private final LoginConfigService loginConfigService;

    public LoginConfigController(LoginConfigService loginConfigService) {
        this.loginConfigService = loginConfigService;
    }

}
