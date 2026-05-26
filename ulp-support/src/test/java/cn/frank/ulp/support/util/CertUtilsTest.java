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
package cn.frank.ulp.support.util;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class CertUtilsTest {

    private static X509Certificate buildCert(String dnString) throws Exception {
        KeyPairGenerator g = KeyPairGenerator.getInstance("RSA");
        g.initialize(2048);
        KeyPair kp = g.generateKeyPair();
        X500Name name = new X500Name(dnString);
        Date now = new Date();
        Date later = new Date(now.getTime() + 86_400_000L);
        JcaX509v3CertificateBuilder b = new JcaX509v3CertificateBuilder(name, BigInteger.ONE, now,
            later, name, kp.getPublic());
        ContentSigner signer = new JcaContentSignerBuilder("SHA256WithRSA").build(kp.getPrivate());
        return new JcaX509CertificateConverter().getCertificate(b.build(signer));
    }

    @Test
    void getNotAfterReturnsString() throws Exception {
        X509Certificate cert = buildCert("CN=x,O=ULP,C=CN");
        assertThat(CertUtils.getNotAfter(cert)).isNotBlank();
    }

    @Test
    void getCertificateBytesAndBackRoundTrip() throws Exception {
        X509Certificate cert = buildCert("CN=rt,O=ULP,C=CN");
        byte[] der = cert.getEncoded();
        X509Certificate restored = CertUtils.getCertificate(der);
        assertThat(restored.getSerialNumber()).isEqualTo(cert.getSerialNumber());
    }

    @Test
    void encodePemRoundTrip() throws Exception {
        X509Certificate cert = buildCert("CN=pem,O=ULP,C=CN");
        String pem = CertUtils.encodePem(cert);
        assertThat(pem).contains("-----BEGIN CERTIFICATE-----");
        X509Certificate restored = CertUtils.loadCertFromString(pem);
        assertThat(restored.getSerialNumber()).isEqualTo(cert.getSerialNumber());
    }

    @Test
    void getPinComputesSha256() throws Exception {
        X509Certificate cert = buildCert("CN=pin,O=ULP,C=CN");
        String pin = CertUtils.getPin(cert);
        assertThat(pin).isNotBlank();
        assertThat(CertUtils.getPin(cert)).isEqualTo(pin);
    }
}
