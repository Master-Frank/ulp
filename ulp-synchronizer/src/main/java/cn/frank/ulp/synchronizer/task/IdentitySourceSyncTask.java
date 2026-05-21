/*
 * ulp-synchronizer - United Login Platform
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
package cn.frank.ulp.synchronizer.task;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.StopWatch;

import cn.frank.ulp.common.enums.TriggerType;
import cn.frank.ulp.identitysource.core.IdentitySource;
import cn.frank.ulp.identitysource.core.IdentitySourceConfig;
import cn.frank.ulp.support.trace.TraceUtils;

import lombok.RequiredArgsConstructor;
import static cn.frank.ulp.support.constant.UlpConstants.CACHE_LOCK_KEY_PREFIX;
import static cn.frank.ulp.support.constant.UlpConstants.COLON;

/**
 * 身份源同步任务
 *
 * @author Frank Zhang
 */
@RequiredArgsConstructor
public class IdentitySourceSyncTask implements Runnable {
    private final AlternativeJdkIdGenerator idGenerator = new AlternativeJdkIdGenerator();
    private static final Logger             logger      = LoggerFactory
        .getLogger(IdentitySourceSyncTask.class);

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        logger.info("[任务触发]-同步身份源[{}]数据开始", name);
        String traceId = idGenerator.generateId().toString();
        TraceUtils.put(traceId);
        RLock lock = redissonClient.getLock(CACHE_LOCK_KEY_PREFIX + COLON + id);
        boolean tryLock = false;
        try {
            tryLock = lock.tryLock(1, TimeUnit.SECONDS);
            if (tryLock) {
                identitySource.sync(TriggerType.JOB);
            }
        } catch (InterruptedException ignored) {

        } finally {
            stopWatch.stop();
            if (tryLock && lock.isLocked()) {
                lock.unlock();
            }
            TraceUtils.remove();
            logger.info("[任务触发]-同步身份源[{}]数据结束, 执行时长: {} ms", name, stopWatch.getTotalTimeMillis());
        }
    }

    /**
     * ID
     */
    private final String                                         id;

    /**
     * 名称
     */
    private final String                                         name;

    /**
     * RedissonClient
     */
    private final RedissonClient                                 redissonClient;

    /**
     * 身份源
     */
    private final IdentitySource<? extends IdentitySourceConfig> identitySource;

}
