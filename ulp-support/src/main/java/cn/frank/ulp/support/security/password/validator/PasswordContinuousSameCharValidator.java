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

import org.passay.PasswordData;
import org.passay.RepeatCharactersRule;
import org.passay.Rule;
import org.passay.RuleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;

import cn.frank.ulp.support.security.password.PasswordValidator;
import cn.frank.ulp.support.security.password.exception.PasswordContinuousSameCharInvalidException;
import cn.frank.ulp.support.security.password.exception.PasswordInvalidException;

import lombok.Generated;

/**
 * 密码连续相同字符验证器
 * 验证密码中连续相同字符的数量是否超过限制
 */
public final class PasswordContinuousSameCharValidator implements PasswordValidator {
    @Generated
    private static final Logger logger = LoggerFactory
        .getLogger(PasswordContinuousSameCharValidator.class);

    /**
    * 最大连续相同字符数
    */
    private final Integer       maxConsecutiveChars;

    /**
    * 验证密码
    *
    * @param password 密码
    * @throws PasswordInvalidException 密码无效异常
    */
    @Override
    public void validate(String password) throws PasswordInvalidException {
        RepeatCharactersRule rule = new RepeatCharactersRule(this.maxConsecutiveChars);
        Rule[] rules = new Rule[1];
        rules[0] = rule;
        org.passay.PasswordValidator validator = new org.passay.PasswordValidator(rules);
        RuleResult result = validator.validate(new PasswordData(password));
        if (!result.isValid()) {
            logger.error("密码连续相同字符验证失败",
                JSONObject.toJSONString(result.getDetails(), new JSONWriter.Feature[0]));
            throw new PasswordContinuousSameCharInvalidException("密码中连续相同字符数量超过限制");
        }
    }

    /**
    * 构造函数
    *
    * @param maxConsecutiveChars 最大连续相同字符数
    */
    @Generated
    public PasswordContinuousSameCharValidator(Integer maxConsecutiveChars) {
        this.maxConsecutiveChars = maxConsecutiveChars;
    }
}
