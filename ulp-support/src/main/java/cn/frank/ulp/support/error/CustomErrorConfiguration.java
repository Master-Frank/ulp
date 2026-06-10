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
package cn.frank.ulp.support.error;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.View;

/**
 * 自定义错误配置类
 * 用于配置错误处理相关的Bean
 */
@Configuration
@AutoConfigureBefore({ ErrorMvcAutoConfiguration.class })
public class CustomErrorConfiguration {

    /**
    * 默认错误视图Bean
    *
    * @return 错误视图
    */
    @Bean(name = { "error" })
    public View defaultErrorView() {
        return new CustomErrorView();
    }

    /**
    * 错误属性Bean
    *
    * @param errorAttributesHandler 错误属性处理器
    * @return 错误属性
    */
    @Bean
    public ErrorAttributes errorAttributes(@Autowired(required = false) ErrorAttributesHandler errorAttributesHandler) {
        return Objects.isNull(errorAttributesHandler) ? new CustomErrorAttributes()
            : new CustomErrorAttributes(errorAttributesHandler);
    }
}
