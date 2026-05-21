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
package cn.frank.ulp.audit.annotation;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.alibaba.fastjson2.JSON;

import lombok.AllArgsConstructor;

/**
 * AuditExpressionRoot
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/28 22:48
 */
@AllArgsConstructor
public class AuditExpressionRoot implements AuditExpressionOperations {

    /**
     * Gets the {@link Authentication} used for evaluating the expressions
     *
     * @return the {@link Authentication} for evaluating the expressions
     */
    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 转为JSON字符串
     *
     * @param object {@link Object}
     * @return {@link String}
     */
    @Override
    public String toJsonString(Object object) {
        return JSON.toJSONString(object);
    }

}
