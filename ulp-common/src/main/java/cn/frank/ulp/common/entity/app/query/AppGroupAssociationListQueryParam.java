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

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

/**
 * 查询应用列表入参
 *
 * @author TOPIAM
 * Created by support@topiam.cn on 2024/11/4 14:24
 */
@Data
public class AppGroupAssociationListQueryParam implements Serializable {
    @Serial
    private static final long serialVersionUID = -7110595216804896858L;
    /**
     * 组ID
     */
    private String            id;

    /**
     * 应用名称
     */
    private String            appName;
}
