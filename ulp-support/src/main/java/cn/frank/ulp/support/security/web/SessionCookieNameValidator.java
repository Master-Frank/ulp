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
package cn.frank.ulp.support.security.web;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import cn.frank.ulp.support.validation.annotation.Phone;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SessionCookieNameValidator implements ConstraintValidator<Phone, String> {

    public static final String COOKIE_NAME_REGEX = "^[a-zA-Z0-9_\\-]+$";

    private Pattern            cookieNamePattern;

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
