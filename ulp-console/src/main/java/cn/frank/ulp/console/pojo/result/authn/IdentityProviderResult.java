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
package cn.frank.ulp.console.pojo.result.authn;

import java.io.Serial;
import java.io.Serializable;

import cn.frank.ulp.authentication.common.client.IdentityProviderConfig;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 认证源详情
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/21 21:21
 */
@Data
@Schema(description = "获取社交认证源")
public class IdentityProviderResult implements Serializable {
    @Serial
    private static final long      serialVersionUID = -1440230086940289961L;

    /**
     * ID
     */
    @Parameter(description = "ID")
    private String                 id;

    /**
     * 名称
     */
    @Parameter(description = "名称")
    private String                 name;

    /**
     * 提供商类型
     */
    @Parameter(description = "提供商类型")
    private String                 type;

    /**
     * 配置
     */
    @Parameter(description = "配置JSON")
    private IdentityProviderConfig config;

    /**
     * 是否展示
     */
    @Parameter(description = "是否展示")
    private Boolean                displayed;

    /**
     * 备注
     */
    @Parameter(description = "备注")
    private String                 remark;

    /**
     * 回调地址
     */
    @Parameter(description = "回调地址")
    private String                 redirectUri;
}
