/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
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

    private static final class StringToEnumConverter<T extends Enum<?>> implements Converter<String, T> {

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
