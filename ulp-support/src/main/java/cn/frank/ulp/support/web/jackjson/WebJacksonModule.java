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
package cn.frank.ulp.support.web.jackjson;

import cn.frank.ulp.support.web.useragent.UserAgent;

import tools.jackson.core.Version;
import tools.jackson.databind.JacksonModule;

/**
 * Web Jackson模块类
 * 用于配置Web相关的Jackson序列化和反序列化
 */
public class WebJacksonModule extends JacksonModule {

    @Override
    public void setupModule(SetupContext context) {
        context.setMixIn(UserAgent.class, UserAgentMixin.class);
    }

    @Override
    public Version version() {
        return new Version(1, 0, 0, null, null, null);
    }

    @Override
    public String getModuleName() {
        return WebJacksonModule.class.getName();
    }
}
