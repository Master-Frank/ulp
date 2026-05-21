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
package cn.frank.ulp.core.security.util;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import cn.frank.ulp.common.entity.account.UserDetailEntity;
import cn.frank.ulp.common.entity.account.UserEntity;
import cn.frank.ulp.common.exception.UserNotFoundException;
import cn.frank.ulp.common.repository.account.UserDetailRepository;
import cn.frank.ulp.common.repository.account.UserRepository;
import cn.frank.ulp.support.context.ApplicationContextService;
import cn.frank.ulp.support.security.util.SecurityUtils;

/**
 * 用户工具
 *
 * @author Frank Zhang
 */
public final class UserUtils {
    private static final Logger logger = LoggerFactory.getLogger(UserUtils.class);

    public static UserEntity getUser() {
        return getUser(SecurityUtils.getCurrentUserId());
    }

    public static UserEntity getUser(String userId) {
        Optional<UserEntity> optional = ApplicationContextService.getBean(UserRepository.class)
            .findById(userId);
        if (optional.isPresent()) {
            return optional.get();
        }
        SecurityContextHolder.clearContext();
        logger.error("根据用户ID: [{}] 未查询到用户信息", userId);
        throw new UserNotFoundException();
    }

    public static UserDetailEntity getUserDetails() {
        return getUserDetails(SecurityUtils.getCurrentUserId());
    }

    public static UserDetailEntity getUserDetails(String userId) {
        Optional<UserDetailEntity> optional = ApplicationContextService
            .getBean(UserDetailRepository.class).findByUserId(userId);
        if (optional.isPresent()) {
            return optional.get();
        }
        SecurityContextHolder.clearContext();
        logger.error("根据用户ID: [{}] 未查询到用户信息", userId);
        throw new UserNotFoundException();
    }
}
