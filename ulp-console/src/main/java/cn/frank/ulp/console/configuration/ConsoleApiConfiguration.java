/*
 * ulp-console - United Login Platform
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
package cn.frank.ulp.console.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import cn.frank.ulp.EiamConsoleApplication;
import cn.frank.ulp.support.util.VersionUtils;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

/**
 * ApiConfiguration
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2019/5/16 21:28
 */
@Configuration
public class ConsoleApiConfiguration {

    /**
     * API INFO
     *
     * @return {@link Info}
     */
    private Info info() {
        Contact contact = new Contact();
        contact.setEmail("support@topiam.cn");
        contact.setName("TOPIAM");
        contact.setUrl("https://eiam.topiam.cn");
        return new Info()
            //title
            .title(environment.getProperty("spring.application.name"))
            //描述
            .description("TOPIAM 控制台 REST API 文档")
            //服务条款网址
            .termsOfService("https://eiam.topiam.cn")
            //内容
            .contact(contact)
            //版本
            .version(VersionUtils.getVersion(EiamConsoleApplication.class));
    }

    /**
     * 定义openapi
     *
     * @return {@link OpenAPI}
     */
    @Bean
    public OpenAPI openApi() {
        OpenAPI openApi = new OpenAPI();
        openApi.setComponents(new Components());
        openApi.setPaths(new Paths());
        openApi.setInfo(info());
        return openApi;
    }

    private final Environment environment;

    public ConsoleApiConfiguration(Environment environment) {
        this.environment = environment;
    }

}
