/*
 * ulp-portal - United Login Platform
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
package cn.frank.ulp.portal.configuration;

import org.springframework.boot.autoconfigure.session.RedisSessionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;

import cn.frank.ulp.core.security.session.ClusterSessionRegistryImpl;

/**
 *
 * @author Frank Zhang
 */
@Configuration
public class PortalSessionConfiguration {

    @Bean
    public SessionRegistry sessionRegistry(FindByIndexNameSessionRepository<? extends Session> sessionRepository,
                                           RedisSessionProperties redisSessionProperties) {
        return new ClusterSessionRegistryImpl<>(sessionRepository,
            redisSessionProperties.getNamespace());
    }
}
