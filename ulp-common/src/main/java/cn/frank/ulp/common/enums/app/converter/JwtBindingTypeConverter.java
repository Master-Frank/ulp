/*
 * ulp-common - United Login Platform
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
package cn.frank.ulp.common.enums.app.converter;

import java.util.Objects;

import cn.frank.ulp.common.enums.app.JwtBindingType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2022/5/22 23:25
 */
@Converter(autoApply = true)
public class JwtBindingTypeConverter implements AttributeConverter<JwtBindingType, String> {
    @Override
    public String convertToDatabaseColumn(JwtBindingType attribute) {
        if (Objects.isNull(attribute)) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public JwtBindingType convertToEntityAttribute(String dbData) {
        return JwtBindingType.getType(dbData);
    }
}
