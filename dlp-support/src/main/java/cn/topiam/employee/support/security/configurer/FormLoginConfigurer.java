/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.support.security.configurer;

import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import cn.topiam.employee.support.security.authentication.WebAuthenticationDetailsSource;

public class FormLoginConfigurer<H extends HttpSecurityBuilder<H>>
    extends AbstractAuthenticationFilterConfigurer<H, FormLoginConfigurer<H>, UsernamePasswordAuthenticationFilter> {

    public FormLoginConfigurer() {
        super(new UsernamePasswordAuthenticationFilter(), "/api/v1/login");
    }

    @Override
    public void init(H builder) throws Exception {
        builder.addFilterBefore(this.getAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        this.authenticationDetailsSource(getAuthenticationDetailsSource(builder));
        super.init(builder);
    }

    @Override
    public RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, "POST");
    }

    public static <B extends HttpSecurityBuilder<B>> WebAuthenticationDetailsSource getAuthenticationDetailsSource(B builder) {
        WebAuthenticationDetailsSource detailsSource = builder.getSharedObject(WebAuthenticationDetailsSource.class);
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
