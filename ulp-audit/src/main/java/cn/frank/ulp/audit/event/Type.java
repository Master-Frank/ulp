/*
 * ulp-audit - United Login Platform
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
package cn.frank.ulp.audit.event;

import java.util.List;

import cn.frank.ulp.support.security.userdetails.UserType;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 类型
 *
 * @author Frank Zhang
 */
@Data
@AllArgsConstructor
public class Type {
    /**
     * 编码
     */
    private String         code;
    /**
     * 名称
     */
    private String         name;
    /**
     * 资源
     */
    private Resource       resource;
    /**
     * 用户类型
     */
    private List<UserType> userTypes;

    @Override
    public String toString() {
        return String.format("[%s](%s) %s", name, code, resource);
    }

}
