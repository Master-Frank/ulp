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
package cn.frank.ulp.support.security.password.generator;

import java.util.Arrays;
import java.util.List;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;

import cn.frank.ulp.support.security.password.PasswordGenerator;

/**
 * 默认密码生成器
 * 生成包含大小写字母和数字的随机密码
 */
public final class DefaultPasswordGenerator implements PasswordGenerator {
   /**
    * 字符规则列表
    */
   private final List<CharacterRule> characterRules;

   /**
    * 构造函数
    * 初始化字符规则，包括大写字母、小写字母和数字
    */
   public DefaultPasswordGenerator() {
      CharacterRule[] rules = new CharacterRule[3];
      rules[0] = new CharacterRule(EnglishCharacterData.UpperCase, 2);
      rules[1] = new CharacterRule(EnglishCharacterData.LowerCase, 2);
      rules[2] = new CharacterRule(EnglishCharacterData.Digit, 2);
      this.characterRules = Arrays.asList(rules);
   }

   /**
    * 生成密码
    *
    * @return 生成的密码
    */
   @Override
   public String generatePassword() {
      org.passay.PasswordGenerator generator = new org.passay.PasswordGenerator();
      return generator.generatePassword(10, this.characterRules);
   }
}
