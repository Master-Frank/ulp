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
package cn.frank.ulp.support.security.password.validator;

import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.RuleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;

import cn.frank.ulp.support.security.password.PasswordValidator;
import cn.frank.ulp.support.security.password.exception.PasswordInvalidException;
import cn.frank.ulp.support.security.password.exception.PasswordLengthInvalidException;

import lombok.Generated;

/**
 * 密码长度验证器
 * 验证密码长度是否符合要求
 */
public final class PasswordLengthValidator implements PasswordValidator {
    @Generated
    private static final Logger logger = LoggerFactory.getLogger(PasswordLengthValidator.class);

    /**
    * 最小长度
    */
    private final Integer       minLength;

    /**
    * 最大长度
    */
    private final Integer       maxLength;

    /**
    * 构造函数
    *
    * @param minLength 最小长度
    * @param maxLength 最大长度
    */
    @Generated
    public PasswordLengthValidator(Integer minLength, Integer maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    /**
    * 验证密码
    *
    * @param password 密码
    * @throws PasswordInvalidException 密码无效异常
    */
    @Override
    public void validate(String password) throws PasswordInvalidException {
        LengthRule lengthRule = new LengthRule(this.minLength, this.maxLength);
        RuleResult result = lengthRule.validate(new PasswordData(password));
        if (!result.isValid()) {
            logger.error("密码长度验证失败: {}",
                JSONObject.toJSONString(result.getDetails(), new JSONWriter.Feature[0]));
            throw new PasswordLengthInvalidException(
                "密码长度必须在" + this.minLength + "到" + this.maxLength + "之间");
        }
    }
}
