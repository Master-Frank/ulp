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
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import lombok.Data;

/**
 * RSA 加解密 / 密钥工具.
 */
public class RsaUtils {

    /** 算法名（保留字段以兼容旧调用方）。 */
    public static final String PUBLIC_KEY       = "RSA";
    public static final String PRIVATE_KEY      = "RSA";

    /** 默认密钥长度。 */
    private static final int   DEFAULT_KEY_SIZE = 2048;

    public RsaUtils() {
    }

    public static String encrypt(RSAPublicKey publicKey, byte[] data) throws Exception {
        if (publicKey == null) {
            throw new Exception("加密公钥为空, 请设置");
        }
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return base64ToStr(cipher.doFinal(data));
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法", e);
        } catch (java.security.InvalidKeyException e) {
            throw new Exception("加密公钥非法,请检查", e);
        } catch (javax.crypto.IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (javax.crypto.BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        } catch (javax.crypto.NoSuchPaddingException e) {
            return null;
        }
    }

    public static String encrypt(RSAPrivateKey privateKey, byte[] data) throws Exception {
        if (privateKey == null) {
            throw new Exception("加密私钥为空, 请设置");
        }
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return base64ToStr(cipher.doFinal(data));
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法", e);
        } catch (java.security.InvalidKeyException e) {
            throw new Exception("加密私钥非法,请检查", e);
        } catch (javax.crypto.IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (javax.crypto.BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        } catch (javax.crypto.NoSuchPaddingException e) {
            return null;
        }
    }

    public static String decrypt(RSAPublicKey publicKey, byte[] data) throws Exception {
        if (publicKey == null) {
            throw new Exception("解密公钥为空, 请设置");
        }
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return new String(cipher.doFinal(data), StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法", e);
        } catch (java.security.InvalidKeyException e) {
            throw new Exception("解密公钥非法,请检查", e);
        } catch (javax.crypto.IllegalBlockSizeException e) {
            throw new Exception("密文长度非法");
        } catch (javax.crypto.BadPaddingException e) {
            throw new Exception("密文数据已损坏");
        } catch (javax.crypto.NoSuchPaddingException e) {
            return null;
        }
    }

    public static String decrypt(RSAPrivateKey privateKey, byte[] data) throws Exception {
        if (privateKey == null) {
            throw new Exception("解密私钥为空, 请设置");
        }
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(cipher.doFinal(data), StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法", e);
        } catch (java.security.InvalidKeyException e) {
            throw new Exception("解密私钥非法,请检查", e);
        } catch (javax.crypto.IllegalBlockSizeException e) {
            throw new Exception("密文长度非法");
        } catch (javax.crypto.BadPaddingException e) {
            throw new Exception("密文数据已损坏");
        } catch (javax.crypto.NoSuchPaddingException e) {
            return null;
        }
    }

    public static RSAPublicKey loadPublicKey(String publicKeyStr) throws Exception {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(publicKeyStr);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            return (RSAPublicKey) factory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法", e);
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法", e);
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    public static RSAPrivateKey loadPrivateKey(String privateKeyStr) throws Exception {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法", e);
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法", e);
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    public static String base64ToStr(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] strToBase64(String str) {
        return Base64.getDecoder().decode(str);
    }

    public static RsaResult getKeys() {
        return getKeys(DEFAULT_KEY_SIZE);
    }

    public static RsaResult getKeys(int keySize) {
        if (keySize < DEFAULT_KEY_SIZE) {
            throw new IllegalArgumentException("RSA 密钥长度不少于 2048 位");
        }
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(keySize, new SecureRandom());
            KeyPair keyPair = generator.generateKeyPair();
            return new RsaResult(keyPair.getPrivate(), keyPair.getPublic());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Data
    public static class RsaResult {
        private PrivateKey privateKey;
        private PublicKey  publicKey;

        public RsaResult(PrivateKey privateKey, PublicKey publicKey) {
            this.privateKey = privateKey;
            this.publicKey = publicKey;
        }
    }
}
