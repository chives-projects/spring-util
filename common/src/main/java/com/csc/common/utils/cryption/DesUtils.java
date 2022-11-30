package com.csc.common.utils.cryption;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.util.Base64;

/**
 * @Description: DES加密解密工具，对称加密，速度快
 * 可以采用公开密钥体制RSA加密传输对称密码算法密钥（保证密钥的安全转移），对称密码体制DES加密通信的内容（提高加密速度）
 * A生成 publicKey 和 privateKey ，使用privateKey对DES的密钥加密，将加密后的文本发送到B，
 * B使用 publicKey加密文本得到DES的密钥，之后就可以通过DES快速通信
 * B解密得到密钥和直接把密钥发送给B区别
 * @Author: csc
 * @Create: 2022-11-25
 * @Version: 1.0
 */
public class DesUtils {
    /**
     * 密钥，长度大于等于8
     */
    private static final String SECRET_KEY = "efghijkl";
    /**
     * 偏移变量，固定占8位字节
     */
    private final static String IV_PARAMETER = "abcdefgh";
    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "DES";
    /**
     * 加密/解密算法-工作模式-填充模式
     */
    private static final String CIPHER_ALGORITHM = "DES/CBC/PKCS5Padding";
    /**
     * 默认编码
     */
    private static final String CHARSET = "utf-8";

    public static String encrypt(String content, String secretKey) {
        try {
            SecretKey key = getSecretKey(secretKey);

            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET)));
            byte[] result = cipher.doFinal(content.getBytes(CHARSET));
            return new String(Base64.getEncoder().encode(result), CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("exception:" + e.toString());
        }
        return null;
    }

    public static String DES_CBC_Decrypt(String content, String secretKey) {
        try {
            SecretKey key = getSecretKey(secretKey);

            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET)));
            byte[] result = cipher.doFinal(Base64.getDecoder().decode(content.getBytes(CHARSET)));
            return new String(result, CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("exception:" + e.toString());
        }
        return null;
    }

    private static SecretKey getSecretKey(String secretKey) throws Exception {
        DESKeySpec keySpec = new DESKeySpec(secretKey.getBytes(CHARSET));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        return keyFactory.generateSecret(keySpec);
    }

    public static void main(String[] args) {
        String content = "站在大明门前守卫的禁卫军，事先没有接到\n" + "有关的命令，但看到大批盛装的官员来临，也就";
        String encrypt = encrypt(content, SECRET_KEY);
        System.out.println("加密后：" + encrypt);
        String decrypt = DES_CBC_Decrypt(encrypt, SECRET_KEY);
        System.out.println("解密后：" + decrypt);
    }
}
