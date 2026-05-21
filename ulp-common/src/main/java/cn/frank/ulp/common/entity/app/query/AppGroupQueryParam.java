/*
 * ulp-common - United Login Platform
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
package cn.frank.ulp.common.entity.app.query;

import java.io.Serializable;

import cn.frank.ulp.common.enums.app.AppGroupType;

import lombok.Data;

/**
 * 查询分组列表入参
 *
 * @author TOPIAM
 * Created by support@topiam.cn on 2024/11/4 14:23
 */
@Data
public class AppGroupQueryParam implements Serializable {

    /**
     * 分组名称
     */
    private String       name;

    /**
     * 应用名称
     */
    private String       appName;

    /**
     * 分组编码
     */
    private String       code;

    /**
     * 分组类型
     */
    private AppGroupType type;

}
