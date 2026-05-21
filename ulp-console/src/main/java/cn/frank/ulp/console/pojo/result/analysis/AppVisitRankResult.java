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

import lombok.AllArgsConstructor;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 应用热点访问结果
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/11/22 23:16
 */
@Data
@AllArgsConstructor
@Schema(description = "应用访问频次")
public class AppVisitRankResult implements Serializable {

    /**
     * 应用名称
     */
    @Schema(description = "应用名称")
    private String name;

    /**
     * 访问次数
     */
    @Schema(description = "访问次数")
    private Long   count;
}
