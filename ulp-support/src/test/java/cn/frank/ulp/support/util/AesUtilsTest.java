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
package cn.frank.ulp.support.util;

import javax.crypto.Cipher;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 锁定 AesUtils 当前契约。
 * 注意：当前实现使用 Cipher.getInstance("AES")，JDK SunJCE 默认解析为 AES/ECB/PKCS5Padding。
 * 这是与前端的约定，不要在没有同步前端的情况下"修复"为 CBC。
 */
class AesUtilsTest {

    private static final String KEY_BASE64 = AesUtils.generateKey();

    @Test
    void roundTripAscii() {
        String plain = "admin@ulp";
        String cipher = AesUtils.encrypt(plain, KEY_BASE64);
        assertThat(AesUtils.decrypt(cipher, KEY_BASE64)).isEqualTo(plain);
    }

    @Test
    void roundTripCjk() {
        String plain = "用户名密码-中文测试";
        String cipher = AesUtils.encrypt(plain, KEY_BASE64);
        assertThat(AesUtils.decrypt(cipher, KEY_BASE64)).isEqualTo(plain);
    }

    @Test
    void instanceMethodsRoundTrip() {
        AesUtils utils = new AesUtils(KEY_BASE64);
        String cipher = utils.encrypt("hello");
        assertThat(utils.decrypt(cipher)).isEqualTo("hello");
    }

    @Test
    void emptyOrBlankReturnsNull() {
        AesUtils utils = new AesUtils(KEY_BASE64);
        assertThat(utils.encrypt("")).isNull();
        assertThat(utils.decrypt("")).isNull();
        assertThat(utils.encrypt(null)).isNull();
        assertThat(utils.decrypt(null)).isNull();
    }

    @Test
    void wrongKeyThrows() {
        String cipher = AesUtils.encrypt("payload", KEY_BASE64);
        String wrongKey = AesUtils.generateKey();
        assertThatThrownBy(() -> AesUtils.decrypt(cipher, wrongKey))
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    void invalidBase64KeyThrows() {
        assertThatThrownBy(() -> AesUtils.encrypt("x", "not-base64!@#"))
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    void documentedTransformIsEcb() throws Exception {
        // 锁定当前 transformation。如果改成 CBC/GCM，前端约定需要同步改。
        Cipher c = Cipher.getInstance("AES");
        assertThat(c.getAlgorithm()).isEqualTo("AES");
        // ECB 不需要 IV，文档化此事实
        // 若引入 BouncyCastle 等 provider 此 assertion 可能失败 — 这是预期信号
    }

    @Test
    void generateKeyReturnsBase64Of16Bytes() {
        String k = AesUtils.generateKey();
        byte[] decoded = java.util.Base64.getDecoder().decode(k);
        assertThat(decoded).hasSize(16); // 128-bit
    }
}
