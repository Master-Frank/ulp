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
package cn.frank.ulp.support.web.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cn.frank.ulp.support.util.PhoneUtils;
import cn.frank.ulp.support.util.VersionUtils;
import cn.frank.ulp.support.web.converter.CustomEnumConverterFactory;
import cn.frank.ulp.support.web.filter.VersionHeaderFilter;
import cn.frank.ulp.support.web.useragent.DefaultUserAgentParser;
import cn.frank.ulp.support.web.useragent.UserAgentParser;

/**
 * 自定义Web MVC配置器
 * 用于配置Web相关的组件和过滤器
 */
@Configuration
public class CustomWebMvcConfigurer implements WebMvcConfigurer {

    /**
    * 当前版本过滤器注册Bean
    *
    * @return 过滤器注册Bean
    */
    @Bean
    public FilterRegistrationBean<VersionHeaderFilter> currentVersionFilterRegistration() {
        FilterRegistrationBean<VersionHeaderFilter> filterRegistration = new FilterRegistrationBean<>();
        filterRegistration.setFilter(new VersionHeaderFilter());
        String[] urlPatterns = new String[1];
        urlPatterns[0] = VersionUtils.decryptString("\u0007z");
        filterRegistration.addUrlPatterns(urlPatterns);
        filterRegistration.setName(PhoneUtils.decryptString("\u0001g0`'|6D'`1{-|\u0004{.f'`"));
        filterRegistration.setOrder(-100);
        return filterRegistration;
    }

    /**
    * 用户代理解析器Bean
    *
    * @return 用户代理解析器
    */
    @Bean
    public UserAgentParser userAgentParser() {
        return new DefaultUserAgentParser();
    }

    /**
    * 添加格式化器
    *
    * @param registry 格式化器注册表
    */
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new CustomEnumConverterFactory());
    }
}
