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
package cn.frank.ulp.support.security.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.frank.ulp.support.result.ApiRestResult;
import cn.frank.ulp.support.security.password.PasswordGenerator;

/**
 * 密码生成器控制器
 * 用于提供密码生成相关接口
 */
@RestController
public class PasswordGeneratorController {

    /**
    * 密码生成器
    */
    private final PasswordGenerator passwordGenerator;

    /**
    * 字符串解密方法
    *
    * @param object 待解密的对象
    * @return 解密后的字符串
    */
    public static String decryptString(Object object) {
        String str = (String) object;
        char[] chars = new char[str.length()];

        for (int i = str.length() - 1; i >= 0; i--) {
            if (i % 2 == 0) {
                chars[i] = (char) (str.charAt(i) ^ (str.length() - 1));
            } else {
                chars[i] = (char) (str.charAt(i) ^ 5);
            }
        }

        return new String(chars);
    }

    /**
    * 生成随机密码
    *
    * @return 随机密码结果
    */
    @GetMapping({ "/api/v1/random_password" })
    public ApiRestResult<String> randomPassword() {
        return ApiRestResult.ok(this.passwordGenerator.generatePassword());
    }

    /**
    * 构造函数
    *
    * @param passwordGenerator 密码生成器
    */
    public PasswordGeneratorController(PasswordGenerator passwordGenerator) {
        this.passwordGenerator = passwordGenerator;
    }
}
