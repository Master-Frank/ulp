/*
 * ulp-core - United Login Platform
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
package cn.frank.ulp.core.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.frank.ulp.core.security.access.SecurityAccessExpression;

/**
 *
 * @author Frank Zhang
 */
@Configuration
public class CoreSecurityConfiguration {

    /**
     * 安全访问表达式
     *
     * @return {@link SecurityAccessExpression}
     */
    @Bean(name = "sae")
    public SecurityAccessExpression securityAccessExpression() {
        return new SecurityAccessExpression();
    }
}
