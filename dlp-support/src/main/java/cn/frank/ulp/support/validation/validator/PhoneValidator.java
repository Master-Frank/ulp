/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.support.validation.validator;

import org.apache.commons.lang3.StringUtils;

import cn.frank.ulp.support.util.PhoneUtils;
import cn.frank.ulp.support.validation.annotation.Phone;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

    @Override
    public void initialize(Phone phone) {
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(phone)) {
            return false;
        }
        return PhoneUtils.isPhoneValidate(phone);
    }
}
