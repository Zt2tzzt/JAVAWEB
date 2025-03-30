# Spring Boot 原理之自动装配自定义 starter

## 一、Spring Boot starter 是什么

starter 就是 Spring Boot 的**起步依赖**。

Spring Boot 官方，提供了很多起步依赖；

然而，在实际的项目开发中，可能会用到很多第三方的技术（比如 MyBatis、PageHelper）；

并非所有第三方的技术，都提供与 Spring Boot 整合的 starter 起步依赖，但是这些技术又非常的通用，在很多项目中都在使用。

所以一般会将这些公共组件封装为 Spring Boot 的 starter 起步依赖。

## 二、Spring Boot starter 命名规范

Spring 的起步依赖，规范如下：

- Spring Boot 官方提供的起步依赖，都以 spring-boot-starter-xxx 命名；
- Spring Boot 非官方提供的起步依赖，都以 xxx-spring-boot-starter 命名；
  - 比如：mybatis-spring-boot-starter、pageHelper-spring-boot-starter……

## 三、Spring Boot starter 业务场景

前面使用的阿里云 OSS 对象存储服务，没有提供与 Spring Boot 整合的起步依赖，使用起来有如下几步，比较繁琐，

1. 引入对应的依赖；
2. 在配置文件当中进行配置；
3. 基于官方 SDK 示例，来改造对应的工具类；
4. 在项目中正常使用。

在 Spring Boot 项目中，一般都会将这些公共组件，封装为 Spring Boot 中的 starter，也就是起步依赖。

- 其中自定义一些公共组件，提前把需要的 Bean 自动配置好；

在项目中，将组件对应的坐标，引入进来就可直接使用。

Spring Boot starter 起步依赖中，通常依赖了 autoconfigure 自动配置依赖。用于注入依赖中的 Bean 对象。

它们的关系如下：

![自定义starter起步依赖](NoteAssets/自定义starter起步依赖.png)

Mybatis 依赖，提供了配置类，并且也提供了 Spring Boot 项目启动时，会自动读取到配置文件 `META-INF.spring.org.springframework.boot.autoconfigure.AutoConfiguration.imports`，

其中声明了配置类的全限定名，用于生成相关 Bean 对象，并注册到 IOC 容器中。

开发者可以直接在 Spring Boot 程序中，使用 MyBatis 自动配置的 Bean 对象。

## 四、Spring Boot starter 模块规范

在自定义一个起步依赖 starter 的时候，按照规范，需要定义两个模块（Module）：

1. starter 模块，利用 Maven 的**依赖传递**特性，进行依赖管理，会把程序开发所需要的依赖，都引入到项目中；
2. autoconfigure 模块，用于自动配置；

在项目中，只需要引入一个起步依赖（starter 模块）即可，它会将 autoconfigure 自动配置的依赖传递进来。

## 五、Spring Boot Starter 自定义案例

接下来，完成一个自定义 starter 案例。

需求：自定义 aliyun-oss-spring-boot-starte 依赖，完成阿里云 OSS 操作工具类 `AliyunOSSUtils` 的自动配置。

目标：引入 aliyun-oss-spring-boot-starter 起步依赖后，直接注入 `AliyunOSSUtils` 的 Bean 对象，就可使用阿里云 OSS SDK 功能。

### 1.阿里云 OSS 在原项目使用

原项目中，阿里云 OSS 的使用。

#### 1.原项目配置文件

demo-project/javaweb-practise/src/main/resources/application.yml

```yaml
aliyun:
  oss:
    endpoint: https://oss-cn-shenzhen.aliyuncs.com
    accessKeyId: xxxxxx
    accessKeySecret: xxxxxx
    bucketName: zetian-bucket
```

#### 2.原项目 AliyunOSSProperties2 类

`AliyunOSSProperties2` 类，用于从配置文件中，加载配置的属性：

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

#### 3.原项目 AliyunOSSUtil2 类

`AliyunOSSUtil2` 工具类

demo-project/javaweb-practise/src/main/java/com/kkcf/utils/AliyunOSSUtil2.java

```java
package com.kkcf.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
public class AliyunOSSUtil2 {
    @Autowired
    private AliyunOSSProperties2 aliOSSProperties;

    /**
     * 实现上传图片到OSS
     */
    public String upload(MultipartFile multipartFile) throws IOException {
        // 获取上传的文件的输入流
        InputStream inputStream = multipartFile.getInputStream();

        // 避免文件覆盖
        String originalFilename = multipartFile.getOriginalFilename();
        String fileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));

        // 上传文件到 OSS
        OSS ossClient = new OSSClientBuilder().build(aliOSSProperties.getEndpoint(),
                aliOSSProperties.getAccessKeyId(), aliOSSProperties.getAccessKeySecret());
        ossClient.putObject(aliOSSProperties.getBucketName(), fileName, inputStream);

        // 文件访问路径
        String url = aliOSSProperties.getEndpoint().split("//")[0] + "//" + aliOSSProperties.getBucketName() + "." + aliOSSProperties.getEndpoint().split("//")[1] + "/" + fileName;

        // 关闭ossClient
        ossClient.shutdown();
        return url;// 把上传到oss的路径返回
    }
}
```

在项目当中，要使用阿里云 OSS，就可以注入 `AliyunOSSUtil2` 工具类的 Bean 对象，来进行文件上传。但这种方式其实是比较繁琐的。

现在使用阿里云 OSS，姑且需要做这么几步，那么在开发其它项目时，也要做这几步；团队中其它成员使用阿里云 OSS，步骤也是一样的。

所以，可以制作一个公共组件（自定义 starter）；将来要使用阿里云 OSS 进行文件上传，只需要将起步依赖引入进来之后，就可以直接注入 `AliyunOSSUtil2` 的 Bean 对象使用了。

### 2.阿里云 OSS 自定义 starter

具体的实现步骤：

1. 创建自定义 starter 模块（进行依赖管理）；
   - 把阿里云 OSS 所有的依赖，统一管理起来。
2. 创建 autoconfigure 模块；
   - 在 starter 模块中引入 autoconfigure模块；
   - 别的项目使用时，只需要引入 starter 起步依赖即可。
3. 在 autoconfigure 中，完成自动配置；
   1. 定义一个自动配置类，在自动配置类中将所要配置的 Bean 都提前配置好；
   2. 定义配置文件  `META-INF.spring.org.springframework.boot.autoconfigure.AutoConfiguration.imports`，把自动配置类的全类名，定义在配置文件中。

下面按照步骤，来实现自定义 starter。

#### 1.starter 模块创建

在 IDEA 中，创建一个 Maven 模块（Module）。

- Project Structure -> Modules -> + 号 -> New Module
- Group 设为 com.aliyun.oss
- Artifact 设为 aliyun-oss-spring-boot-starter
- Package Name 设为 com.aliyun.oss
- location 设为 D:\Workshop\tutorial\JAVAWEB\demo-project（本地项目存放位置）

点击“create”，创建项目；在创建的模块中，删除掉一些多余的配置：

在 pom.xml 文件中：

删除掉以下描述信息：

```xml
<name>aliyun-oss-spring-boot-starter</name>
<description>aliyun-oss-spring-boot-starter</description>
```

删除掉以下单元测试依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

删除掉以下 maven 插件。

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

删除多余内容后，pom.xml 文件如下：

demo-project/aliyun-oss-spring-boot-starter/pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.3.4</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.aliyun.oss</groupId>
    <artifactId>aliyun-oss-spring-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>

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
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
    </dependencies>
</project>
```

starter 项目，仅进行依赖管理，所以将项目下的所有内容，都删掉；

只保留 pom.xml、aliyun-oss-spring-boot-starter.iml 两个文件

> IDEA 2023 及以上版本，不需要生成 aliyun-oss-spring-boot-starter.iml  文件。
>
> 如果没有该文件，在项目根目录下，使用命令生成：
>
> ```shell
> mvn idea:module
> ```

#### 2.autoconfigure 模块创建

在 IDEA 中，创建第二个 Maven 模块：

- Project Structure -> Modules -> + 号 -> New Module
- Group 设为 com.aliyun.oss
- Artifact 设为 aliyun-oss-spring-boot-autoconfigure
- Package Name 设为 com.aliyun.oss
- location 设为 D:\Workshop\tutorial\JAVAWEB\demo-project

点击“create”创建项目，在创建的模块中，删除掉一些多余的配置：

在 pom.xml 文件中：

删除掉描述信息：

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

删除多余内容后，pom.xml 文件如下：

demo-project/aliyun-oss-spring-boot-autoconfigure/pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
  
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.3.4</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
  
    <groupId>com.aliyun.oss</groupId>
    <artifactId>aliyun-oss-spring-boot-autoconfigure</artifactId>
    <version>0.0.1-SNAPSHOT</version>
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
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
    </dependencies>
</project>
```

将项目下的所有内容都删掉，只保留 pom.xml、aliyun-oss-spring-boot-autoconfigure.iml 两个文件，和 src 目录。

在 src 目录中，删除启动类，和测试类。

#### 3.starter 模块引入 autoconfigure 模块

在 aliyun-oss-spring-boot-starter 模块的 pom.xml 文件中，引入 aliyun-oss-spring-boot-autoconfigure 的依赖

demo-project/aliyun-oss-spring-boot-starter/pom.xml

```xml
<dependency>
    <groupId>com.aliyun.oss</groupId>
    <artifactId>aliyun-oss-spring-boot-autoconfigure</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

#### 4.autoconfigure 模块自动配置

##### 1.AliyunOSSProperties2 类添加并修改

`AliyunOSSProperties2` 类，用于加载配置文件中的属性。

将 `AliyunOSSProperties2` 类，复制到 aliyun-oss-spring-boot-autoconfigure 的 com.aliyun.oss 包中，解决其中的报错：

- 因为该模块中，没有引入 Lombok 依赖，所以要删除类上的 `@Data` 注解；并为类中的属性添加 getter、setter 方法。

`AliyunOSSProperties2` 类上删掉 `@Component` 注解。

- 因为该依赖引入到的 Spring Boot 项目中，不会去扫描 com.aliyun.oss 这个包；
- 所以这个包里类上的 `@Component` 及其衍生注解也就失去了意义。

此时 `AliyunOSSProperties2` 类上 `@ConfigurationProperties(prefix = "aliyun.oss")` 注解，报红色错误，暂时先不管；

- 后续定义的 `AliOSSAutoConfiguration` 自动配置类，为它加上 `@EnableConfigurationProperties(AliyunOSSProperties2.class)` 注解，可消除错误。

demo-project/aliyun-oss-spring-boot-autoconfigure/src/main/java/com/aliyun/oss/AliyunOSSProperties2.java

```java
package com.aliyun.oss;

import org.springframework.boot.context.properties.ConfigurationProperties;

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

##### 2.AliyunOSSUtil2 类添加并修改

将 `AliyunOSSUtil2` 类，复制到 aliyun-oss-spring-boot-autoconfigure 的 com.aliyun.oss 包中

`AliyunOSSUtil2` 类上，删掉 `@Component` 注解。

- 因为该依赖引入到的 Spring Boot 项目中，不会去扫描 com.aliyun.oss 这个包，所以这个包里所有类上的 `@Component` 及其衍生注解，也就失去了意义。

`AliyunOSSUtil2` 类中，用于依赖注入的 `@Autowired` 注解，也删掉。

- `@Autowired` 注解标注了 `private AliyunOSSProperties2 aliOSSProperties;` 属性，该注解删掉后，要为该属性生成 getter、setter 方法，以便后续通过 setter 方法设值。

demo-project/aliyun-oss-spring-boot-autoconfigure/src/main/java/com/aliyun/oss/AliyunOSSUtil2.java

```java
package com.aliyun.oss;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class AliyunOSSUtil2 {
    private AliyunOSSProperties2 aliOSSProperties;

    public AliyunOSSProperties2 getAliOSSProperties() {
        return aliOSSProperties;
    }

    public void setAliOSSProperties(AliyunOSSProperties2 aliOSSProperties) {
        this.aliOSSProperties = aliOSSProperties;
    }

    /**
     * 实现上传图片到OSS
     */
    public String upload(MultipartFile multipartFile) throws IOException {
        // 获取上传的文件的输入流
        InputStream inputStream = multipartFile.getInputStream();

        // 避免文件覆盖
        String originalFilename = multipartFile.getOriginalFilename();
        String fileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));

        // 上传文件到 OSS
        OSS ossClient = new OSSClientBuilder().build(aliOSSProperties.getEndpoint(),
                aliOSSProperties.getAccessKeyId(), aliOSSProperties.getAccessKeySecret());
        ossClient.putObject(aliOSSProperties.getBucketName(), fileName, inputStream);

        // 文件访问路径
        String url = aliOSSProperties.getEndpoint().split("//")[0] + "//" + aliOSSProperties.getBucketName() + "." + aliOSSProperties.getEndpoint().split("//")[1] + "/" + fileName;

        // 关闭ossClient
        ossClient.shutdown();
        return url;// 把上传到oss的路径返回
    }
}
```

因为 `AliyunOSSUtil2` 类中，用到了 spring boot web 开发，以及阿里云 OSS SDK 中相关的 API；

所以在 pom.xml 文件中，引入 spring boot web 开发依赖，以及阿里云 OSS 的相关依赖。

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

##### 3.AliOSSAutoConfiguration 类添加

下面定义一个自动配置类 `AliOSSAutoConfiguration`，在自动配置类中，来声明 `AliyunOSSUtil2` 类的 Bean 对象。

demo-project/aliyun-oss-spring-boot-autoconfigure/src/main/java/com/aliyun/oss/AliOSSAutoConfiguration.java

```java
package com.aliyun.oss;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 声明当前类为 Spring 的配置类
@EnableConfigurationProperties(AliyunOSSProperties2.class) // 导入 AliyunOSSProperties2 类的 Bean 对象，并交给 Spring IOC 容器管理
public class AliOSSAutoConfiguration {
    @Bean
    public AliyunOSSUtil2 aliyunOSSUtil2(AliyunOSSProperties2 aliyunOSSProperties2) {
        AliyunOSSUtil2 aliyunOSSUtil2 = new AliyunOSSUtil2();
        aliyunOSSUtil2.setAliOSSProperties(aliyunOSSProperties2);
        return aliyunOSSUtil2;
    }
}
```

##### 4.自动配置文件

在 aliyun-oss-spring-boot-autoconfigure 模块中的 resources 目录下，新建自动配置文件：

demo-project/aliyun-oss-spring-boot-autoconfigure/src/main/resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports

```imports
com.aliyun.oss.AliOSSAutoConfiguration
```

### 3.阿里云 OSS 自定义 starter 使用

阿里云 OSS 的 starter 已经定义好了，接下来做一个测试。

在 IDEA 中，导入自定义 starter 测试工程 springboot-autoconfiguration-test。

在该测试工程的 pom.xml 文件中，引入自定义 starter 依赖：

demo-project/springboot-autoconfiguration-test/pom.xml

```xml
<!-- 自定义 starter-->
<dependency>
    <groupId>com.aliyun.oss</groupId>
    <artifactId>aliyun-oss-spring-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

在测试工程中的 application.yml 配置文件中，配置阿里云 OSS 参数信息：

demo-project/springboot-autoconfiguration-test/src/main/resources/application.yml

```yaml
aliyun:
  oss:
    endpoint: https://oss-cn-shenzhen.aliyuncs.com
    accessKeyId: xxxxxx
    accessKeySecret: xxxxxx
    bucketName: zetian-bucket
```

在测试工程中的 `UploadController` 类，编写代码：

demo-project/springboot-autoconfiguration-test/src/main/java/com/itheima/controller/UploadController.java

```java
package com.itheima.controller;

import com.aliyun.oss.AliyunOSSUtil2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {
    @Autowired
    private AliyunOSSUtil2 aliyunOSSUtil2;

    @PostMapping("/upload")
    public String upload(MultipartFile image) throws Exception {
        //上传文件到阿里云 OSS
        return aliyunOSSUtil2.upload(image);
    }
}
```

启动 Spring Boot 测试工程：自动配置会把 `AliyunOSSUtil2` 类的 Bean 对象，注册到 IOC 容器中。

用接口测试工具，进行文件上传：
