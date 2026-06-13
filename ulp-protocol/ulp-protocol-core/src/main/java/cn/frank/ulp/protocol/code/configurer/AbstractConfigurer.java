/*
 * ulp-protocol-core - United Login Platform
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
package cn.frank.ulp.protocol.code.configurer;

import org.springframework.security.config.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import cn.frank.ulp.protocol.code.EndpointMatcher;

/**
 * Base configurer
 *
 * @author Frank Zhang
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public abstract class AbstractConfigurer {
    private final ObjectPostProcessor<Object> objectPostProcessor;

    public AbstractConfigurer(ObjectPostProcessor<Object> objectPostProcessor) {
        this.objectPostProcessor = objectPostProcessor;
    }

    /**
     * init
     *
     * @param httpSecurity {@link HttpSecurity}
     */
    public abstract void init(HttpSecurity httpSecurity);

    /**
     * configure
     *
     * @param httpSecurity {@link HttpSecurity}
     */
    public abstract void configure(HttpSecurity httpSecurity);

    /**
     * 获取请求匹配器
     *
     * @return {@link EndpointMatcher}
     */
    public abstract EndpointMatcher getEndpointMatcher();

    public final <T> T postProcess(T object) {
        return (T) this.objectPostProcessor.postProcess(object);
    }

    public final ObjectPostProcessor<Object> getObjectPostProcessor() {
        return this.objectPostProcessor;
    }

}
