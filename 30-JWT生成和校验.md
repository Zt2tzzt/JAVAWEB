# JWT 生成和校验

在 JWT 登录认证的场景中，涉及到两步操作：

1. 在登录成功之后，要生成令牌。
2. 每一次请求当中，要接收令牌并对令牌进行校验。

基于 Java 代码，生成和校验 JWT 令牌。

## 一、JWT 生成

在 Maven 项目中，引入 JWT 依赖。

demo-project/javaweb-practise/pom.xml

```xml
<!-- JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.1</version>
</dependency>
```

在测试类中，测试 JWT 令牌的生成：

demo-project/javaweb-practise/src/test/java/com/kkcf/JavawebPractiseApplicationTests.java

```java
package com.kkcf;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
//@SpringBootTest
class JavawebPractiseApplicationTests {
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
                .signWith(SignatureAlgorithm.HS256, "kkcf") // 签名算法，密钥 kkcf
                .setClaims(claims) // 存放的数据
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 设置有效期为 1 小时
                .compact();

        System.out.println("JWT: " + jwt);
    }
}
```

> 在测试类中，注释掉上方的 `@SpringBootTest` 注解，以进行简单的单元测试，无需加载整个 Spring 环境。

运行测试方法，得到生成的令牌为

```sh
eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiemV0aWFuIiwiZXhwIjoxNzI3MzE2NTQ5LCJpZCI6MX0.7514DsLRwRK3Fm6D7Tqn97qNWZi_T9WKS9r8NzJPYoU
```

- 可发现 JWT 令牌,，通过英文的点（`.`），对第一、二、三部分进行分割；

在 JWT [官网](https://jwt.io/)，将生成的令牌放在 Encoded 位置，此时就会自动的将令牌解析如下：

HEADER

```json
{
  "alg": "HS256"
}
```

- 第一部分（HEADER）解析出来，可看到 JSON 格式的原始数据：使用的签名算法为 HS256。

PAYLOAD:DATA

```json
{
  "name": "zetian",
  "exp": 1727316549,
  "id": 1
}
```

- 第二个部分（PAYLOAD）解析出来，可看到是自定义的 JSON 格式数据，
- 还有一个 `exp` 表示设置的过期时间。

前两个部分是 Base64 编码，所以可以直接解码出来。

VERIFY SIGNATURE

```sh
HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),

) secret base64 encoded
```

最后一个部分不是 Base64 编码，而是经过签名算法计算出来的，所以最后一个部分不会解析。

## 二、JWT 解析

基于 Java 代码，来解析 JWT 令牌：

demo-project/javaweb-practise/src/test/java/com/kkcf/JavawebPractiseApplicationTests.java

```java
package com.kkcf;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
// @SpringBootTest
class JavawebPractiseApplicationTests {

    /**
     * 测试 JWT 令牌的解析
     */
    @Test
    public void testJWTparse() {
        Claims claims = Jwts.parser()
                .setSigningKey("kkcf") // 密钥
                .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiemV0aWFuIiwiZXhwIjoxNzI3MzE2NTQ5LCJpZCI6MX0.7514DsLRwRK3Fm6D7Tqn97qNWZi_T9WKS9r8NzJPYoU")
                .getBody();

        System.out.println("claims: " + claims);
    }
}
```

执行该测试方法，得到输出结果：

```sh
claims: {name=zetian, exp=1727411139, id=1}
```

- 可以看到自定义的数据 `id`、`name` 和 `exp` 过期时间；

在解析 JWT 令牌的过程中，没有出现异常，说明解析成功了。否则，说明解析失败，

解析异常的原因有：

- JWT 令牌过期；
- JWT 令牌被篡改。

只要解析出现异常了，这个令牌就是非法的。

## 三、JWT 案例练习

在案例中，使用 JWT 生成令牌；

在客户端（浏览器）发送登录请求并登录成功后，返回这个令牌。

编写工具类 `JwtUtil`，用于操作 JWT

demo-project/javaweb-practise/src/main/java/com/kkcf/utils/JwtUtil.java

```java
package com.kkcf.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;

public class JwtUtil {
    public static String sigkey = "kkcf"; // 盐
    public static Long expire = 1000 * 60 * 60L;

    /**
     * 此方法用于：生成 JWT
     *
     * @param claims 数据
     * @return String
     */
    public static String generateToken(HashMap<String, Object> claims) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, sigkey) // 签名算法，加盐
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
```

在 `LoginControllr` 控制器类中，为登录成功的用户，下发令牌。

demo-project/javaweb-practise/src/main/java/com/kkcf/controller/LoginController.java

```java
package com.kkcf.controller;

import com.kkcf.pojo.Emp;
import com.kkcf.pojo.Result;
import com.kkcf.service.EmpService;
import com.kkcf.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private EmpService empService;

    @PostMapping
    public Result<String> login(@RequestBody Emp emp) {
        log.info("员工登录，员工信息：{}", emp);
        Emp res = empService.loginEmp(emp);

        String jwt = JwtUtil.generateToken(new HashMap<>(Map.of(
                "id", res.getId(),
                "username", res.getUsername(),
                "name", res.getName()
        )));

        return res != null ? Result.success(jwt) : Result.error("登录失败");
    }
}
```

在接口测试工具中，发送 POST 请求 `http://localhost/login`，得到返回结果：

```json
{
    "code": 1,
    "msg": "success",
    "data": "eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoi6YeR5bq4IiwiaWQiOjEsImV4cCI6MTcyNzQwODQ1NiwidXNlcm5hbWUiOiJqaW55b25nIn0.MSA0tDhs-B9GxnSUhyhjHmAKldBfC9xJ9hEnWe4f81k"
}
```
