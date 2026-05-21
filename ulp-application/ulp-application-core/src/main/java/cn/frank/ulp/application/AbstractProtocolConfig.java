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
package cn.frank.ulp.application;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/13 21:32
 */
@Data
@SuperBuilder
public class AbstractProtocolConfig implements Serializable {

    @Serial
    private static final long serialVersionUID = 7006674647924828758L;

    /**
     * 应用ID
     */
    @NonNull
    private String            appId;

    /**
     * 应用名称
     */
    @NonNull
    private String            appName;

    /**
     * 客户端ID
     */
    @NonNull
    private String            clientId;

    /**
     * 客户端秘钥
     */
    @NonNull
    private String            clientSecret;

    /**
     * 应用编码
     */
    @NonNull
    private String            appCode;

    /**
     * 应用模版
     */
    @NonNull
    private String            appTemplate;
}
