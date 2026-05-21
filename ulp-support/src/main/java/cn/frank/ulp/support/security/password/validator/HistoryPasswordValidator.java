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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.google.common.collect.Lists;

import cn.frank.ulp.support.security.password.PasswordValidator;
import cn.frank.ulp.support.security.password.exception.PasswordHistoryInvalidException;
import cn.frank.ulp.support.security.password.exception.PasswordInvalidException;

import lombok.Generated;

/**
 * 历史密码验证器
 * 验证新密码是否与历史密码重复
 */
public final class HistoryPasswordValidator implements PasswordValidator {
    @Generated
    private static final Logger   logger = LoggerFactory.getLogger(HistoryPasswordValidator.class);

    /**
    * 是否启用验证
    */
    private final Boolean         enabled;

    /**
    * 密码编码器
    */
    private final PasswordEncoder passwordEncoder;

    /**
    * 历史密码列表
    */
    private final List<String>    historyPasswords;

    /**
    * 验证密码
    *
    * @param password 密码
    * @throws PasswordInvalidException 密码无效异常
    */
    @Override
    public void validate(String password) throws PasswordInvalidException {
        if (this.enabled) {
            for (String historyPassword : this.historyPasswords) {
                if (this.passwordEncoder.matches(password, historyPassword)) {
                    throw new PasswordHistoryInvalidException("密码不能与历史密码重复");
                }
            }
        }
    }

    /**
    * 构造函数
    *
    * @param enabled 是否启用
    * @param historyPasswords 历史密码列表
    * @param passwordEncoder 密码编码器
    */
    @Generated
    public HistoryPasswordValidator(Boolean enabled, List<String> historyPasswords,
                                    PasswordEncoder passwordEncoder) {
        this.enabled = enabled;
        this.historyPasswords = historyPasswords;
        this.passwordEncoder = passwordEncoder;
    }

    /**
    * 构造函数
    *
    * @param enabled 是否启用
    */
    public HistoryPasswordValidator(Boolean enabled) {
        this.enabled = enabled;
        this.historyPasswords = Lists.newArrayList();
        this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
    * 构造函数
    *
    * @param historyPasswords 历史密码列表
    * @param passwordEncoder 密码编码器
    */
    public HistoryPasswordValidator(List<String> historyPasswords,
                                    PasswordEncoder passwordEncoder) {
        this.enabled = Boolean.TRUE;
        this.historyPasswords = historyPasswords;
        this.passwordEncoder = passwordEncoder;
    }
}
