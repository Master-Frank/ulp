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

import cn.frank.ulp.common.enums.app.AppPolicySubjectType;

import lombok.Data;

/**
 * 应用授权策略查询参数
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/9/27 21:29
 */
@Data
public class AppAccessPolicyQueryParam {

    /**
     * 应用id
     */
    private String               appId;

    /**
     * 授权主体
     */
    private String               subjectName;

    /**
     * 授权主体ID
     */
    private String               subjectId;

    /**
     * 主体类型（用户、分组、组织机构）
     */
    private AppPolicySubjectType subjectType;

    /**
     * 应用名称
     */
    private String               appName;
}
