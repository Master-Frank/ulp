/*
 * eiam-common - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.common.enums.app.converter;

import java.util.Objects;

import cn.frank.ulp.common.enums.app.FormEncryptType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/2/23 23:57
 */
@Converter(autoApply = true)
public class FormEncryptTypeConverter implements AttributeConverter<FormEncryptType, String> {
    @Override
    public String convertToDatabaseColumn(FormEncryptType attribute) {
        if (Objects.isNull(attribute)) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public FormEncryptType convertToEntityAttribute(String dbData) {
        return FormEncryptType.getType(dbData);
    }
}
