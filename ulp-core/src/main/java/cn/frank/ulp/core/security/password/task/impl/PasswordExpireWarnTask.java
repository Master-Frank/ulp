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

import java.time.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.scheduling.annotation.Scheduled;

import com.google.common.collect.Maps;

import cn.frank.ulp.common.entity.account.UserEntity;
import cn.frank.ulp.common.entity.setting.SettingEntity;
import cn.frank.ulp.common.enums.MailType;
import cn.frank.ulp.common.enums.SmsType;
import cn.frank.ulp.common.enums.UserStatus;
import cn.frank.ulp.common.repository.account.UserRepository;
import cn.frank.ulp.common.repository.setting.SettingRepository;
import cn.frank.ulp.core.message.mail.MailMsgEventPublish;
import cn.frank.ulp.core.message.sms.SmsMsgEventPublish;
import cn.frank.ulp.core.security.password.task.PasswordExpireTask;
import cn.frank.ulp.support.trace.Trace;

import lombok.RequiredArgsConstructor;
import static cn.frank.ulp.core.message.MsgVariable.EXPIRE_DAYS;
import static cn.frank.ulp.core.setting.PasswordPolicySettingConstants.*;

/**
 * 密码过期前提醒任务
 *
 * @author Frank Zhang
 */
@RequiredArgsConstructor
public class PasswordExpireWarnTask implements PasswordExpireTask {
    private final Logger logger = LoggerFactory.getLogger(PasswordExpireWarnTask.class);

    /**
     * 提醒 每天凌晨扫描，发送邮件及短信提醒
     */
    @Trace
    @Scheduled(cron = "0 0 0 * * ?")
    @Override
    public void execute() {
        logger.info("密码过期前提醒任务开始");
        int expireDays = getExpireDays();
        int expireWarnDays = getWarnExpireDays();
        //1、根据提醒时间，分页查询即将要过期的密码
        List<UserEntity> list = userRepository
            .findAll(Example.of(new UserEntity().setStatus(UserStatus.ENABLED)));
        //2、发送通知提醒
        for (UserEntity user : list) {
            // 获取到期日期
            LocalDate expiredDate = user.getLastUpdatePasswordTime().atOffset(ZoneOffset.MAX)
                .plusDays(expireDays).toLocalDate();
            // 到了提醒时间
            if (user.getLastUpdatePasswordTime().plusDays(expireWarnDays)
                .isBefore(LocalDateTime.now())) {
                //如果邮箱不为空，发送邮件通知
                if (StringUtils.isNotBlank(user.getEmail())) {
                    logger.info("邮件通知密码过期前提醒用户:{}", user.getUsername());
                    HashMap<String, Object> map = Maps.newHashMap();
                    //查询两个时间的间距
                    map.put(EXPIRE_DAYS, Duration.between(LocalDate.now(), expiredDate).toDays());
                    mailMsgEventPublish.publish(MailType.PASSWORD_SOON_EXPIRED_REMIND,
                        user.getEmail(), map);
                }
                // 短信通知
                if (StringUtils.isNotBlank(user.getPhone())) {
                    logger.info("短信通知密码过期前提醒用户:{}", user.getUsername());
                    LinkedHashMap<String, String> map = Maps.newLinkedHashMap();
                    // 查询两个时间的间距
                    map.put(EXPIRE_DAYS,
                        String.valueOf(Duration.between(LocalDate.now(), expiredDate).toDays()));
                    smsMsgEventPublish.publish(SmsType.PASSWORD_SOON_EXPIRED_REMIND,
                        user.getPhone(), map);
                }
            }

        }
        logger.info("密码过期前提醒任务结束");
    }

    /**
     * 获取密码过期前警告日
     *
     * @return {@link  Integer}
     */
    private Integer getWarnExpireDays() {
        SettingEntity beforeExpireDaysSetting = settingRepository
            .findByName(PASSWORD_POLICY_VALID_WARN_BEFORE_DAYS);
        String beforeExpireDays = Objects.isNull(beforeExpireDaysSetting)
            ? PASSWORD_POLICY_DEFAULT_SETTINGS.get(PASSWORD_POLICY_VALID_WARN_BEFORE_DAYS)
            : beforeExpireDaysSetting.getValue();
        return Integer.valueOf(beforeExpireDays);
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
    private final SettingRepository   settingRepository;

    /**
     * 用户Repository
     */
    private final UserRepository      userRepository;

    /**
     * 邮件消息发布
     */
    private final MailMsgEventPublish mailMsgEventPublish;

    /**
     * 短信消息发送
     */
    private final SmsMsgEventPublish  smsMsgEventPublish;
}
