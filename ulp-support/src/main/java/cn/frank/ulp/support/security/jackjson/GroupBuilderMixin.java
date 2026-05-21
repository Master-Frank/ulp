/*
 * ulp-support - ULP support library (replaces the former eiam-support private jar).
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

import cn.frank.ulp.support.security.userdetails.Group;

/**
 * 组构建器Mixin类
 * 用于扩展组构建器功能
 */
public class GroupBuilderMixin {

    /**
    * 组列表
    */
    private List<Group> groups = new ArrayList<>();

    /**
    * 添加组
    *
    * @param group 组
    * @return 组构建器Mixin
    */
    public GroupBuilderMixin addGroup(Group group) {
        this.groups.add(group);
        return this;
    }

    /**
    * 获取组列表
    *
    * @return 组列表
    */
    public List<Group> getGroups() {
        return this.groups;
    }
}
