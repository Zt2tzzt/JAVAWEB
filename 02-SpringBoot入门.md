# Spring

## 一、Spring 框架

[Spring](https://spring.io)，是最受欢迎的 Java 框架，没有之一。Spring 发展到今天，已经形成了一种开发生态圈；

Spring 提供了若干个子项目，每个项目用于完成特定的功能；

在 Java 项目开发时，一般会偏向于选择一套 Spring 家族的技术，来解决对应领域的问题，

- 我们称这一套技术为 **Spring 全家桶**；
- Spring 框架之间整合方便，无缝衔接。

Spring 家族旗下最基础、最核心的，就是 Spring Framework 框架。直接基于它进行开发，存在两个问题：

- 配置繁琐；
- 入门难度大。

因此 Spring 官方推荐开发者，从另外一个项目开始学习，那就是目前最火爆的 Spring Boot。

Spring Boot 最大的特点有两个 ：

- 简化配置
- 快速开发

Spring Boot 可以非常快速的构建应用程序、简化开发、提高效率 。

Spring Boot 框架底层，仍然基于 Spring Frameworrk 实现。

## 二、Spring Boot 快速入门

需求：基于 Spring Boot 框架，开发一个 web 后端应用服务，

- 浏览器发起请求 `/hello` 后，给浏览器返回字符串 `“Hello Frog~”`。

实现步骤：

1. 创建 Spring Boot 项目；
2. 定义 `HelloController` 类，添加方法 `hello`，并添加注解；
3. 测试运行。

### 1.IDEA Spring Boot 项目创建

1. 进入创建项目窗口：File -> New -> Project；
2. 选择左侧 Spring Boot；
3. 填写右侧信息：
   - Language 设为 Java；
   - Type 设为 Maven；
   - Group 为组织名，比如设为 `com.kkcf`
   - artifact 为项目名，比如设为 `springboot-web-quickstart`；设置完成后，上方 name 会跟着修改。
   - Pacakge name 为要在项目中创建的包的层级，比如设为 `com.kkcf`
   - Java 为 Java 版本；比如设为 17；
   - Pacakging 为打包方式，比如设为 Jar；
4. 点击 Next，进入下一步；
5. 选择 Spring Boot 版本，默认是最新版本。
6. 在下方，勾选 Web -> Spring Web；
7. 点击 Create，联网创建项目；

生成的 pom.xml 文件如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.3.3</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.kkcf</groupId>
    <artifactId>springboot-web-quickstart</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>springboot-web-quickstart</name>
    <description>springboot-web-quickstart</description>
    <url/>
    <licenses>
        <license/>
    </licenses>
    <developers>
        <developer/>
    </developers>
    <scm>
        <connection/>
        <developerConnection/>
        <tag/>
        <url/>
    </scm>
    <properties>
        <java.version>17</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

- `<parent>` 标签，表示 maven 项目的父级项目。

`SpringbootWebQuickstartApplication` 是自动生成的类，用于作为项目入口，启动 Spring Boot 项目。

demo-project/springboot-web-quickstart/src/main/java/com/kkcf/SpringbootWebQuickstartApplication.java

```java
package com.kkcf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootWebQuickstartApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootWebQuickstartApplication.class, args);
    }
}
```

src/main/resource 目录下，创建了两个目录，和一个配置文件，结构如下：

├─📁 static/
├─📁 templates/
└─📄 application.properties

### 2.请求处理类创建

在 src/main/java/com.kkcf 目录下，创建一个目录 controller 目录，用于存放请求处理类。

创建一个控制器类 `HelloController`。用于请求处理：

demo-project/springboot-web-quickstart/src/main/java/com/kkcf/controller/HelloController.java

```java
package com.kkcf.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String hello() {
        return "Hello Frog";
    }
}
```

> IDEA 中，创建类时，连带创建目录，使用 `目录名.类名` 的写法创建。

#### 1.@RestController 注解

`@RestController` 注解，表示 Spring Boot 控制器类，用于 HTTP 请求处理；

#### 2.@RequestMapping 注解

`@RequestMapping` 注解，表示 Spring Boot 的请求处理方法。

### 3.Spring Boot Web 项目启动

执行 `SpringbootWebQuickstartApplication` 类的 `main` 方法，启动项目。

会占用一个 8080 端口。

在浏览器，访问 `http://localhost:8080/hello`，得到返回结果 `Hello Frog`。
