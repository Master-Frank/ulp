/*
 * ulp-support - United Login Platform
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
package cn.frank.ulp.support.security.configurer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;

import cn.frank.ulp.support.security.crypto.password.PasswordEncoderFactories;

/**
 * 自定义安全Bean配置类
 * 用于配置安全相关的Bean
 */
public class CustomSecurityBeansConfiguration {

    /**
    * 日志记录器
    */
    private final Logger logger = LoggerFactory.getLogger(CustomSecurityBeansConfiguration.class);

    /**
    * 密码编码器Bean
    *
    * @return 密码编码器
    */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return (new PasswordEncoderFactories()).createDelegatingPasswordEncoder();
    }

    /**
    * 安全上下文持有者策略Bean
    *
    * @return 安全上下文持有者策略
    */
    @Bean
    public SecurityContextHolderStrategy securityContextHolderStrategy() {
        return SecurityContextHolder.getContextHolderStrategy();
    }
}
