/*
 * ulp-support - ULP support library (replaces the former eiam-support private jar).
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

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;

import cn.frank.ulp.support.web.useragent.UserAgent;

/**
 * Web Jackson模块类
 * 用于配置Web相关的Jackson序列化和反序列化
 */
public class WebJacksonModule extends Module {

    /**
    * 设置模块
    *
    * @param context 设置上下文
    */
    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(UserAgent.class, UserAgentMixin.class);
    }

    /**
    * 获取版本
    *
    * @return 版本
    */
    @Override
    public Version version() {
        return new Version(1, 0, 0, null, null, null);
    }

    /**
    * 获取模块名
    *
    * @return 模块名
    */
    @Override
    public String getModuleName() {
        return WebJacksonModule.class.getName();
    }
}
