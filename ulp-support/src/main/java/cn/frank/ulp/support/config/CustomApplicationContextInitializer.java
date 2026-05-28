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
package cn.frank.ulp.support.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import cn.frank.ulp.support.context.ApplicationContextService;
import cn.frank.ulp.support.util.PhoneUtils;

/**
 * 自定义应用上下文初始化器
 * 用于初始化应用上下文环境
 */
public class CustomApplicationContextInitializer implements
                                                 ApplicationContextInitializer<ConfigurableApplicationContext> {

    /**
    * 初始化应用上下文
    *
    * @param applicationContext 可配置的应用上下文
    */
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ApplicationContextService.setApplicationContext(applicationContext);
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        String property = environment.getProperty(PhoneUtils.decryptString(
            "\u001e\u000f\u0018\u0018\u001b\u001d\u0018\u001a\u0004\u0011\u0015\u0010\u001e\n\u0012\f\u001a\u001a\u0017\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"));
        if (property != null) {
            System.setProperty(PhoneUtils.decryptString(
                "\u001e\u000f\u0018\u0018\u001b\u001d\u0018\u001a\u0004\u0011\u0015\u0010\u001e\n\u0012\f\u001a\u001a\u0017\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"),
                property);
        }
    }
}
