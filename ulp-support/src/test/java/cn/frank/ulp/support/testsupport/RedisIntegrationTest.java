/*
 * ulp-support - ULP support library
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
package cn.frank.ulp.support.testsupport;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 涉及 Redis 写入的集成测试子基类。
 * <p>Redis 没有事务回滚概念，{@code @Transactional} 不会清理 Redis 状态，
 * 因此通过 {@code @AfterEach} 显式 flushDb 保证测试间隔离。</p>
 * <p>不碰 Redis 的测试直接继承 {@link AbstractIntegrationTest} 即可，
 * 不要继承本类（避免每个测试都付一次 flushDb 的代价）。</p>
 */
public abstract class RedisIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    protected StringRedisTemplate stringRedisTemplate;

    @AfterEach
    void flushDb() {
        RedisConnectionFactory factory = stringRedisTemplate.getConnectionFactory();
        if (factory != null) {
            factory.getConnection().serverCommands().flushDb();
        }
    }
}
