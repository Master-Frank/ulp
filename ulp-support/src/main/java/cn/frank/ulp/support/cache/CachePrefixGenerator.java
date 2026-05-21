/*
 * ulp-support - ULP support library (replaces the former eiam-support private jar).
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
package cn.frank.ulp.support.cache;

import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.lang.NonNull;

/**
 * 缓存前缀生成器
 * 用于生成Redis缓存键的前缀
 */
public class CachePrefixGenerator implements CacheKeyPrefix {

    /**
    * 前缀
    */
    private final String prefix;

    /**
    * 计算缓存键前缀
    *
    * @param cacheName 缓存名称
    * @return 完整的缓存键前缀
    */
    @NonNull
    public String compute(@NonNull String cacheName) {
        return this.prefix + ":" + cacheName + ":";
    }

    /**
    * 构造函数
    *
    * @param prefix 前缀
    */
    public CachePrefixGenerator(String prefix) {
        this.prefix = prefix;
    }
}