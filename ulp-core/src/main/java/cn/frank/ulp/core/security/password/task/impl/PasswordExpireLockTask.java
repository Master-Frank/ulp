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
package cn.frank.ulp.core.security.password.task.impl;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import com.google.common.collect.Lists;

import cn.frank.ulp.common.entity.account.UserEntity;
import cn.frank.ulp.common.entity.setting.SettingEntity;
import cn.frank.ulp.common.enums.UserStatus;
import cn.frank.ulp.common.repository.account.UserRepository;
import cn.frank.ulp.common.repository.setting.SettingRepository;
import cn.frank.ulp.core.security.password.task.PasswordExpireTask;
import cn.frank.ulp.support.trace.Trace;

import lombok.RequiredArgsConstructor;
import static cn.frank.ulp.core.setting.PasswordPolicySettingConstants.PASSWORD_POLICY_DEFAULT_SETTINGS;
import static cn.frank.ulp.core.setting.PasswordPolicySettingConstants.PASSWORD_POLICY_VALID_DAYS;

/**
 * 密码过期锁定任务
 *
 * @author Frank Zhang
 */
@RequiredArgsConstructor
public class PasswordExpireLockTask implements PasswordExpireTask {

    private final Logger logger = LoggerFactory.getLogger(PasswordExpireLockTask.class);

    /**
     * 锁定密码过期用户
     */
    @Trace
    @Scheduled(cron = "0 0 0 * * ?")
    @Override
    public void execute() {
        logger.info("密码过期锁定用户任务开始");
        // 查询非密码过期锁定和过期锁定用户信息
        List<UserEntity> list = userRepository.findAllByStatusIn(
            Lists.newArrayList(UserStatus.PASSWORD_EXPIRED_LOCKED, UserStatus.EXPIRED_LOCKED));
        Iterator<UserEntity> iterator = list.iterator();
        while (!list.isEmpty()) {
            UserEntity user = iterator.next();
            //获取到期日期
            int expireDays = getExpireDays();
            if (user.getLastUpdatePasswordTime().plusDays(expireDays)
                .isBefore(LocalDateTime.now())) {
                user.setStatus(UserStatus.PASSWORD_EXPIRED_LOCKED);
                userRepository.save(user);
                logger.info("锁定密码过期用户:{}", user.getUsername());
                iterator.remove();
            }
            iterator = list.iterator();
        }
        logger.info("密码过期锁定用户任务结束");
    }

    /**
     * 获取密码过期日
     *
     * @return {@link  Integer}
     */
    private Integer getExpireDays() {
        SettingEntity expireDaysSetting = settingRepository.findByName(PASSWORD_POLICY_VALID_DAYS);
        String expireDays = Objects.isNull(expireDaysSetting)
            ? PASSWORD_POLICY_DEFAULT_SETTINGS.get(PASSWORD_POLICY_VALID_DAYS)
            : expireDaysSetting.getValue();
        return Integer.parseInt(expireDays);
    }

    /**
     * 设置
     */
    private final SettingRepository settingRepository;

    /**
     * UserRepository
     */
    private final UserRepository    userRepository;

}
