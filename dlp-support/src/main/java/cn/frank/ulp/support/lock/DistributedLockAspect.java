/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.support.lock;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Aspect
@Component
public class DistributedLockAspect {

    private static final Logger log = LoggerFactory.getLogger(DistributedLockAspect.class);

    private final RedissonClient redissonClient;

    public DistributedLockAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Around("@annotation(lock)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, Lock lock) throws Throwable {
        String key = this.parseKey(lock.key(), proceedingJoinPoint);
        RLock rlock = this.redissonClient.getLock(key);
        boolean locked = false;

        try {
            locked = rlock.tryLock(0L, lock.time(), TimeUnit.SECONDS);
            if (!locked) {
                log.debug("Failed to acquire distributed lock: {}", key);
                throw new TopIamLockException("Failed to acquire distributed lock: " + key);
            }
            return proceedingJoinPoint.proceed();
        } finally {
            if (locked) {
                rlock.unlock();
            }
        }
    }

    private String parseKey(String key, ProceedingJoinPoint proceedingJoinPoint) {
        if (!StringUtils.hasText(key)) {
            MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
            Method method = signature.getMethod();
            return method.getDeclaringClass().getSimpleName() + ":" + method.getName();
        }
        return key;
    }
}
