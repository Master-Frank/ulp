/*
 * eiam-core - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.core.security.task;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import cn.frank.ulp.common.entity.account.UserEntity;
import cn.frank.ulp.common.enums.UserStatus;
import cn.frank.ulp.common.repository.account.UserRepository;
import cn.frank.ulp.support.lock.Lock;
import cn.frank.ulp.support.trace.Trace;

import lombok.RequiredArgsConstructor;

/**
 * 用户过期锁定任务
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/4/17 21:22
 */
@RequiredArgsConstructor
public class UserExpireLockTask {

    private final Logger logger = LoggerFactory.getLogger(UserExpireLockTask.class);

    /**
     * 每分钟扫描已过期的用户
     */
    @Trace
    @Lock(throwException = false)
    @Scheduled(cron = "0 0 0 * * ?")
    public void execute() {
        logger.info("扫描已过期待锁定用户任务开始");
        //1、根据提醒时间，分页查询即将要过期的密码
        List<UserEntity> list = userRepository.findExpireUser();
        Iterator<UserEntity> iterator = list.iterator();
        logger.info("已过期待锁定用户数量为:{}个", list.size());
        while (!list.isEmpty()) {
            UserEntity entity = iterator.next();
            entity.setStatus(UserStatus.EXPIRED_LOCKED);
            userRepository.save(entity);
            logger.info("锁定已过期用户：{}", entity.getUsername());
            iterator.remove();
            iterator = list.iterator();
        }
        logger.info("扫描已过期待锁定任务结束");
    }

    /**
     * UserRepository
     */
    private final UserRepository userRepository;

}
