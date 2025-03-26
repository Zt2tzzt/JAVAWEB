package com.kkcf.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;

public class JwtUtil {
    public static String sigkey = "kkcf";
    public static Long expire = 1000 * 60 * 60L;

    /**
     * 此方法用于：生成 JWT
     *
     * @param claims 数据
     * @return String
     */
    public static String generateToken(HashMap<String, Object> claims) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, sigkey) // 签名算法
                .setClaims(claims) // 存放的数据
                .setExpiration(new Date(System.currentTimeMillis() + expire)) // 设置有效期为 1 小时
                .compact();
    }

    /**
     * 此方法用于：解析 JWT
     *
     * @param jwt JWT
     * @return Claims
     */
    public static Claims parseToken(String jwt) {

        return Jwts.parser()
                .setSigningKey(sigkey)
                .parseClaimsJws(jwt)
                .getBody();
    }
}
