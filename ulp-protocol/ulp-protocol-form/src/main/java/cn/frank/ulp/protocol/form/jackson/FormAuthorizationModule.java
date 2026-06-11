/*
 * ulp-protocol-form - United Login Platform
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
package cn.frank.ulp.protocol.form.jackson;

import tools.jackson.core.Version;
import tools.jackson.databind.module.SimpleModule;

import cn.frank.ulp.protocol.form.authentication.FormAuthenticationToken;

/**
 * FormAuthorizationModule
 *
 * Jackson 3 默认类型校验在 mapper builder 上配置，模块不再主动启用 default typing。
 *
 * @author Frank Zhang
 */
public class FormAuthorizationModule extends SimpleModule {

    public FormAuthorizationModule() {
        super(FormAuthorizationModule.class.getName(), new Version(1, 0, 0, null, null, null));
    }

    @Override
    public void setupModule(SetupContext context) {
        context.setMixIn(FormAuthenticationToken.class, FormAuthenticationTokenMixin.class);
    }

}
