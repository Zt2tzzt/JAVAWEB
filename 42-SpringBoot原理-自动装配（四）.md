# Spring Boot 原理之自动装配自定义 starter

## 一、Spring Boot starter 是什么

starter 就是 Spring Boot 的起步依赖。在 Spring Boot 中，已经给我们提供了很多起步依赖；

然而，在实际的项目开发中，可能会用到很多第三方的技术（比如 MyBatis、PageHelper）；

并非所有第三方的技术，都给提供与 Spring Boot 整合的 starter 起步依赖，但是这些技术又非常的通用，在很多项目中都在使用。

所以在 Spring Boot 项目中，一般会将这些公共组件封装为 Spring Boot 的 starter。

## 二、Spring Boot starter 命名规范

Spring 的起步依赖，规范如下：

- Spring Boot 官方提供的起步依赖，都以 spring-boot-starter-xxx 命名；
- Spring Boot 非官方提供的起步依赖，都以 xxx-spring-boot-starter；
  - 比如：mybatis-spring-boot-starter、pageHelper-spring-boot-starter。

## 三、Spring Boot Starter 业务场景

前面使用的阿里云 OSS 对象存储服务，没有提供与 Spring Boot 整合的起步依赖，使用起来就比较繁琐，

- 需要引入对应的依赖；
- 还需要在配置文件当中进行配置，
- 还需要基于官方 SDK 示例，来改造对应的工具类；
- 在项目当中才可以正常使用。

可以自定义一些公共组件，提前把需要的 Bean 自动配置好。在项目中，将组件对应的坐标引入进来就可直接使用。

在 Spring Boot 项目中，一般都会将这些公共组件，封装为 Spring Boot 中的 starter，也就是起步依赖。

Spring Boot starter 起步依赖中，通常依赖了 autoconfigure 自动配置依赖。用于注入依赖中的 Bean 对象。它们的关系如下：

![自定义starter起步依赖](NoteAssets/自定义starter起步依赖.png)

Mybatis 依赖，提供了配置类，并且也提供了 Spring Boot 项目启动时，会自动读取的配置文件 `META-INF.spring.org.springframework.boot.autoconfigure.AutoConfiguration.imports` 其中声明了配置类的全限定名，用于生成相关 Bean 对象，并注册到 IOC 容器中。

开发者可以直接在 Spring Boot 程序中，使用 Mybatis 自动配置的 Bean 对象。

## 四、Spring Boot Starter 模块规范

在自定义一个起步依赖 starter 的时候，按照规范，需要定义两个模块（Module）：

1. starter 模块，利用 Maven 的依赖传递特性，进行依赖管理，会把程序开发所需要的依赖，都引入到项目中；
2. autoconfigure 模块，用于自动配置；

在项目中，只需要引入一个起步依赖（starter 模块）即可，它会将 autoconfigure 自动配置的依赖传递进来。

## 五、Spring Boot Starter 自定义案例

接下来完成一个自定义 starter 案例。

需求：自定义 aliyun-oss-spring-boot-starter，完成阿里云 OSS 操作工具类 AliyunOSSUtils 的自动配置。

目标：引入 aliyun-oss-spring-boot-starter 起步依赖后，注入 AliyunOSSUtils 的 Bean 对象直接使用阿里云 OSS SDK 功能。

原项目中，阿里云 OSS 的使用。



要自定义 starter，主要做两件事；

- 自动配置依赖 autoconfigure，定义要加载的配置类、 Bean 对象
- 在 starter 中，引入依赖。再引入 autoconfigure；



需求：自定义 aliyun-oss-spring-boot-starter，完成阿里云 OSS 操作工具类 AliyunOSSUtils 的自动配置。

目标；引入自定义的起步依赖后，要想使用阿里云 OSS，只需注入 AliyunOSSUtils 的 Bean 对象，直接使用即可。

实现步骤：

1. 创建 aliyun-oss-spring-boot-starter 模块；
2. 创建 aliyun-oss-spring-boot-autoconfigure 模块，在 starter 中引入该模块。
3. 在 aliyun-oss-spring-boot-autoconfigure 模块中的定义自动配置的功能。并定义自动配置文件 META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports



在 IDEA 中，创建第一个 Maven 模块。

- Project Structure -> Modules -> + 号 -> New Module
- Group 设为 com.aliyun.oss
- Artifact 设为 aliyun-oss-spring-boot-starter
- Package Name 设为 com.aliyun.oss
- location 设为 D:\Workshop\tutorial\JAVAWEB\demo-project

创建项目；

在 pom.xml 文件中，删除掉描述信息：

```xml
<name>aliyun-oss-spring-boot-starter</name>
<description>aliyun-oss-spring-boot-starter</description>
```

删除掉单元测试依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

删除掉 maven 插件。

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

starter 项目，仅进行依赖管理，所以将项目下的所有内容都删掉，只保留 pom.xml、aliyun-oss-spring-boot-starter.iml 两个文件

> 如果没有 aliyun-oss-spring-boot-starter.iml 文件，在项目根目录下，使用命令生成：
>
> ```shell
> mvn idea:module
> ```



在 IDEA 中，创建第二个 Maven 模块：

- Project Structure -> Modules -> + 号 -> New Module
- Group 设为 com.aliyun.oss
- Artifact 设为 aliyun-oss-spring-boot-autoconfigure
- Package Name 设为 com.aliyun.oss
- location 设为 D:\Workshop\tutorial\JAVAWEB\demo-project

创建项目

在 pom.xml 文件中，删除掉描述信息：

```xml
<name>aliyun-oss-spring-boot-autoconfigure</name>
<description>aliyun-oss-spring-boot-autoconfigure</description>
```

删除单元测试的依赖：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

删除掉 maven 插件。

```xml
<build>
    <plugins>
       <plugin>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-maven-plugin</artifactId>
       </plugin>
    </plugins>
</build>
```

将项目下的所有内容都删掉，只保留 pom.xml、aliyun-oss-spring-boot-autoconfigure.iml 两个文件和 src 目录。

在 src 目录中，删除启动类，和测试类。



在 aliyun-oss-spring-boot-starter 项目的 pom.xml 文件中，引入 aliyun-oss-spring-boot-autoconfigure 的依赖

demo-project/aliyun-oss-spring-boot-starter/pom.xml

```xml
<dependency>
    <groupId>com.aliyun.oss</groupId>
    <artifactId>aliyun-oss-spring-boot-autoconfigure</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

将 AliyunOSSProperties2 类，复制到 aliyun-oss-spring-boot-autoconfigure 的 com.aliyun.oss 包中，解决其中的报错：

demo-project/aliyun-oss-spring-boot-autoconfigure/src/main/java/com/aliyun/oss/AliyunOSSProperties2.java

```java
package com.aliyun.oss;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliyunOSSProperties2 {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
}
```

将 AliyunOSSUtil 类，复制到 aliyun-oss-spring-boot-autoconfigure 的 com.aliyun.oss 包中，解决其中的报错

demo-project/aliyun-oss-spring-boot-autoconfigure/src/main/java/com/aliyun/oss/AliyunOSSUtil.java



在 pom.xml 文件中，引入 spring boot 开发的 web 依赖，以及阿里云 OSS 的相关依赖。

demo-project/aliyun-oss-spring-boot-autoconfigure/pom.xml

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- 阿里云 OSS SDK -->
<dependency>
    <groupId>com.aliyun.oss</groupId>
    <artifactId>aliyun-sdk-oss</artifactId>
    <version>3.17.4</version>
</dependency>
<dependency>
    <groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
    <version>2.3.1</version>
</dependency>
<dependency>
    <groupId>javax.activation</groupId>
    <artifactId>activation</artifactId>
    <version>1.1.1</version>
</dependency>
```



删掉 AliyunOSSUtil 、AliyunOSSProperties2 类上的 @Component 注解。因为该依赖中不会再用 Spring 组件扫描。

AliyunOSSUtil  类中，@Autowired 注解的依赖注入，也删掉



定义一个自动配置类。

AliyunOSSProperties2 类上 @ConfigurationProperties 注解，用于自动注入 application.yml 配置文件中的属性。

在 AliyunOSSUtil 中，可使用 @EnableConfigurationProperties 注解，注入。

