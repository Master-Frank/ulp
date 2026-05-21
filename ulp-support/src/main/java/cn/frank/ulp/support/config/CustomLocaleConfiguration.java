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
package cn.frank.ulp.support.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * 自定义本地化配置类
 * 用于配置国际化相关的组件
 */
public class CustomLocaleConfiguration implements WebMvcConfigurer {

    /**
    * 语言参数名称
    */
    public static final String LANGUAGE_NAME = "ulp_language";

    /**
    * 添加拦截器
    *
    * @param registry 拦截器注册表
    */
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("ulp_language");
        registry.addInterceptor(localeChangeInterceptor);
    }

    /**
    * 本地化解析器Bean
    *
    * @return 本地化解析器
    */
    @Bean
    public LocaleResolver localeResolver() {
        return new CookieLocaleResolver("ulp_language");
    }
}