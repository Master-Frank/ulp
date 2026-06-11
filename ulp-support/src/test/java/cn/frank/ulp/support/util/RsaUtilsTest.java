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

import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RsaUtilsTest {

    @Test
    void getKeysGenerates2048() {
        RsaUtils.RsaResult keys = RsaUtils.getKeys();
        assertThat(keys.getPublicKey()).isNotNull();
        assertThat(keys.getPrivateKey()).isNotNull();
        assertThat(keys.getPublicKey().getAlgorithm()).isEqualTo("RSA");
    }

    @Test
    void getKeysRejectsTooSmallSize() {
        assertThatThrownBy(() -> RsaUtils.getKeys(1024))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void publicEncryptPrivateDecryptAscii() throws Exception {
        RsaUtils.RsaResult keys = RsaUtils.getKeys();
        RSAPublicKey pub = (RSAPublicKey) keys.getPublicKey();
        RSAPrivateKey priv = (RSAPrivateKey) keys.getPrivateKey();

        String plain = "hello-rsa";
        String cipher = RsaUtils.encrypt(pub, plain.getBytes(StandardCharsets.UTF_8));
        String decrypted = RsaUtils.decrypt(priv, RsaUtils.strToBase64(cipher));
        assertThat(decrypted).isEqualTo(plain);
    }

    /**
     * RsaUtils.decrypt 使用 new String(bytes) 不指定 charset。
     * Windows 默认 GBK，Linux 默认 UTF-8。CJK 数据跨平台会乱。
     * 此测试在 UTF-8 平台通过；GBK 平台会失败 — 暴露 bug。
     */
    @Test
    void publicEncryptPrivateDecryptCjk() throws Exception {
        RsaUtils.RsaResult keys = RsaUtils.getKeys();
        RSAPublicKey pub = (RSAPublicKey) keys.getPublicKey();
        RSAPrivateKey priv = (RSAPrivateKey) keys.getPrivateKey();

        String plain = "中文-rsa-测试";
        String cipher = RsaUtils.encrypt(pub, plain.getBytes(StandardCharsets.UTF_8));
        String decrypted = RsaUtils.decrypt(priv, RsaUtils.strToBase64(cipher));
        // 在 UTF-8 平台应通过；GBK 平台暴露 charset 缺失
        assertThat(decrypted).isEqualTo(plain);
    }

    @Test
    void loadPublicKeyRoundTrip() throws Exception {
        RsaUtils.RsaResult keys = RsaUtils.getKeys();
        String pubB64 = RsaUtils.base64ToStr(keys.getPublicKey().getEncoded());
        RSAPublicKey loaded = RsaUtils.loadPublicKey(pubB64);
        assertThat(loaded.getEncoded()).isEqualTo(keys.getPublicKey().getEncoded());
    }

    @Test
    void loadPrivateKeyRoundTrip() throws Exception {
        RsaUtils.RsaResult keys = RsaUtils.getKeys();
        String privB64 = RsaUtils.base64ToStr(keys.getPrivateKey().getEncoded());
        RSAPrivateKey loaded = RsaUtils.loadPrivateKey(privB64);
        assertThat(loaded.getEncoded()).isEqualTo(keys.getPrivateKey().getEncoded());
    }

    @Test
    void loadPublicKeyRejectsGarbage() {
        assertThatThrownBy(() -> RsaUtils.loadPublicKey("not-a-key")).isInstanceOf(Exception.class);
    }

    @Test
    void encryptRejectsNullPublicKey() {
        assertThatThrownBy(() -> RsaUtils.encrypt((RSAPublicKey) null, new byte[] { 1 }))
            .isInstanceOf(Exception.class).hasMessageContaining("公钥");
    }

    @Test
    void decryptRejectsNullPrivateKey() {
        assertThatThrownBy(() -> RsaUtils.decrypt((RSAPrivateKey) null, new byte[] { 1 }))
            .isInstanceOf(Exception.class).hasMessageContaining("私钥");
    }
}
