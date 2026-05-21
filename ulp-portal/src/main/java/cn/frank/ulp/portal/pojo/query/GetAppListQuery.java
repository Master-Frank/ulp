/*
 * ulp-portal - United Login Platform
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
package cn.frank.ulp.portal.pojo.query;

import java.io.Serial;
import java.io.Serializable;

import org.springdoc.core.annotations.ParameterObject;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 查询应用列表
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/7/6 22:38
 */
@Data
@Schema(description = "查询应用列表")
@ParameterObject
public class GetAppListQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = -4981513177967939516L;

    /**
     * 应用名称
     */
    @Parameter(description = "应用名称")
    private String            name;

    /**
     * 应用分组ID
     */
    @Parameter(description = "应用分组ID")
    private String            groupId;

}
