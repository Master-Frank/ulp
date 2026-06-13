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
package cn.frank.ulp.support.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;

// Minimal replacement for Spring Boot 3 org.springframework.boot.autoconfigure.cache.CacheProperties,
// which was deleted in Spring Boot 4. We only ever read spring.cache.redis.key-prefix in this codebase.
@ConfigurationProperties("spring.cache")
public class UlpCacheProperties {

    private final Redis redis = new Redis();

    public Redis getRedis() {
        return redis;
    }

    public static class Redis {
        private String keyPrefix;

        public String getKeyPrefix() {
            return keyPrefix;
        }

        public void setKeyPrefix(String keyPrefix) {
            this.keyPrefix = keyPrefix;
        }
    }
}
