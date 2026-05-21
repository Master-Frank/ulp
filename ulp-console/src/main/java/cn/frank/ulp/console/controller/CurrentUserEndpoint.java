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
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.frank.ulp.common.entity.setting.AdministratorEntity;
import cn.frank.ulp.common.exception.UserNotFoundException;
import cn.frank.ulp.common.repository.setting.AdministratorRepository;
import cn.frank.ulp.support.result.ApiRestResult;
import cn.frank.ulp.support.security.userdetails.UserDetails;
import cn.frank.ulp.support.security.util.SecurityUtils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.media.Schema;
import static cn.frank.ulp.common.constant.SessionConstants.CURRENT_USER;
import static cn.frank.ulp.support.util.AvatarUtils.bufferedImageToBase64;
import static cn.frank.ulp.support.util.AvatarUtils.generateAvatarImg;
import static cn.frank.ulp.support.util.DesensitizationUtils.emailEncrypt;
import static cn.frank.ulp.support.util.DesensitizationUtils.phoneEncrypt;

/**
 * 当前用户
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/12/23 21:49
 */
@Slf4j
@RestController
@RequestMapping
public class CurrentUserEndpoint {

    @GetMapping(CURRENT_USER)
    public ApiRestResult<CurrentUserResult> getCurrentUser() {
        //当前用户名
        UserDetails userDetails = SecurityUtils
            .getCurrentUser();
        Optional<AdministratorEntity> optional = administratorRepository
            .findById(userDetails.getId());
        if (optional.isEmpty()) {
            SecurityContextHolder.clearContext();
            throw new UserNotFoundException();
        }
        AdministratorEntity administrator = optional.get();
        CurrentUserResult result = new CurrentUserResult();
        //用户ID
        result.setAccountId(userDetails.getId());
        //用户名
        result.setUsername(administrator.getUsername());
        //姓名
        result.setFullName(administrator.getFullName());
        //昵称
        result.setNickName(administrator.getNickName());
        //头像
        if (StringUtils.isEmpty(administrator.getAvatar())) {
            result.setAvatar(bufferedImageToBase64(generateAvatarImg(administrator.getUsername())));
        } else {
            result.setAvatar(administrator.getAvatar());
        }
        //邮箱
        result.setEmail(emailEncrypt(administrator.getEmail()));
        //手机号
        result.setPhone(phoneEncrypt(administrator.getPhone()));
        return ApiRestResult.ok(result);
    }

    /**
     * 当前用户结果返回
     *
     * @author TopIAM
     * Created by support@topiam.cn on 2020/10/26 23:16
     */
    @Data
    @Schema(description = "当前用户响应")
    public static class CurrentUserResult implements Serializable {
        /**
         * 帐户ID
         */
        @Schema(description = "帐户ID")
        private String      accountId;

        /**
         * 用户名
         */
        @Schema(description = "用户名")
        private String      username;

        /**
         * 姓名
         */
        @Schema(description = "姓名")
        private String      fullName;

        /**
         * 昵称
         */
        @Schema(description = "昵称")
        private String      nickName;

        /**
         * 头像
         */
        @Schema(description = "头像")
        private String      avatar;

        /**
         * 邮箱
         */
        @Schema(description = "邮箱")
        private String      email;

        /**
         * 手机号
         */
        @Schema(description = "手机号")
        private String      phone;
        /**
         * 访问权限
         */
        private Set<String> access;
    }

    private final AdministratorRepository administratorRepository;

    public CurrentUserEndpoint(AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
    }
}
