# Spring 框架 Interceptor 拦截器

## 一、Interceptor 拦截器是什么

Interceptor 拦截器，是一种动态拦截方法，调用的机制，类似于过滤器。

Interceptor 拦截器，是 Spring 框架提供的，用来动态拦截 Controller 控制器方法的执行。

在拦截器中，通常也做一些通用性的操作，比如：

1. 可以通过拦截器，来拦截前端发起的请求，将登录校验的逻辑，全部编写在拦截器当中；
2. 在校验的过程当中：
   - 请求（携带）JWT 令牌，且合法，可直接放行，去访问 Web 服务器中的资源。
   - 请求未携带 JWT 令牌，或是非法令牌，就可以直接给前端响应未登录的错误信息。

## 二、Interceptor 拦截器的使用

拦截器的使用步骤：

1. 定义拦截器，实现 `HandlerInterceptor` 接口。

2. 注册配置拦截器。

定义拦截器类 `LoginInterceptor`，实现 `HandlerInterceptor` 接口，重写其中所有方法。

demo-project/javaweb-practise/src/main/java/com/kkcf/interceptor/LoginInterceptor.java

```java
package com.kkcf.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    /**
     * 此方法用于：在目标资源方法前运行，返回 true 则放行，返回 false 则拦截，不放行
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("preHandle……");
        //return HandlerInterceptor.super.preHandle(request, response, handler);
        return true; // 直接放行
    }

    /**
     * 此方法用于：在目标资源方法后运行，可以做资源清理工作
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle……");
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 此方法用于：在资源处理完成后运行，可以做资源清理工作
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("afterCompletion……");
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
```

- `preHandle` 方法：在目标资源方法执行前执行。 返回 `true`  则放行    返回 `false` 则不放行。
- `postHandle` 方法：在目标资源方法执行后执行。
- `afterCompletion` 方法：在视图渲染完毕后执行，最后执行。

### 1.@Configuration 注解

定义配置类 `WebConfig` 实现 `WebMvcConfigurer` 接口。

- 在该类上方加上 `@Configuration` 注解，表示注册配置类。
- 实现接口中的 `addInterceptors` 方法。

demo-project/javaweb-practise/src/main/java/com/kkcf/config/WebConfig.java

```java
package com.kkcf.config;

import com.kkcf.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 表示配置类
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**"); // 配置拦截器拦截的资源
    }
}
```

## 三、Interceptor 拦截器路径配置

指定拦截器的拦截路径：

- 调用 `addPathPatterns("要拦截路径")` 方法，指定要拦截哪些资源。
- 调用 `excludePathPatterns("不拦截路径")` 方法，指定哪些资源不需要拦截。

在拦截器中除了可以设置`/**` 拦截所有资源外，还有一些常见拦截路径设置：

| 拦截路径  | 含义                  | 举例                                                 |
| --------- | --------------------- | ---------------------------------------------------- |
| /*        | 一级路径              | 能匹配 /depts，/emps，/login，…；不能匹配 /depts/1   |
| /**       | 任意级路径            | 能匹配 /depts，/depts/1，/depts/1/2，…               |
| /depts/*  | /depts 下的一级路径   | 能匹配 /depts/1；不能匹配 /depts/1/2，/depts，…      |
| /depts/** | /depts 下的任意级路径 | 能匹配 /depts，/depts/1，/depts/1/2；不能匹配/emps/1 |

demo-project/javaweb-practise/src/main/java/com/kkcf/config/WebConfig.java

```java
package com.kkcf.config;

import com.kkcf.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 表示配置类
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        // 配置拦截器拦截的资源
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns("/login");
    }
}
```

## 四、Interceptor 拦截器执行流程

过滤器、拦截器的执行时机如下图所示：

![过滤器-拦截器执行流程](NoteAssets/过滤器-拦截器执行流程.png)

当前是基于 Spring Boot 开发 Web 服务器，

1. Filter 过滤器，会先拦截到请求。执行放行前的逻辑，再执行放行操作；
2. 请求进入到了 Spring 环境中处理，准备访问 Controller 控制器中的方法。
3. Tomcat 并不识别 Controller 控制器，但它识别 Servlet 程序，所以在 Spring 的 Web 环境中，提供了一个非常核心的 `DispatcherServlet`（前端控制器），所有请求都会经它派发给 Controller。
4. Spring 的 Interceptor 拦截器，会在执行 Controller 方法之前，拦截请求，执行 `preHandle` 方法；该方法返回一个布尔类型的值：
   - 返回 true 则放行；
   - 返回 false 则不会放行（controller 中的方法也不会执行）。
5. Controller 中的方法，执行完毕后，再回过来执行 Interceptor 拦截器的 `postHandle`、`afterCompletion` 方法；
6. 然后再返回给 `DispatcherServlet`，
7. 最终再来执行 Filter 过滤器当中放行后的逻辑。
8. 执行完毕之后，最终给浏览器响应数据。

Filter 过滤器与 Interceptor 拦截器的区别，主要有两点：

- 接口规范不同：
  - Filter 过滤器，需要实现 Servlet 规范的 `Filter` 接口；
  - Interceptor 拦截器，需要实现 Spring 的 `HandlerInterceptor` 接口。

- 拦截范围不同：
  - Filter 过滤器，会拦截所有对资源的请求；
  - Interceptor 拦截器，只会拦截 Spring 环境中的资源请求。


## 五、Intercepter 拦截器登录校验实现

登录校验的业务逻辑，与之前 Filter 过滤器中的逻辑是完全一致的。

现在只需要把这个技术方案，由原来的 Filter 过滤器，换成 interceptor 拦截器。

demo-project/javaweb-practise/src/main/java/com/kkcf/interceptor/LoginInterceptor.java

```java
package com.kkcf.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.kkcf.pojo.Result;
import com.kkcf.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    /**
     * 此方法用于：目标资源方法前运行，返回 true 则放行，返回 false 则拦截，不放行
     *
     * @param req
     * @param res
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        System.out.println("preHandle……");

        // 1.获取请求 url
        String url = req.getRequestURL().toString();
        log.info("请求的 URL 地址是：{}", url);

        // 2.判断请求 url 中是否有 login，如果包含，说明是登录请求，可直接方向
        if (url.contains("login")) {
            log.info("登录请求，放行...");
            return true;
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
            return false;
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
            return false;
        }

        // 6.放行
        log.info("令牌合法，放行...");
        //return HandlerInterceptor.super.preHandle(request, response, handler);
        return true;
    }

    /**
     * 此方法用于：目标资源方法后运行，可以做资源清理工作
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle……");
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 此方法用于：资源处理完成后运行，可以做资源清理工作
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("afterCompletion……");
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
```

注册并配置拦截器：

demo-project/javaweb-practise/src/main/java/com/kkcf/config/WebConfig.java

```java
package com.kkcf.config;

import com.kkcf.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 表示配置类
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        // 配置拦截器拦截的资源
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login");
    }
}
```
