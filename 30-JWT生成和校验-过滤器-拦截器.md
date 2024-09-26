# JWT 生成和校验、过滤器、拦截器

在JWT登录认证的场景中我们发现，整个流程当中涉及到两步操作：

1. 在登录成功之后，要生成令牌。
2. 每一次请求当中，要接收令牌并对令牌进行校验。

稍后我们再来学习如何来生成jwt令牌，以及如何来校验jwt令牌。

JWT 依赖引入

```xml
<!-- JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.1</version>
</dependency>
```

在测试类中，测试 JWT 临牌的生成和解析：

```java
package com.kkcf;

import com.kkcf.utils.AliyunOSSProperties1;
import com.kkcf.utils.AliyunOSSProperties2;
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
                .signWith(SignatureAlgorithm.HS256, "kkcf") // 签名算法
                .setClaims(claims) // 存放的数据
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 设置有效期为 1 小时
                .compact();

        System.out.println("JWT: " + jwt);
    }

    /**
     * 测试 JWT 令牌的解析
     */
    @Test
    public void testJWTparse() {
        Claims claims = Jwts.parser()
                .setSigningKey("kkcf")
                .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiemV0aWFuIiwiZXhwIjoxNzI3MzE2NTQ5LCJpZCI6MX0.7514DsLRwRK3Fm6D7Tqn97qNWZi_T9WKS9r8NzJPYoU")
                .getBody();

        System.out.println("claims: " + claims);
    }
}
```



> 在测试类中，注释掉上方的 `@SpringBootTest` 注解，不用加载整个 Spring 环境，以进行简单的单元测试



JWT 解析过期令牌也会报错。

只要解析报错了，这个令牌就是非法的。



在案例中，使用 JWT 生成令牌，在客户端发送登录请求并登录成功后，返回这个令牌。

编写工具类 JwtUtil，用于操作 JWT

demo-project/javaweb-practise/src/main/java/com/kkcf/utils/JwtUtil.java

```java
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
```

在 LoginControllr 中，为登录成功的用户，下发令牌。

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

        return res != null ? Result.success(JwtUtil.generateToken(new HashMap<>(Map.of(
                "id", res.getId(),
                "username", res.getUsername(),
                "name", res.getName()
        )))) : Result.error("登录失败");
    }
} 
```



过滤器 Filter

过滤器是什么



过滤器的实现：

1. 定义 Filter，定义一个类，实现 `javax.servlet.Fileter` 接口。并重写其所有方法。
2. 配置 Filter，在其上加 @WebFilter 注解。配置拦截资源的路径。引导类（启动类）加上 @ServletComponentScan 注解
   - JavaWeb 三大组件，不属于 Spring Boor 组件，要加上该注解表示在当前项目中支持 Servlet 组件。
   - 注释掉 @WebFilter 注解，Filter 也就失效了。



JavaWeb 服务器启动时，会自动创建 Filter 过滤器对象；该对象创建完毕后，会自动调用 init 方法

- 通常会在 init 方法中，完成一些资源和环境的准备操作。

JavaWeb 服务器关闭时，会自动调用 destroy 方法，该方法只会被调用一次。

- 通常会在 destroy 方法中，做一些资源的释放。资源清理的工作。

init、destroy 方法不常用，Filter 接口中以提供了默认实现。

doFilter 是每次拦截到请求后，都会调用的方法。

demo-project/javaweb-practise/src/main/java/com/kkcf/filter/DemoFilter.java

```java
package com.kkcf.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class DemoFilter implements Filter {
    /**
     * 此方法用于：初始化拦截器，只会调用一次
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init 初始化方法执行了");
        Filter.super.init(filterConfig);
    }


    /**
     * 此方法用于：拦截请求，调用多次
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("拦截到了请求，放行前的逻辑……");
        // 放行
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("拦截到了请求，放行后的逻辑……");
    }

    /**
     * 此方法用于：销毁拦截器，只会调用一次
     */
    @Override
    public void destroy() {
        System.out.println("destroy 销毁方法执行了");
        Filter.super.destroy();
    }
}
```



引导（启动）类

demo-project/javaweb-practise/src/main/java/com/kkcf/JavawebPractiseApplication.java

```java
package com.kkcf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan // 当前项目开启了对 JavaWeb（Servlet）组件的支持。
@SpringBootApplication
public class JavawebPractiseApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavawebPractiseApplication.class, args);
    }
}
```



过滤器 Filter 详解

过滤器的执行流程

demo-project/javaweb-practise/src/main/java/com/kkcf/filter/AbcFilter.java

```java
package com.kkcf.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class AbcFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("abc，放行前的逻辑……");

        filterChain.doFilter(servletRequest, servletResponse);

        System.out.println("abc，放行后的逻辑……");
    }
}
```



过滤器拦截路径

三种拦截路径的配置方式。





过滤器链



在案例中，使用过滤器进行 token 校验

实现步骤：

demo-project/javaweb-practise/src/main/java/com/kkcf/filter/CheckLoginFilter.java

```java
package com.kkcf.filter;

import com.alibaba.fastjson.JSONObject;
import com.kkcf.pojo.Result;
import com.kkcf.utils.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/*")
public class CheckLoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        // 1.获取请求 url
        String url = req.getRequestURL().toString();
        log.info("请求的 URL 地址是：{}", url);

        // 2.判断请求 url 中是否有 login，如果包含，说明是登录请求，可直接方向
        if (url.contains("login")) {
            log.info("登录请求，放行...");
            filterChain.doFilter(req, res);
            return;
        }

        // 3.获取请求头中的令牌 (token)
        String token = req.getHeader("token");

        // 4.判断令牌是否存在。如果不存在，返回错误结果（未登录）
        if (!StringUtils.hasLength(token)) {
            log.info("请求头中 token 为空，返回未登录信息...");
            Result<String> result = Result.error("NOT_LOGIN");
            // 手动将 Java 实例对象，转为 JSON 格式的数据，使用 Alibaba 提供的 fastJSON 工具包。
            String jsonStr = JSONObject.toJSONString(result);
            // 返回 JSON 数据
            res.getWriter().write(jsonStr);
            return;
        }

        // 5.解析 token，如果解析失败，返回错误结果（未登录）
        try {
            JwtUtil.parseToken(token);
        } catch (Exception e) {
            log.info("解析令牌失败，返回未登录信息...");
            Result<String> result = Result.error("NOT_LOGIN");
            // 手动将 Java 实例对象，转为 JSON 格式的数据，使用 Alibaba 提供的 fastJSON 工具包。
            String jsonStr = JSONObject.toJSONString(result);
            // 返回 JSON 数据
            res.getWriter().write(jsonStr);
            return;
        }

        // 6.放行
        log.info("令牌合法，放行...");
        filterChain.doFilter(req, res);
    }
}
```



拦截器是什么