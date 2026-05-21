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
package cn.frank.ulp.console.pojo.result.analysis;

import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 概述总计结果
 *
 * @author Frank Zhang
 */
@Data
@Schema(description = "概述总计响应")
public class OverviewResult implements Serializable {

    /**
     * 今日认证量
     */
    @Schema(description = "今日认证量")
    private String todayAuthnCount;

    /**
     * 认证源总数
     */
    @Schema(description = "认证源总数")
    private String idpCount;

    /**
     * 用户数量
     */
    @Schema(description = "用户数量")
    private String userCount;

    /**
     * 应用数量
     */
    @Schema(description = "应用数量")
    private String appCount;
}
