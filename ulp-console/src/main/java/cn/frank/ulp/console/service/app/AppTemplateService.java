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
package cn.frank.ulp.console.service.app;

import java.util.List;

import cn.frank.ulp.common.enums.app.AppType;
import cn.frank.ulp.console.pojo.result.app.AppTemplateResult;

/**
 * 应用模板服务
 *
 * @author Frank Zhang
 */
public interface AppTemplateService {
    /**
     * List
     *
     * @param name {@link String}
     * @param type {@link AppType}
     * @return {@link List}
     */
    List<AppTemplateResult> getAppTemplateList(AppType type, String name);

    /**
     * List
     *
     * @param code {@link AppTemplateResult}
     * @return {@link Object}
     */
    Object getAppTemplateFormSchema(String code);
}
