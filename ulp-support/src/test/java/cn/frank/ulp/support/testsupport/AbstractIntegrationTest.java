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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * 所有集成测试的共享基类：
 * <ul>
 *   <li>{@code @SpringBootTest(MOCK)} + {@code @AutoConfigureMockMvc} —— MockMvc 驱动 controller 层断言</li>
 *   <li>{@code @Transactional} —— 每个测试方法运行在事务里，结束时回滚，避免测试间 SQL 状态泄漏</li>
 *   <li>{@code @Testcontainers} —— 启用 Testcontainers JUnit Jupiter 集成</li>
 *   <li>{@code @Container static} —— 每个 JVM 启动一次 MySQL + Redis 容器</li>
 *   <li>{@code @DynamicPropertySource} —— 把容器 endpoint 注入 Spring 配置</li>
 * </ul>
 * <p>跨次运行的容器复用通过开发者本机 {@code ~/.testcontainers.properties} 显式开启
 * （{@code testcontainers.reuse.enable=true}）；默认每次 JVM 启停销毁。</p>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
@Testcontainers
public abstract class AbstractIntegrationTest {

    @Container
    protected static final MySQLContainer<?>   MYSQL = SharedContainers.mysql();

    @Container
    protected static final GenericContainer<?> REDIS = SharedContainers.redis();

    @DynamicPropertySource
    static void wireContainerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
        registry.add("spring.datasource.driver-class-name", MYSQL::getDriverClassName);
        registry.add("spring.data.redis.host", REDIS::getHost);
        registry.add("spring.data.redis.port", () -> REDIS.getMappedPort(6379));
    }

    @Autowired
    protected MockMvc mockMvc;
}
