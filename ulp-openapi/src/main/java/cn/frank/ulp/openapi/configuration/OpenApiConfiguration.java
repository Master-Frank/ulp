/*
 * ulp-openapi - United Login Platform
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
package cn.frank.ulp.openapi.configuration;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import cn.frank.ulp.UlpOpenApiApplication;
import cn.frank.ulp.support.util.VersionUtils;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import static cn.frank.ulp.openapi.constant.OpenApiV1Constants.OPEN_API_NAME;
import static cn.frank.ulp.openapi.constant.OpenApiV1Constants.OPEN_API_V1_PATH;

/**
 * OpenAPI 文档
 *
 * @author Frank Zhang
 */
@Configuration
public class OpenApiConfiguration {

    /**
     * 权限管理 RestAPI
     *
     * @return {@link GroupedOpenApi}
     */
    @Bean
    public GroupedOpenApi permissionRestApi() {
        return GroupedOpenApi.builder().group(OPEN_API_NAME).pathsToMatch(OPEN_API_V1_PATH + "/**")
            .build();
    }

    /**
     * API INFO
     *
     * @return {@link Info}
     */
    private Info info() {
        Contact contact = new Contact();
        contact.setEmail("");
        contact.setName("ULP");
        contact.setUrl("");
        return new Info()
            //title
            .title(environment.getProperty("spring.application.name"))
            //描述
            .description("REST API 文档")
            //服务条款网址
            .termsOfService("")
            //内容
            .contact(contact)
            //版本
            .version(VersionUtils.getVersion(UlpOpenApiApplication.class));
    }

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI().info(info());
    }

    private final Environment environment;

    public OpenApiConfiguration(Environment environment) {
        this.environment = environment;
    }

}
