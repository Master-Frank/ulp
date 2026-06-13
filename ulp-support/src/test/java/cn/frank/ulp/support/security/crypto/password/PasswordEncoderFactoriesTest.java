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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * 锁定 security-baseline spec 中的密码 encoder 注册基线。
 */
class PasswordEncoderFactoriesTest {

    private PasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        encoder = new PasswordEncoderFactories().createDelegatingPasswordEncoder();
    }

    @Test
    void encodeProducesArgon2Prefix() {
        String encoded = encoder.encode("changeit");
        assertThat(encoded).startsWith("{argon2}");
    }

    @Test
    void matchesLegacyBcryptHash() {
        String legacy = "{bcrypt}" + new BCryptPasswordEncoder().encode("changeit");
        assertThat(encoder.matches("changeit", legacy)).isTrue();
        assertThat(encoder.matches("wrong-password", legacy)).isFalse();
    }

    @Test
    void upgradeEncodingFlagsBcryptForRehash() {
        assertThat(encoder).isInstanceOf(DelegatingPasswordEncoder.class);
        DelegatingPasswordEncoder delegating = (DelegatingPasswordEncoder) encoder;

        String legacyBcrypt = "{bcrypt}" + new BCryptPasswordEncoder().encode("changeit");
        assertThat(delegating.upgradeEncoding(legacyBcrypt)).isTrue();

        String newArgon2 = delegating.encode("changeit");
        assertThat(delegating.upgradeEncoding(newArgon2)).isFalse();
    }

    @Test
    void noopEncoderStillRegisteredForTestFixtures() {
        assertThat(encoder.matches("changeit", "{noop}changeit")).isTrue();
    }
}
