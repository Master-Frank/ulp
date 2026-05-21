/*
 * ulp-protocol-jwt - United Login Platform
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
package cn.frank.ulp.protocol.jwt;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.SpecVersion;

/**
 * Jwt openapi 定制器
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/11/25 13:53
 */
public class JwtOpenApiCustomizer implements GlobalOpenApiCustomizer, ApplicationContextAware {

    private static final Logger LOGGER       = LoggerFactory.getLogger(JwtOpenApiCustomizer.class);

    /**
     * Tag
     */
    private static final String ENDPOINT_TAG = "JWT API";

    /**
     * The Context.
     */
    private ApplicationContext  applicationContext;

    @Override
    public void customise(OpenAPI openApi) {
        FilterChainProxy filterChainProxy = applicationContext.getBean(
            AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME, FilterChainProxy.class);
        boolean openapi31 = SpecVersion.V31 == openApi.getSpecVersion();
        for (SecurityFilterChain filterChain : filterChainProxy.getFilterChains()) {

        }
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
