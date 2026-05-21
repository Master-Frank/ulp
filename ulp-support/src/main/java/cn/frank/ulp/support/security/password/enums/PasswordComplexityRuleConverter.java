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
package cn.frank.ulp.support.security.password.enums;

import java.util.Objects;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * 密码复杂度规则转换器
 * 用于JPA实体与数据库之间的类型转换
 */
@Converter(autoApply = true)
public class PasswordComplexityRuleConverter implements
                                             AttributeConverter<PasswordComplexityRule, String> {

    /**
    * 将实体属性转换为数据库列值
    *
    * @param rule 密码复杂度规则
    * @return 数据库列值
    */
    @Override
    public String convertToDatabaseColumn(PasswordComplexityRule rule) {
        return Objects.isNull(rule) ? null : rule.getCode();
    }

    /**
    * 将数据库列值转换为实体属性
    *
    * @param code 数据库列值
    * @return 密码复杂度规则
    */
    @Override
    public PasswordComplexityRule convertToEntityAttribute(String code) {
        return PasswordComplexityRule.getType(code);
    }
}
