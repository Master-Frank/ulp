/*
 * ulp-identity-source-core - United Login Platform
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
package cn.frank.ulp.identitysource.core;

import java.util.Map;

import cn.frank.ulp.common.enums.TriggerType;
import cn.frank.ulp.support.exception.UlpException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 身份源Provider
 *
 * @author Frank Zhang
 */
public interface IdentitySource<T extends IdentitySourceConfig> {
    /**
     * 获取身份源ID
     *
     * @return {@link String}
     */
    String getId();

    /**
     * 获取身份源名称
     *
     * @return {@link String}
     */
    String getName();

    /**
     * 获取身份源配置
     *
     * @return {@link String}
     */
    T getConfig();

    /**
     * 同步
     *
     * @param triggerType {@link TriggerType} 执行方式
     */
    void sync(TriggerType triggerType);

    /**
     * 回调
     *
     * @param request {@link HttpServletRequest}
     * @param body {@link String}
     * @return {@link Map}
     */
    default Object event(HttpServletRequest request, String body) {
        throw new UlpException("暂未实现");
    }

}
