/*
 * ulp-identity-source-core - United Login Platform
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
package cn.frank.ulp.identitysource.core.event;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

import cn.frank.ulp.support.context.ApplicationContextService;

/**
 * 身份源配置事件工具
 *
 * @author Frank Zhang
 */
public class IdentitySourceEventUtils {
    public static final String IDENTITY_SOURCE_EVENT = "ulp-identity-source-event";

    /**
     * refresh
     * <p>
     * 利用redis订阅分发，完成集群参数刷新加载
     */
    public static void destroy(String id) {
        RedissonClient redisson = ApplicationContextService.getBean(RedissonClient.class);
        RTopic topic = redisson.getTopic(IDENTITY_SOURCE_EVENT);
        topic.publish(new IdentitySourceEvent(id, IdentitySourceEventType.DESTROY));
    }

    /**
     * refresh
     * <p>
     * 利用redis订阅分发，完成集群参数刷新加载
     */
    public static void register(String id) {
        RedissonClient redisson = ApplicationContextService.getBean(RedissonClient.class);
        RTopic topic = redisson.getTopic(IDENTITY_SOURCE_EVENT);
        topic.publish(new IdentitySourceEvent(id, IdentitySourceEventType.REGISTER));
    }

    /**
     * sync
     * <p>
     * 利用redis订阅分发，发送同步任务
     */
    public static void sync(String id) {
        RedissonClient redisson = ApplicationContextService.getBean(RedissonClient.class);
        RTopic topic = redisson.getTopic(IDENTITY_SOURCE_EVENT);
        topic.publish(new IdentitySourceEvent(id, IdentitySourceEventType.SYNC));
    }

}
