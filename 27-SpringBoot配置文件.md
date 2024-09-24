# Spring Boot 配置文件

Spring Boot 项目的默认配置文件是 application.properties。当程序在启动时，会默认读取配置文件。

## 一、@Value 注解

Spring Boot 提供了 @Value 注解，将配置文件中属性的值，映射到类中的属性。

@Value 注解，通常用于外部配置的属性注入，具体用法为：`@Value("${配置文件中的key}")`

demo-project/javaweb-practise/src/main/java/com/kkcf/utils/AliyunOSSProperties1.java

```java
package com.kkcf.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class AliyunOSSProperties1 {
    @Value("${aliyun.oss.endpoint}")
    public String endpoint;
    @Value("${aliyun.oss.accessKeyId}")
    public String accessKeyId;
    @Value("${aliyun.oss.accessKeySecret}")
    public String accessKeySecret;
    @Value("${aliyun.oss.bucketName}")
    public String bucketName;
}
```

在测试类中，获取配置文件中的属性值：

demo-project/javaweb-practise/src/test/java/com/kkcf/JavawebPractiseApplicationTests.java

```java
package com.kkcf;

import com.kkcf.utils.AliyunOSSProperties1;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class JavawebPractiseApplicationTests {
    @Autowired
    private AliyunOSSProperties1 aliyunOSSProperties1;

    @Test
    void contextLoads() {
        String endpoint1 = aliyunOSSProperties1.getEndpoint();
        log.info("endpoint1: {}", endpoint1);
    }
}
```

## 二、yml 配置文件

事实上，Spring Boot 项目可以使用多种配置文件：

- application.properties
- application.yml 或 application.yaml

它们格式的对比如下：

application.properties

```properties
server.port=8080
server.address=127.0.0.1
```

application.yml

```yaml
server:
  port: 8080
  address: 127.0.0.1
```

application.yaml

```yaml
server:
  port: 8080
  address: 127.0.0.1
```

XML、properties、yml 等常见配置文件格式的对比，如下图所示：

![常见配置文件格式对比](NoteAssets/常见配置文件格式对比.png)

Spring Boot 中，仅支持 properties、yml 格式的配置，现代企业开发中，基本都用 yml 格式进行配置。

可以看到，配置同样的数据信息，yml 格式的数据有以下特点：

- 容易阅读
- 容易与脚本语言交互
- 以数据为核心，重数据轻格式

## 三、yml 基本语法

yml 配置文件，基本语法有如下几点：

- 大小写敏感；
- 值前面必须有空格，作为分隔符；
- 缩进表示层级关系，不允许使用 Tab，只能用空格（IDEA 会自动将 Tab 转为空格）。
- 缩进的空格数不影响层级关系，只要相同层级的元素左侧对齐即可。
- `#` 表示注释，从该字符一直到行尾，都会被解析器忽略

## 四、 yml 数据格式

对象或 Map 集合

```yaml
# 定义对象 / Map 集合
users:
  name: "zhangsan"
  age: 18
  address: "北京"
```

数组或 List、Set 集合

```yaml
# 定义数组
hobby:
  - sing
  - dance
  - rap
  - basketball
```

## 五、在项目中使用 yml 配置文件

使用 application.yml 替换掉 application.properties 配置文件

原有的 application.properties 配置文件：

demo-project/javaweb-practise/src/main/resources/application.properties

```properties
spring.application.name=javaweb-practise

# ?????
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
# ????? url
spring.datasource.url=jdbc:mysql://localhost:3306/javawebdb
# ????????
spring.datasource.username=root
# ???????
spring.datasource.password=wee1219

# ?? MyBatis ??
mybatis.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
# ?? MyBatis ??????
mybatis.configuration.map-underscore-to-camel-case=true

# 单个文件上传大小，最大为 10M
spring.servlet.multipart.max-file-size=10MB
# 单个请求中上传的最大值为 100MB（一个请求中可能包含多个文件）
spring.servlet.multipart.max-request-size=100MB

# 阿里云你 OSS 服务配置
aliyun.oos.endpoint=https://oss-cn-shenzhen.aliyuncs.com
aliyun.oos.accessKeyId=xxxxxxx
aliyun.oos.accessKeySecret=xxxxxx
aliyun.oos.bucketName=xxxxxx
```

application.yml 配置文件：

demo-project/javaweb-practise/src/main/resources/application.yml

```yaml
spring:
  # 项目名称
  application:
    name: javaweb-practise

  # 数据库配置
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/javawebdb
    username: root
    password: wee1219

  # 文件上传配置
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB

# MyBatis 配置
mybatis:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    map-underscore-to-camel-case: true

aliyun:
  oos:
    endpoint: https://oss-cn-shenzhen.aliyuncs.com
    accessKeyId: xxxxxx
    accessKeySecret: xxxxxx
    bucketName: xxxxxx
```

## 六、@ConfigurationProperties 注解

使用 @Value 注解，进行配置文件属性注入有一个问题，那就是当属性较多是，写起来比较繁琐。

Spring 提供了一种简化方式，可以直接将配置文件中配置项的值，自动的注入到对象的属性中。有如下步骤：

1. 创建一个实现类，其中的属性名，与配置文件当中键名一致。
2. 实体类的属性，还需要提供 getter、setter方法（可使用 @Data 注解）。
3. 实体类要交给 Spring 的 IOC 容器管理，成为 IOC 容器当中的 bean 对象。
4. 在实体类上添加 `@ConfigurationProperties` 注解，并通过 `perfect` 属性，来指定配置参数项的前缀。

创建实体类 AliyunOSSProperties2。

demo-project/javaweb-practise/src/main/java/com/kkcf/utils/AliyunOSSProperties2.java

```java
package com.kkcf.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliyunOSSProperties2 {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
}
```

在测试类中测试：

```java
package com.kkcf;

import com.kkcf.utils.AliyunOSSProperties2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class JavawebPractiseApplicationTests {
    @Autowired
    private AliyunOSSProperties2 aliyunOSSProperties2;

    @Test
    void contextLoads() {
        String endpoint2 = aliyunOSSProperties2.getEndpoint();
        log.info("endpoint2: {}", endpoint2);
    }
}
```

为配合该实体类的属性，在 properties 或者 yml 配置文件中，提示应配置的属性，可以在 pom.xml 文件中，引入一下依赖：

这项依赖的作用，是自动识别 `@ConfigurationProperties` 注解标识的 bean 对象。

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
</dependency>
```

@ConfigurationProperties 注解与 @Value 注解的异同：

相同点：

- 都可用来注入配置文件中的属性。

不同点：

- @Value 注解，只能一个个的进行外部属性的注入。
- @ConfigurationProperties 注解，可以批量的将外部的属性配置注入到 bean对象的属性中。
