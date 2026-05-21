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
package cn.frank.ulp.support.security.password.validator;

import org.passay.CharacterCharacteristicsRule;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordData;
import org.passay.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.frank.ulp.support.security.password.PasswordValidator;
import cn.frank.ulp.support.security.password.enums.PasswordComplexityRule;
import cn.frank.ulp.support.security.password.exception.PasswordComplexityRuleInvalidException;
import cn.frank.ulp.support.security.password.exception.PasswordInvalidException;

import lombok.Generated;

/**
 * 密码复杂度规则验证器
 * 根据不同的密码复杂度规则验证密码是否符合要求
 */
public final class PasswordComplexityRuleValidator implements PasswordValidator {
    /**
     * 日志记录器
     */
    @Generated
    private static final Logger logger = LoggerFactory.getLogger(PasswordComplexityRuleValidator.class);

    /**
     * 密码复杂度规则
     */
    private final PasswordComplexityRule complexityRule;

    /**
     * 验证密码
     *
     * @param password 密码
     * @throws PasswordInvalidException 密码无效异常
     */
    @Override
    public void validate(String password) throws PasswordInvalidException {
        if (!this.complexityRule.equals(PasswordComplexityRule.NONE)) {
            if (this.complexityRule.equals(PasswordComplexityRule.MUST_NUMBERS_AND_LETTERS)) {
                // 必须包含数字和字母
                Rule[] rules = new Rule[2];
                rules[0] = new CharacterRule(EnglishCharacterData.Digit, 1);
                rules[1] = new CharacterRule(EnglishCharacterData.Alphabetical, 1);
                org.passay.PasswordValidator validator = new org.passay.PasswordValidator(rules);
                if (!validator.validate(new PasswordData(password)).isValid()) {
                    throw new PasswordComplexityRuleInvalidException("密码必须包含数字和字母");
                }
            } else if (this.complexityRule.equals(PasswordComplexityRule.MUST_NUMBERS_AND_CAPITAL_LETTERS)) {
                // 必须包含数字和大写字母
                Rule[] rules = new Rule[2];
                rules[0] = new CharacterRule(EnglishCharacterData.Digit, 1);
                rules[1] = new CharacterRule(EnglishCharacterData.UpperCase, 1);
                org.passay.PasswordValidator validator = new org.passay.PasswordValidator(rules);
                if (!validator.validate(new PasswordData(password)).isValid()) {
                    throw new PasswordComplexityRuleInvalidException("密码必须包含数字和大写字母");
                }
            } else if (this.complexityRule.equals(PasswordComplexityRule.MUST_CONTAIN_NUMBERS_UPPERCASE_LETTERS_LOWERCASE_LETTERS_AND_SPECIAL_CHARACTERS)) {
                // 必须包含数字、大写字母、小写字母和特殊字符
                Rule[] rules = new Rule[3];
                rules[0] = new CharacterRule(EnglishCharacterData.Alphabetical, 2);
                rules[1] = new CharacterRule(EnglishCharacterData.Digit, 1);
                rules[2] = new CharacterRule(EnglishCharacterData.Special, 1);
                org.passay.PasswordValidator validator = new org.passay.PasswordValidator(rules);
                if (!validator.validate(new PasswordData(password)).isValid()) {
                    throw new PasswordComplexityRuleInvalidException("密码必须包含数字、大写字母、小写字母和特殊字符");
                }
            } else if (this.complexityRule.equals(PasswordComplexityRule.CONTAIN_AT_LEAST_TWO_OF_NUMBERS_LETTERS_AND_SPECIAL_CHARACTERS)) {
                // 至少包含数字、字母和特殊字符中的两种
                CharacterRule[] rules = new CharacterRule[3];
                rules[0] = new CharacterRule(EnglishCharacterData.Digit, 1);
                rules[1] = new CharacterRule(EnglishCharacterData.Special, 1);
                rules[2] = new CharacterRule(EnglishCharacterData.Alphabetical, 1);

                CharacterCharacteristicsRule characteristicRule = new CharacterCharacteristicsRule(rules);
                characteristicRule.setNumberOfCharacteristics(2);

                Rule[] validatorRules = new Rule[1];
                validatorRules[0] = characteristicRule;
                org.passay.PasswordValidator validator = new org.passay.PasswordValidator(validatorRules);
                if (!validator.validate(new PasswordData(password)).isValid()) {
                    throw new PasswordComplexityRuleInvalidException("密码至少包含数字、字母和特殊字符中的两种");
                }
            } else if (this.complexityRule.equals(PasswordComplexityRule.CONTAIN_AT_LEAST_THREE_OF_NUMBERS_UPPERCASE_LETTERS_LOWERCASE_LETTERS_AND_SPECIAL_CHARACTERS)) {
                // 至少包含数字、大写字母、小写字母和特殊字符中的三种
                CharacterRule[] rules = new CharacterRule[4];
                rules[0] = new CharacterRule(EnglishCharacterData.Digit, 1);
                rules[1] = new CharacterRule(EnglishCharacterData.Special, 1);
                rules[2] = new CharacterRule(EnglishCharacterData.LowerCase, 1);
                rules[3] = new CharacterRule(EnglishCharacterData.UpperCase, 1);

                CharacterCharacteristicsRule characteristicRule = new CharacterCharacteristicsRule(rules);
                characteristicRule.setNumberOfCharacteristics(3);

                Rule[] validatorRules = new Rule[1];
                validatorRules[0] = characteristicRule;
                org.passay.PasswordValidator validator = new org.passay.PasswordValidator(validatorRules);
                if (!validator.validate(new PasswordData(password)).isValid()) {
                    throw new PasswordComplexityRuleInvalidException("密码至少包含数字、大写字母、小写字母和特殊字符中的三种");
                }
            } else {
                throw new PasswordComplexityRuleInvalidException("不支持的密码复杂度规则");
            }
        }
    }

    /**
     * 构造函数
     *
     * @param complexityRule 密码复杂度规则
     */
    @Generated
    public PasswordComplexityRuleValidator(PasswordComplexityRule complexityRule) {
        this.complexityRule = complexityRule;
    }
}
