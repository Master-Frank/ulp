/*
 * ULP - United Login Platform
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.frank.ulp.support.security.web;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import cn.frank.ulp.support.validation.annotation.Phone;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SessionCookieNameValidator implements ConstraintValidator<Phone, String> {

    public static final String COOKIE_NAME_REGEX = "^[a-zA-Z0-9_\\-]+$";

    private Pattern cookieNamePattern;

    @Override
    public void initialize(Phone constraintAnnotation) {
        this.cookieNamePattern = Pattern.compile(COOKIE_NAME_REGEX);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) {
            return true;
        }
        return this.cookieNamePattern.matcher(value).matches();
    }
}
