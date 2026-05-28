/*
 * ulp-identity-source-dingtalk - United Login Platform
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
package cn.frank.ulp.identitysource.dingtalk;

import java.io.Serial;

import cn.frank.ulp.common.jackjson.encrypt.JsonPropertyEncrypt;
import cn.frank.ulp.identitysource.core.IdentitySourceConfig;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotEmpty;

/**
 * 钉钉配置
 *
 * @author Frank Zhang
 */
@Getter
@Setter
public class DingTalkConfig extends IdentitySourceConfig {

    @Serial
    private static final long serialVersionUID = 8537679521873685897L;
    /**
     * 企业 ID
     */
    private String            corpId;
    /**
     * 应用App key
     */
    @NotEmpty(message = "AppKey不能为空")
    private String            appKey;

    /**
     * 应用AppSecret
     */
    @JsonPropertyEncrypt
    @NotEmpty(message = "AppSecret不能为空")
    private String            appSecret;

    /**
     * 加密 aes key。实时同步，此参数必填。
     */
    @JsonPropertyEncrypt
    private String            aesKey;

    /**
     * 签名token
     */
    @JsonPropertyEncrypt
    private String            token;

    public DingTalkConfig() {
    }
}
