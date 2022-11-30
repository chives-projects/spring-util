package com.csc.common.utils.cryption;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

/**
 * @Description: JWT的简单加密
 * @Author: csc
 * @Create: 2022-11-24
 * @Version: 1.0
 */
public class JWTUtils {
    private volatile static JWTUtils instance = null;

    ////更安全，但是需要借助存储否则不适用分布式部署
    private final Key loginKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

//    //第三部分自定义校验字符串
//    public static final String CUSTOM_STR = "CUSTOM_STR";
//    //根据key生成密钥（会根据字节参数长度自动选择相应的 HMAC 算法）
//    private Key loginKey = Keys.hmacShaKeyFor(CUSTOM_STR.getBytes());

    private JWTUtils() {
    }

    private static JWTUtils getInstance() {
        if (instance == null) {
            synchronized (JWTUtils.class) {
                if (instance == null) {
                    instance = new JWTUtils();
                }
            }
        }
        return instance;
    }

    public static String encrypt(String str) {
        return Jwts.builder()
                .signWith(getInstance().loginKey)
//                .setHeaderParam(JwsHeader.KEY_ID,"userID")//请求头
//                .setClaims(new HashMap<>())//
                .setSubject(str)
//                .setExpiration(new Date(System.currentTimeMillis() + 5000))//有效时间
                .compact();//关闭
    }

    public static String decrypt(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getInstance().loginKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
//                .get("key").toString();
                .getSubject();
    }
}
