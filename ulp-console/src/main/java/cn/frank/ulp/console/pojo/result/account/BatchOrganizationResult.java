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
package cn.frank.ulp.console.pojo.result.account;

import lombok.Data;
import lombok.EqualsAndHashCode;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 批量获取组织结果
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/11 21:27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "批量获取组织响应")
public class BatchOrganizationResult extends OrganizationResult {

}
