package com.kkcf;

import com.kkcf.utils.AliyunOSSProperties1;
import com.kkcf.utils.AliyunOSSProperties2;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest
class JavawebPractiseApplicationTests {
    @Autowired
    private AliyunOSSProperties1 aliyunOSSProperties1;
    @Autowired
    private AliyunOSSProperties2 aliyunOSSProperties2;

    @Test
    void contextLoads() {
        String endpoint1 = aliyunOSSProperties1.getEndpoint();
        log.info("endpoint1: {}", endpoint1);

        String endpoint2 = aliyunOSSProperties2.getEndpoint();
        log.info("endpoint2: {}", endpoint2);
    }

    /**
     * 测试 JWT 令牌的生成
     */
    @Test
    public void testJWTGenerate() {
        HashMap<String, Object> claims = new HashMap<>(Map.of(
                "id", 1,
                "name", "zetian"
        ));

        String jwt = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, "kkcf") // 签名算法
                .setClaims(claims) // 存放的数据
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 设置有效期为 1 小时
                .compact();

        System.out.println("JWT: " + jwt);
    }

    /**
     * 测试 JWT 令牌的解析
     */
    /*@Test
    public void testJWTparse(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey("kkcf")
                .parseClaimsJws(jwt)
                .getBody();

        System.out.println("claims: " + claims);
    }*/
}
