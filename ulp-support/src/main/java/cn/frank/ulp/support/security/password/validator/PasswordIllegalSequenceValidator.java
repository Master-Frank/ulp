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
package cn.frank.ulp.support.security.password.validator;

import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.PasswordData;
import org.passay.Rule;
import org.passay.RuleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;

import cn.frank.ulp.support.security.password.PasswordValidator;
import cn.frank.ulp.support.security.password.exception.PasswordIllegalSequenceInvalidException;
import cn.frank.ulp.support.security.password.exception.PasswordInvalidException;

import lombok.Generated;

/**
 * 密码非法序列验证器
 * 验证密码是否包含非法序列（如连续字母、数字、键盘序列等）
 */
public final class PasswordIllegalSequenceValidator implements PasswordValidator {
    @Generated
    private static final Logger logger = LoggerFactory
        .getLogger(PasswordIllegalSequenceValidator.class);

    /**
    * 是否启用验证
    */
    private final Boolean       enabled;

    /**
    * 构造函数
    *
    * @param enabled 是否启用
    */
    @Generated
    public PasswordIllegalSequenceValidator(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
    * 验证密码
    *
    * @param password 密码
    * @throws PasswordInvalidException 密码无效异常
    */
    @Override
    public void validate(String password) throws PasswordInvalidException {
        if (this.enabled) {
            IllegalSequenceRule alphabeticalRule = new IllegalSequenceRule(
                EnglishSequenceData.Alphabetical);
            IllegalSequenceRule numericalRule = new IllegalSequenceRule(
                EnglishSequenceData.Numerical);
            IllegalSequenceRule qwertyRule = new IllegalSequenceRule(EnglishSequenceData.USQwerty);

            Rule[] rules = new Rule[3];
            rules[0] = alphabeticalRule;
            rules[1] = numericalRule;
            rules[2] = qwertyRule;

            org.passay.PasswordValidator validator = new org.passay.PasswordValidator(rules);
            RuleResult result = validator.validate(new PasswordData(password));
            if (!result.isValid()) {
                logger.error("密码非法序列验证失败",
                    JSONObject.toJSONString(result.getDetails(), new JSONWriter.Feature[0]));
                throw new PasswordIllegalSequenceInvalidException("密码不能包含连续的字母、数字或键盘序列");
            }
        }
    }
}
