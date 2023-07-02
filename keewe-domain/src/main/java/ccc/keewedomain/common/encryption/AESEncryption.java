package ccc.keewedomain.common.encryption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

@Component
public class AESEncryption {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String UNICODE_FORMAT = "UTF8";

    @Value("${encryption.key}")
    private String encryptionKey;

    @Value("${encryption.iv}")
    private String iv;

    public String encrypt(String value) throws Exception {
        Cipher c = Cipher.getInstance(ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(encryptionKey.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
        c.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);
        byte[] encValue = c.doFinal(value.getBytes(UNICODE_FORMAT));
        String encryptedValue = Base64.getEncoder().encodeToString(encValue);
        System.out.println(encryptedValue);
        return encryptedValue;
    }

    public String decrypt(String value) throws Exception {
        Cipher c = Cipher.getInstance(ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(encryptionKey.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
        c.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);
        byte[] decodedValue = Base64.getDecoder().decode(value);
        byte[] decValue = c.doFinal(decodedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    private Key generateKey() throws Exception {
        Key key = new SecretKeySpec(encryptionKey.getBytes(UNICODE_FORMAT), ALGORITHM);
        return key;
    }
}