package cn.frank.ulp.support.util;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.util.StringUtils;

/**
 * AES 加解密工具.
 */
public class AesUtils {

    private final String key;

    public AesUtils(String key) {
        this.key = key;
    }

    public String encrypt(String plain) {
        return StringUtils.hasText(plain) ? encrypt(plain, this.key) : null;
    }

    public String decrypt(String cipherText) {
        return StringUtils.hasText(cipherText) ? decrypt(cipherText, this.key) : null;
    }

    public static String encrypt(String plain, String key) {
        try {
            SecretKey secretKey = buildKey(key);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder()
                .encodeToString(cipher.doFinal(plain.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String cipherText, String key) {
        try {
            SecretKey secretKey = buildKey(key);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)),
                StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateKey() {
        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128, new SecureRandom());
            SecretKey key = kg.generateKey();
            return Base64.getEncoder().encodeToString(key.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static SecretKey buildKey(String key) {
        byte[] decoded = Base64.getDecoder().decode(key);
        return new SecretKeySpec(decoded, "AES");
    }
}
