/*
 * ulp-application-core - United Login Platform
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
package cn.frank.ulp.application.context;

import java.util.Map;

/**
 * 应用上下文
 *
 * @author Frank Zhang
 */
public interface ApplicationContext {
    /**
     * 获取应用ID
     *
     * @return {@link String}
     */
    String getAppId();

    /**
     * 获取客户端ID
     *
     * @return {@link String}
     */
    String getClientId();

    /**
     * 获取应用编码
     *
     * @return {@link String}
     */
    String getAppCode();

    /**
     * 获取应用模版
     *
     * @return {@link String}
     */
    String getAppTemplate();

    /**
     * 获取协议配置
     *
     * @return {@link Map}
     */
    Map<String, Object> getConfig();
}
