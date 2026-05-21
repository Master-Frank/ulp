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
package cn.frank.ulp.console.pojo.result.app;

import cn.frank.ulp.common.enums.app.AppCertUsingType;

import lombok.AllArgsConstructor;
import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 获取应用证书列表结果
 *
 * @author Frank Zhang
 */
@Data
@AllArgsConstructor
@Schema(description = "获取应用证书列表响应")
public class AppCertListResult {
    /**
     * ID
     */
    @Parameter(description = "证书ID")
    private String           id;

    /**
     * 应用ID
     */
    @Parameter(description = "应用ID")
    private String           appId;

    /**
     * 签名算法
     */
    @Parameter(description = "签名算法")
    private String           signAlgo;

    /**
     * 私钥长度
     */
    @Parameter(description = "私钥长度")
    private Integer          keyLong;

    /**
     * 证书
     */
    @Parameter(description = "证书")
    private String           cert;

    /**
     * 使用类型
     */
    @Parameter(description = "使用类型")
    private AppCertUsingType usingType;
}
