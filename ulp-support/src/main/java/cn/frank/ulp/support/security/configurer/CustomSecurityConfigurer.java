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
package cn.frank.ulp.support.security.configurer;

import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.context.SecurityContextHolderFilter;

import cn.frank.ulp.support.web.filter.CustomRedirectFilter;

/**
 * 自定义安全配置器
 * 用于向Spring Security配置中添加自定义过滤器
 *
 * @param <H> HttpSecurityBuilder的子类
 */
public final class CustomSecurityConfigurer<H extends HttpSecurityBuilder<H>> extends
                                           AbstractHttpConfigurer<CustomSecurityConfigurer<H>, H> {

    /**
    * 配置方法，在此处添加自定义过滤器
    *
    * @param builder HttpSecurity构建器
    */
    @Override
    public void configure(H builder) {
        CustomRedirectFilter filter = new CustomRedirectFilter();
        builder.addFilterBefore(filter, SecurityContextHolderFilter.class);
    }
}