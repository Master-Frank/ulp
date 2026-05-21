/*
 * ulp-core - United Login Platform
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
package cn.frank.ulp.core.configuration;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static cn.frank.ulp.support.constant.EiamConstants.CONTEXT_ENDPOINT;
import static cn.frank.ulp.support.constant.EiamConstants.CONTEXT_ENDPOINT_GROUP_NAME;

/**
 * ApiConfiguration
 *
 * @author Frank Zhang
 */
@Configuration
public class EiamApiConfiguration {

    /**
     * 系统上下文
     *
     * @return {@link GroupedOpenApi}
     */
    @Bean
    public GroupedOpenApi contextRestApi() {
        return GroupedOpenApi.builder().group(CONTEXT_ENDPOINT_GROUP_NAME)
            .pathsToMatch(CONTEXT_ENDPOINT + "/**").build();
    }

}
