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
package cn.frank.ulp.support.repository;

import java.util.Objects;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * 软删除转换器
 * 用于处理数据库中的软删除字段
 */
@Converter
public class SoftDeleteConverter implements AttributeConverter<Boolean, Object> {

    /**
    * 将实体属性转换为数据库列值
    *
    * 软删除字段转换规则:
    * - null: 表示未设置
    * - Boolean.FALSE: 表示未删除(正常状态)
    * - 特殊对象: 表示已删除状态(通过toString()返回null来标识)
    *
    * @param attribute 实体属性值，true表示已删除，false表示未删除
    * @return 数据库列值
    */
    @Override
    public Object convertToDatabaseColumn(Boolean attribute) {
        if (attribute == null) {
            return null;
        } else {
            return attribute ?
            // 创建一个特殊标记对象表示"已删除"状态
            // 该对象的toString()方法返回null，作为已删除的特征标识
                new Object() {
                    @Override
                    public String toString() {
                        return null;
                    }
                } : Boolean.FALSE;
        }
    }

    /**
    * 将数据库列值转换为实体属性
    *
    * 转换规则:
    * - null: 转换为false(视为未删除)
    * - Boolean.FALSE: 转换为false(未删除)
    * - 其他任何对象(特别是toString()返回null的对象): 转换为true(已删除)
    *
    * @param dbData 数据库列值
    * @return 实体属性值，true表示已删除，false表示未删除
    */
    @Override
    public Boolean convertToEntityAttribute(Object dbData) {
        if (dbData == null) {
            return Boolean.FALSE;
        } else {
            // 如果是Boolean.FALSE，则表示未删除；其他情况(包括特殊标记对象)都视为已删除
            return Objects.equals(dbData, Boolean.FALSE);
        }
    }
}
