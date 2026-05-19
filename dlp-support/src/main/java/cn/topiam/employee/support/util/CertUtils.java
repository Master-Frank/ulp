package cn.topiam.employee.support.util;

import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStrictStyle;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import cn.topiam.employee.support.exception.TopIamException;

/**
 * 证书工具类（基于 BouncyCastle）.
 */
public class CertUtils {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CertUtils.class);

    private static final String BC = "BC";

    static {
        if (Security.getProvider(BC) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    public CertUtils() {
    }

    public static byte[] getDer(String pem) {
        return Base64.getDecoder().decode(keyCleanup(pem));
    }

    public static byte[] getDer(String pem, String header, String footer) {
        String[] parts = pem.split(footer);
        parts = parts[0].split(header);
        return getDer(parts[1]);
    }

    public static String isRoot(X509Certificate cert) {
        return String
            .valueOf(cert.getSubjectX500Principal().equals(cert.getIssuerX500Principal()))
            .toUpperCase();
    }

    public static String getNotAfter(X509Certificate cert) {
        return cert.getNotAfter().toString();
    }

    public static String getIssuerDn(X509Certificate cert) {
        String dn = cert.getIssuerX500Principal().toString();
        int idx = "CN=".length();
        int comma = dn.indexOf(',');
        if (comma < 0) {
            comma = dn.length();
        }
        return dn.substring(idx, comma);
    }

    public static String getSubjectDn(X509Certificate cert) {
        String dn = cert.getSubjectX500Principal().toString();
        int idx = "CN=".length();
        int comma = dn.indexOf(',');
        if (comma < 0) {
            comma = dn.length();
        }
        return dn.substring(idx, comma);
    }

    public static PrivateKey readPrivateKey(String pem, String password) throws IOException {
        try (PEMParser parser = new PEMParser(new CharArrayReader(pem.toCharArray()))) {
            Object object = parser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider(BC);
            if (object == null) {
                throw new NullPointerException("Unable to decode PEM key:" + pem);
            }
            KeyPair keyPair;
            if (object instanceof PEMEncryptedKeyPair encryptedKeyPair) {
                PEMDecryptorProvider decryptor = new JcePEMDecryptorProviderBuilder()
                    .build(password.toCharArray());
                keyPair = converter.getKeyPair(encryptedKeyPair.decryptKeyPair(decryptor));
            } else if (object instanceof org.bouncycastle.asn1.pkcs.PrivateKeyInfo info) {
                return converter.getPrivateKey(info);
            } else {
                PEMKeyPair pemKeyPair = (PEMKeyPair) object;
                keyPair = converter.getKeyPair(pemKeyPair);
            }
            return keyPair.getPrivate();
        }
    }

    public static PublicKey readPublicKey(String pem, String algorithm) throws IOException {
        try (PEMParser parser = new PEMParser(new CharArrayReader(pem.toCharArray()))) {
            Object object = parser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider(BC);
            if (object == null) {
                throw new NullPointerException("Unable to decode PEM key:" + pem);
            }
            if (object instanceof org.bouncycastle.asn1.x509.SubjectPublicKeyInfo info) {
                return converter.getPublicKey(info);
            }
            if (object instanceof PEMEncryptedKeyPair encryptedKeyPair) {
                PEMDecryptorProvider decryptor = new JcePEMDecryptorProviderBuilder()
                    .build(algorithm.toCharArray());
                return converter.getKeyPair(encryptedKeyPair.decryptKeyPair(decryptor)).getPublic();
            }
            PEMKeyPair pemKeyPair = (PEMKeyPair) object;
            return converter.getKeyPair(pemKeyPair).getPublic();
        }
    }

    public static String getPin(X509Certificate cert) {
        try {
            byte[] spki = cert.getPublicKey().getEncoded();
            byte[] sha = MessageDigest.getInstance("SHA-256").digest(spki);
            return Base64.getEncoder().encodeToString(sha);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static X509Certificate getCertificate(byte[] der) throws CertificateException {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        return (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(der));
    }

    public static X500Name getX500Name(String c, String st, String l, String o, String ou,
                                       String cn) {
        X500NameBuilder builder = new X500NameBuilder(BCStrictStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, cn);
        builder.addRDN(BCStyle.O, o);
        builder.addRDN(BCStyle.L, l);
        builder.addRDN(BCStyle.ST, st);
        builder.addRDN(BCStyle.C, c);
        builder.addRDN(BCStyle.OU, ou);
        return builder.build();
    }

    public static String getCertificate(X500Name issuer, X500Name subject, BigInteger serial,
                                        Date notBefore, Date notAfter, PublicKey publicKey,
                                        PrivateKey privateKey) throws OperatorCreationException,
                                                               CertificateException, IOException {
        JcaX509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(issuer, serial,
            notBefore, notAfter, subject, publicKey);
        if (issuer.equals(subject)) {
            builder.addExtension(Extension.basicConstraints, false, new BasicConstraints(true));
        }
        var signer = new JcaContentSignerBuilder("SHA256WithRSA").setProvider(BC).build(privateKey);
        var holder = builder.build(signer);
        JcaX509CertificateConverter converter = new JcaX509CertificateConverter().setProvider(BC);
        return getCertificate(converter.getCertificate(holder));
    }

    public static X509Certificate loadCertFromString(String pem) {
        try {
            CertificateFactory factory = CertificateFactory.getInstance("X.509", BC);
            byte[] der = Base64.getDecoder().decode(keyCleanup(pem));
            return (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(der));
        } catch (CertificateException | NoSuchProviderException e) {
            log.error(e.getMessage());
            throw new TopIamException(e.getMessage(), e);
        }
    }

    public static X509Certificate loadCertFromInputStream(InputStream is) throws Exception {
        try {
            return (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(is);
        } catch (CertificateException e) {
            log.error("证书加载失败");
            throw new Exception("证书加载失败 X.509", e);
        }
    }

    public static String keyCleanup(String key) {
        return key.replaceAll("-----BEGIN [^-]+-----", "")
            .replaceAll("-----END [^-]+-----", "")
            .replaceAll("\\s", "")
            .trim();
    }

    public static String encodePem(Certificate cert) {
        try {
            return toPem("CERTIFICATE", cert.getEncoded());
        } catch (CertificateEncodingException e) {
            throw new TopIamException("证书编码失败", e);
        }
    }

    public static String encodePem(PublicKey publicKey) {
        return toPem("PUBLIC KEY", publicKey.getEncoded());
    }

    public static String encodePem(PrivateKey privateKey) {
        return toPem("PRIVATE KEY", privateKey.getEncoded());
    }

    public static RSAPrivateKey getPrivateKey(byte[] keyBytes,
                                              String algorithm) throws InvalidKeySpecException,
                                                                NoSuchAlgorithmException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        return (RSAPrivateKey) KeyFactory.getInstance(algorithm).generatePrivate(keySpec);
    }

    public static String toPem(String type, byte[] bytes) {
        StringWriter sw = new StringWriter();
        try (PemWriter writer = new PemWriter(sw)) {
            writer.writeObject(new PemObject(type, bytes));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sw.toString();
    }

    public static String getCertificate(Certificate cert) {
        return encodePem(cert);
    }
}
