/*
 * ulp-identity-source-feishu - United Login Platform
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
package cn.frank.ulp.identitysource.feishu.domain;

import java.io.Serializable;

import com.alibaba.fastjson2.annotation.JSONField;

import lombok.Data;

/**
 * 入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022-02-17 22:45
 */
@Data
public class BaseRequest implements Serializable {
    /**
     * 用户 ID 类型
     * 示例值："open_id"
     * 可选值有：
     * open_id：用户的 open id
     * union_id：用户的 union id
     * user_id：用户的 user id
     * 默认值：open_id
     * 当值为 user_id，字段权限要求：
     * 获取用户 user ID仅自建应用
     */
    @JSONField(name = "user_id_type")
    private String userIdType       = "user_id";
    /**
     * 此次调用中使用的部门ID的类型
     * 示例值："open_department_id"
     * 可选值有：
     * department_id：以自定义department_id来标识部门
     * open_department_id：以open_department_id来标识部门
     * 默认值：open_department_id
     */
    @JSONField(name = "departmentId_type")
    private String departmentIdType = "department_id";
}
