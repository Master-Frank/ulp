/*
 * ulp-identity-source-core - United Login Platform
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
package cn.frank.ulp.identitysource.core.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 用户模型
 *
 * @author Frank Zhang
 */
@Data
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 8567794739261358220L;
    /**
     * 用户ID
     */
    private String            userId;

    /**
     * 头像地址
     */
    private String            avatar;

    /**
     * 国际电话区号 86
     */
    private String            phoneAreaCode;

    /**
     * 手机号
     */
    private String            phone;

    /**
     * 邮箱
     */
    private String            email;

    /**
     * 公司邮箱
     */
    private String            orgEmail;

    /**
     * 所属部门ID列表
     */
    private List<String>      deptIdList;

    /**
     * 状态  true:启用 false:未启用
     */
    private Boolean           active;

    /**
     * 用户详情
     */
    private UserDetail        userDetail;

}
