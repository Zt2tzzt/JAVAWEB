# Spring Boot 原理

Spring 家族的所有框架，都基于基础框架 Spring Framework。

直接基于 Spring FrameWork 框架进行开发， 会比较繁琐，主要体现在：

- pom.xml 依赖配置会比较繁琐。要去找所需要的依赖，和与它配套的依赖，以及对应的版本。否则，就会出现版本冲突问题。
- Spring 配置文件中，要做大量的配置。

所以 Spring 官方，在 Spring 4.0 版本时，推出了一个全新的框架 Spring Boot，来简化基于 Spring 框架的开发。



Spring Boot 框架，之所以简单、快捷，是因为底层提供了两个重要的机制：

- 起步依赖；简化 pom.xom 文件中依赖的配置。
- 自动配置。简化 Bean 的声明、配置……



Spring Boot 起步依赖的原理

Spring Boot 框架，Web 开发的起步依赖：spring-boot-starter-web，会利用 **Maven 的依赖传递**，将相关的依赖（以及对应的版本号）全部引入进来。



Spring Boot 的自动配置原理

Spring 容器自动启动后，一些配置类，Bean 对象，就自动存入到了 IOC 容器中。不需要手动去声明；

从而简化了开发，省去了繁琐的配置操作。



在 IDEA 中，查看项目启动后，查看 IOC 容器（Spring 容器）管理的 Bean 对象。

- Run -> Actuato -> Bean

在其中 gsonAutoConfig 中，可以看到管理了 gson Bean 对象（这是 Google 提供的，用于将 Java 对象转为 Json 对象的包）

在测试类中，使用 gson Bean 对象。

demo-project/javaweb-practise/src/test/java/com/kkcf/AutoConfigurationTest.java

```java
package com.kkcf;

import com.google.gson.Gson;
import com.kkcf.pojo.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AutoConfigurationTest {
    @Autowired
    private Gson gson;

    @Test
    public void testGson() {
        String jsonStr = gson.toJson(Result.success("哈哈"));
        System.out.println(jsonStr);
    }
}
```



Spring Boot 自动配置，就是当 Spring 项目启动后，一些配置类的 Bean 对象，就自动存入到 IOC 容器中；不需要手动去声明，从而简化了开发，省去了繁琐的配置操作；