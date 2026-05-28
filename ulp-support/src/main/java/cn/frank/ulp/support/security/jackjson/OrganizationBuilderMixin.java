/*
 * ulp-support - United Login Platform
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
package cn.frank.ulp.support.security.jackjson;

import java.util.ArrayList;
import java.util.List;

import cn.frank.ulp.support.security.userdetails.Organization;

/**
 * 组织构建器Mixin类
 * 用于扩展组织构建器功能
 */
public class OrganizationBuilderMixin {

    /**
    * 组织列表
    */
    private List<Organization> organizations = new ArrayList<>();

    /**
    * 添加组织
    *
    * @param organization 组织
    * @return 组织构建器Mixin
    */
    public OrganizationBuilderMixin addOrganization(Organization organization) {
        this.organizations.add(organization);
        return this;
    }

    /**
    * 获取组织列表
    *
    * @return 组织列表
    */
    public List<Organization> getOrganizations() {
        return this.organizations;
    }
}
