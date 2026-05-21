/*
 * ulp-application-form - United Login Platform
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
package cn.frank.ulp.application.form;

import cn.frank.ulp.application.ApplicationService;
import cn.frank.ulp.application.form.model.FormProtocolConfig;

/**
 * 应用接口
 *
 * @author Frank Zhang
 */
public interface FormApplicationService extends ApplicationService {

    /**
     * 获取协议配置
     *
     * @param appCode {@link String}
     * @return {@link FormProtocolConfig}
     */
    FormProtocolConfig getProtocolConfig(String appCode);
}
