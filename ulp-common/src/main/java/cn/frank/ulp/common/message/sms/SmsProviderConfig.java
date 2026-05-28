/*
 * ulp-common - United Login Platform
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
package cn.frank.ulp.common.message.sms;

import java.io.Serial;
import java.io.Serializable;

import cn.frank.ulp.common.message.enums.SmsProvider;

import jakarta.validation.constraints.NotNull;

/**
 * 验证码提供商配置
 *
 * @author Frank Zhang
 */
public class SmsProviderConfig implements Serializable {

    @Serial
    private static final long serialVersionUID = 5611656522133230183L;

    /**
     * 平台
     */
    @NotNull(message = "平台类型不能为空")
    private SmsProvider       provider;

    public SmsProvider getProvider() {
        return provider;
    }

    public void setProvider(SmsProvider provider) {
        this.provider = provider;
    }

    public SmsProviderConfig() {
    }
}
