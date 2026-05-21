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
package cn.frank.ulp.portal.pojo.result;

import java.io.Serial;
import java.io.Serializable;

import cn.frank.ulp.common.enums.app.AppProtocol;
import cn.frank.ulp.common.enums.app.AppType;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 获取应用列表
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/6/8 21:58
 */
@Data
@Schema(description = "获取应用列表")
public class GetAppListResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 1263170640092199401L;
    /**
     * 应用ID
     */
    @Parameter(description = "ID")
    private String            id;

    /**
     * 应用code
     */
    @Parameter(description = "CODE")
    private String            code;

    /**
     * 应用类型
     */
    @Parameter(description = "应用类型")
    private AppType           type;

    /**
     * 应用协议
     */
    @Parameter(description = "应用协议")
    private AppProtocol       protocol;

    /**
     * 应用模板
     */
    @Parameter(description = "应用模板")
    private String            template;
    /**
     * 应用名称
     */
    @Parameter(description = "应用名称")
    private String            name;

    /**
     * ICON
     */
    @Parameter(description = "ICON")
    private String            icon;

    /**
     * SSO 发起URL
     */
    @Parameter(description = "SSO 发起URL")
    private String            initLoginUrl;

    /**
     * 应用描述
     */
    @Parameter(description = "应用描述")
    private String            description;
}
