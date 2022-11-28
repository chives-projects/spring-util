package com.csc.spring.common.utils.cryption;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: RSA 非对称加密解密工具，安全，速度慢，防止A发送到B的消息被其他人看到
 * @Author: csc
 * @Create: 2022-11-25
 * @Version: 1.0
 */
public class RSAUtils {
    public static final String ALGORITHM_NAME = "RSA";

    public static final String CHARSET = "UTF-8";
    /**
     * 公钥和私钥可以通过 createKeys 方法生成
     */
    public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCDQeamNSxR3MxfbDsPWnqWK5lU66ca70y4yIE-KbtJpXtPdL0HrwDX-N7ikQCGshARnG6IFZx4ByCEQ0AJBXwyDQwXHWw7hsSChlxKVfeSCT0Shb5qyc0O3wzhK2_jdKuNEL6y75-lPSCzvQ6p5JRRvtiv7mqqNDyATXK-EBATNwIDAQAB";
    public static final String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAINB5qY1LFHczF9sOw9aepYrmVTrpxrvTLjIgT4pu0mle090vQevANf43uKRAIayEBGcbogVnHgHIIRDQAkFfDINDBcdbDuGxIKGXEpV95IJPRKFvmrJzQ7fDOErb-N0q40QvrLvn6U9ILO9DqnklFG-2K_uaqo0PIBNcr4QEBM3AgMBAAECgYAQc3QLJOL3J6td0lzcocR3ieNf52E8WP3OsyKvvd9BbcI0xTbj_aUnbIPS9cWRHEQzUN3xpHPF_26t5RfjiZJ1LW3AdT_Lw-Cog1d6V-Z0aIT-uT2FNzzr8n-OJdeN1X2q91D8VnmV7l7AXrR1LGXT_HfY4srQrHuGljQyuKDsQQJBAMOFC0UnmjN21cpqhTv789bCpuRrkn-rsnNRAQGxKDRUcwOjr7E6LlbLVTBA95gbN4IjDVxDx9VOJawv3vr3_ccCQQCr3AJWy5xpSytU6K3e7dU03Y2IIjYTlcLp6RvlgAjykI7k5wm-Pi6oWoUWqEz7SOhMq_5d7S5QVspGquAtuf8RAkEAw2jJJsi52Q3ABpoHpzfdMJtC6bCJrogPxikDdIl2wTZV7SE_i0W24pA-CAgmbSNmiu41gC1OH-YGyNzfYr1QKQJAAJUINP8PkpO8m9cF78FifYFan2FuSXayaFRv0cEzRo8SzNezgr93LP7RgJglWpyOnOPgQrUw-PiySrb9d2ov4QJAIbd1ywymYTudBUL72CIVv7LhPBtethNeO0iNhv9rSa5uk2qHzSag2P9bWr-t4vgBLFu22jXqHjiREadGcWGrCg";
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;
    /**
     * 长度为512-65536
     */
    private static final int KEY_SIZE = 1024;

    /**
     * 得到公钥
     *
     * @param publicKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 通过X509编码的Key指令获得公钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_NAME);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64URLSafe(publicKey));
        RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
        return key;
    }

    /**
     * 得到私钥
     *
     * @param privateKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 通过PKCS#8编码的Key指令获得私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_NAME);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64URLSafe(privateKey));
        RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
        return key;
    }

    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     */
    public static String publicEncrypt(String data, String publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET)));
//            return new String(cipher.doFinal(data.getBytes(CHARSET)));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 私钥解密
     *
     * @param data
     * @param privateKey
     */
    public static String privateDecrypt(String data, String privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_NAME);
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64URLSafe(data)), CHARSET);
//            return new String(cipher.doFinal(data.getBytes()), CHARSET);
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * rsa切割解码  , ENCRYPT_MODE,加密数据   ,DECRYPT_MODE,解密数据
     * RSA加密明文最大长度117字节，解密要求密文最大长度为128字节，所以在加密和解密的过程中需要分块进行。 RSA加密对明文的长度是有限制的
     */
    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas) {
        int maxBlock = opmode == Cipher.DECRYPT_MODE ? MAX_DECRYPT_BLOCK : MAX_ENCRYPT_BLOCK;

        byte[] buff, result;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int offSet = 0;
            while (datas.length > offSet) {
                if (datas.length - offSet > maxBlock) {
                    //可以调用以下的doFinal（）方法完成加密或解密数据：
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                } else {
                    buff = cipher.doFinal(datas, offSet, datas.length - offSet);
                }
                out.write(buff, 0, buff.length);
                offSet += maxBlock;
            }
            result = out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
        }
        return result;
    }

    public static void main(String[] args) {
        Map<String, String> keyMap = new RSAUtils().createKeys();
        String publicKey = keyMap.get("publicKey");
        String privateKey = keyMap.get("privateKey");
        System.out.println("公钥: \n\r" + publicKey);
        System.out.println("私钥： \n\r" + privateKey);

//        System.out.println("公钥加密——私钥解密:A->B发送消息");
//        String str = "站在大明门前守卫的禁卫军，事先没有接到\n" + "有关的命令，但看到大批盛装的官员来临，也就";
//        System.out.println("\r明文：\r\n" + str);
//        String encodedData = RSAUtil.publicEncrypt(str, PUBLIC_KEY);  //传入明文和公钥加密,得到密文
//        System.out.println("密文：\r\n" + encodedData);
//        String decodedData = RSAUtil.privateDecrypt(encodedData, PRIVATE_KEY); //传入密文和私钥,得到明文
//        System.out.println("解密后文字: \r\n" + decodedData);
    }

    /**
     * 创建公钥和私钥，
     */
    private Map<String, String> createKeys() {
        return createKeys(KEY_SIZE);
    }

    public Map<String, String> createKeys(int keySize) {
        // 为RSA算法创建一个KeyPairGenerator对象
        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance(ALGORITHM_NAME);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm-->[" + ALGORITHM_NAME + "]");
        }

        // 初始化KeyPairGenerator对象,密钥长度
        kpg.initialize(keySize);
        // 生成密匙对
        KeyPair keyPair = kpg.generateKeyPair();
        // 得到公钥
        Key publicKey = keyPair.getPublic();
        String publicKeyStr = Base64.encodeBase64URLSafeString(publicKey.getEncoded());
        // 得到私钥
        Key privateKey = keyPair.getPrivate();
        String privateKeyStr = Base64.encodeBase64URLSafeString(privateKey.getEncoded());
        // map装载公钥和私钥
        Map<String, String> keyPairMap = new HashMap();
        keyPairMap.put("publicKey", publicKeyStr);
        keyPairMap.put("privateKey", privateKeyStr);
        // 返回map
        return keyPairMap;
    }


}

