package com.csc.spring.common.utils.cryption;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @description: md5进行hash计算
 * replace with DigestUtils
 * @Author: csc
 * @create: 2022/11/24
 */
public class Md5Utils {

    public static void main(String[] args) throws FileNotFoundException {
        String str = "站在大明门前守卫的禁卫军，事先没有接到\n" + "有关的命令，但看到大批盛装的官员来临，也就";
        System.out.println(md5DigestAsHex(str));
        System.out.println(computeMD5Hash(str));

        String path = "pom.xml";
        System.out.println(md5DigestAsHex(new FileInputStream((path))));
        System.out.println(computeMD5Hash(new File(path)));
    }

    public static String md5DigestAsHex(String src) {
        return DigestUtils.md5DigestAsHex(src.getBytes());
    }

    /**
     * @Description: 生成字符串的MD5 hash值
     * @Author: csc
     * @create: 2022/11/24
     */
    public static String computeMD5Hash(String input) {
        //参数校验
        if (null == input) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte[] digest = md.digest();
            BigInteger bi = new BigInteger(1, digest);
            String hashText = bi.toString(16);
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }
            return hashText;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String md5DigestAsHex(InputStream inputStream) {
        try {
            DigestUtils.md5DigestAsHex(inputStream);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return StringUtils.EMPTY;
    }

    /**
     * @Description: 计算文件的 md5 hash值
     * @Author: csc
     * @create: 2022/11/24
     */
    public static String computeMD5Hash(File file) {
        //摘要输入流
        DigestInputStream din = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            //第一个参数是一个输入流,第二个是要与此流关联的消息摘要
            din = new DigestInputStream(new BufferedInputStream(new FileInputStream(file)), md5);

            byte[] b = new byte[1024];
            while (din.read(b) != -1) ;
            byte[] digest = md5.digest();
            return DatatypeConverter.printHexBinary(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (din != null) {
                    din.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
