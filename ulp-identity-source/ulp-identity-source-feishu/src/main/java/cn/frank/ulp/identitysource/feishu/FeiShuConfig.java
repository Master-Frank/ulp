/*
 * ulp-identity-source-feishu - United Login Platform
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
package cn.frank.ulp.identitysource.feishu;

import java.io.Serial;

import cn.frank.ulp.common.jackjson.encrypt.JsonPropertyEncrypt;
import cn.frank.ulp.identitysource.core.IdentitySourceConfig;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotEmpty;

/**
 * 飞书配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/2/28 23:17
 */
@Getter
@Setter
public class FeiShuConfig extends IdentitySourceConfig {

    @Serial
    private static final long serialVersionUID = -834514351515253275L;
    /**
     * 应用App key
     */
    @NotEmpty(message = "AppId不能为空")
    private String            appId;

    /**
     * 应用AppSecret
     */
    @JsonPropertyEncrypt
    @NotEmpty(message = "AppSecret不能为空")
    private String            appSecret;

    /**
     * encryptKey
     */
    @JsonPropertyEncrypt
    private String            encryptKey;

    /**
     * verificationToken
     */
    @JsonPropertyEncrypt
    private String            verificationToken;

    public FeiShuConfig() {
    }
}
