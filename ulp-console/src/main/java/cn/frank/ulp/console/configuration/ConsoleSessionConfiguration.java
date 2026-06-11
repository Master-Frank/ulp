/*
 * ulp-console - United Login Platform
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
package cn.frank.ulp.console.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.FlushMode;
import org.springframework.session.Session;
import org.springframework.session.config.SessionRepositoryCustomizer;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisIndexedHttpSession;

import cn.frank.ulp.core.security.session.ClusterSessionRegistryImpl;

/**
 * Spring Boot 4 dropped {@code spring-boot-session} autoconfiguration, so the
 * indexed Redis session repository must be wired explicitly. The annotation
 * resolves {@code redisNamespace} from the {@code spring.session.redis.namespace}
 * property at bind time; the customizer preserves the immediate flush behaviour
 * that {@code spring.session.redis.flush-mode=immediate} previously provided.
 *
 * @author Frank Zhang
 */
@Configuration
@EnableRedisIndexedHttpSession(redisNamespace = "${spring.session.redis.namespace:spring:session}")
public class ConsoleSessionConfiguration {

    @Bean
    public SessionRegistry sessionRegistry(FindByIndexNameSessionRepository<? extends Session> sessionRepository,
                                           @Value("${spring.session.redis.namespace:spring:session}") String redisSessionNamespace) {
        return new ClusterSessionRegistryImpl<>(sessionRepository, redisSessionNamespace);
    }

    @Bean
    public SessionRepositoryCustomizer<RedisIndexedSessionRepository> immediateFlushSessionRepositoryCustomizer() {
        return repository -> repository.setFlushMode(FlushMode.IMMEDIATE);
    }
}
