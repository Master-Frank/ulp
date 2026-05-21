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
