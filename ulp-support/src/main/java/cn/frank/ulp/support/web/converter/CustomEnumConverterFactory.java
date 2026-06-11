/*
 * ulp-support - ULP support library
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
package cn.frank.ulp.support.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import cn.frank.ulp.support.enums.BaseEnum;

public class CustomEnumConverterFactory implements ConverterFactory<String, Enum<?>> {

    @Override
    @NonNull
    public <T extends Enum<?>> Converter<String, T> getConverter(@NonNull Class<T> targetType) {
        return new StringToEnumConverter<>(targetType);
    }

    private static final class StringToEnumConverter<T extends Enum<?>>
                                                    implements Converter<String, T> {

        private final Class<T> targetType;

        StringToEnumConverter(Class<T> targetType) {
            this.targetType = targetType;
        }

        @Override
        @Nullable
        public T convert(@NonNull String source) {
            if (source.isEmpty()) {
                return null;
            }
            for (T constant : this.targetType.getEnumConstants()) {
                if (constant instanceof BaseEnum) {
                    if (((BaseEnum) constant).getCode().equals(source)) {
                        return constant;
                    }
                } else if (constant.name().equals(source)) {
                    return constant;
                }
            }
            return null;
        }
    }
}
