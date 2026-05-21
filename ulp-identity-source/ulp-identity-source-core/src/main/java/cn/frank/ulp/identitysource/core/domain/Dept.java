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

import java.io.Serializable;
import java.util.List;

import org.springframework.util.CollectionUtils;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 部门
 *
 * @author Frank Zhang
 */
@Data
@NoArgsConstructor
public class Dept implements Serializable {

    /**
     * 部门id
     */
    private String     deptId;

    /**
     * 部门父id
     */
    private String     parentId;

    /**
     * 部门名称
     */
    private String     name;

    /**
     * 部门排序
     */
    private Long       order;

    /**
     * 子节点
     */
    private List<Dept> children;

    public boolean isLeaf() {
        return CollectionUtils.isEmpty(children);
    }
}
