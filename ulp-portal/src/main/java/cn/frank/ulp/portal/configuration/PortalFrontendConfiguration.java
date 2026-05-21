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

import java.io.IOException;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

/**
 * 控制台前端配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on  2022/12/4 21:49
 */
@Configuration
public class PortalFrontendConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射静态资源根目录到  frontend
        registry.addResourceHandler("/**").addResourceLocations("classpath:/fe/")
            .resourceChain(true).addResolver(new PathResourceResolver() {
                // 后端匹配不到路由时转给前端
                @Override
                protected Resource getResource(@NotNull String resourcePath,
                                               @NotNull Resource location) throws IOException {
                    Resource resource = super.getResource(resourcePath, location);
                    if (resource == null) {
                        resource = super.getResource("index.html", location);
                    }
                    return resource;
                }
            });
    }
}
