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
package cn.frank.ulp.support.security.crypto.password;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码编码器工厂类
 * 用于创建委托密码编码器
 *
 * 算法基线由 openspec spec `security-baseline` 锁定：
 * - 默认 encoder = argon2（Argon2id），参数对齐 OWASP 2023 Password Storage Cheat Sheet
 * - bcrypt 保留用于校验历史 {bcrypt} 密文，登录成功后由 UserDetailsPasswordService 自动 rehash
 * - noop 仅用于测试 fixture，生产路径禁止主动写入 {noop}
 */
public class PasswordEncoderFactories {

    /**
     * Argon2id 参数下限（与 security-baseline spec 同步）。
     */
    private static final int ARGON2_SALT_LENGTH = 16;
    private static final int ARGON2_HASH_LENGTH = 32;
    private static final int ARGON2_PARALLELISM = 1;
    private static final int ARGON2_MEMORY_KB   = 19456;
    private static final int ARGON2_ITERATIONS  = 2;

    /**
    * 创建委托密码编码器
    *
    * @return 密码编码器
    */
    public PasswordEncoder createDelegatingPasswordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        String defaultEncoder = "argon2";
        encoders.put("argon2", new Argon2PasswordEncoder(ARGON2_SALT_LENGTH, ARGON2_HASH_LENGTH,
            ARGON2_PARALLELISM, ARGON2_MEMORY_KB, ARGON2_ITERATIONS));
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("noop", NoOpPasswordEncoder.getInstance());
        return new DelegatingPasswordEncoder(defaultEncoder, encoders);
    }
}
