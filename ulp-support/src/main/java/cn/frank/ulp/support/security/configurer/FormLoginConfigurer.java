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

import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import cn.frank.ulp.support.security.authentication.WebAuthenticationDetailsSource;

public class FormLoginConfigurer<H extends HttpSecurityBuilder<H>> extends
                                AbstractAuthenticationFilterConfigurer<H, FormLoginConfigurer<H>, UsernamePasswordAuthenticationFilter> {

    public FormLoginConfigurer() {
        super(new UsernamePasswordAuthenticationFilter(), "/api/v1/login");
    }

    @Override
    public void init(H builder) throws Exception {
        builder.addFilterBefore(this.getAuthenticationFilter(),
            UsernamePasswordAuthenticationFilter.class);
        this.authenticationDetailsSource(getAuthenticationDetailsSource(builder));
        super.init(builder);
    }

    @Override
    public RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, "POST");
    }

    public static <B extends HttpSecurityBuilder<B>> WebAuthenticationDetailsSource getAuthenticationDetailsSource(B builder) {
        WebAuthenticationDetailsSource detailsSource = builder
            .getSharedObject(WebAuthenticationDetailsSource.class);
        if (detailsSource == null) {
            detailsSource = getBean(builder, WebAuthenticationDetailsSource.class);
            builder.setSharedObject(WebAuthenticationDetailsSource.class, detailsSource);
        }
        return detailsSource;
    }

    public static <B extends HttpSecurityBuilder<B>, T> T getBean(B builder, Class<T> clazz) {
        return builder.getSharedObject(ApplicationContext.class).getBean(clazz);
    }
}
