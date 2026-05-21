/*
 * ulp-core - United Login Platform
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
package cn.frank.ulp.core.security.access;

import java.util.Objects;

import cn.frank.ulp.support.security.userdetails.UserType;
import cn.frank.ulp.support.security.util.SecurityUtils;

/**
 * 安全访问表达式
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/5/25 22:11
 */
public class SecurityAccessExpression {

    /**
     * 判断是否有相应权限
     *
     * @param authority 权限
     * @return {boolean}
     */
    public boolean hasAuthority(Object authority) {
        if (Objects.isNull(authority)) {
            return false;
        }
        UserType userType = SecurityUtils.getCurrentUserType();
        return userType.equals(authority);
    }
}
