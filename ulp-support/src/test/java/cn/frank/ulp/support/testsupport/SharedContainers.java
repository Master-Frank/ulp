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

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * 集中管理集成测试用的容器 image tag 与默认配置。
 * <p>版本升级是一处改动；复用通过开发者本机 {@code ~/.testcontainers.properties} 显式开启。</p>
 */
public final class SharedContainers {

    private static final DockerImageName MYSQL_IMAGE = DockerImageName.parse("mysql:8.0.36");
    private static final DockerImageName REDIS_IMAGE = DockerImageName.parse("redis:7-alpine");


    private SharedContainers() {
    }

    public static MySQLContainer<?> mysql() {
        return new MySQLContainer<>(MYSQL_IMAGE).withDatabaseName("ulp").withUsername("ulp")
            .withPassword("ulp").withUrlParam("serverTimezone", "GMT%2B8")
            .withUrlParam("useUnicode", "true").withUrlParam("characterEncoding", "UTF-8")
            .withUrlParam("allowPublicKeyRetrieval", "true").withUrlParam("useSSL", "false")
            .withReuse(true);
    }

    public static GenericContainer<?> redis() {
        return new GenericContainer<>(REDIS_IMAGE).withExposedPorts(6379).withReuse(true);
    }
}
