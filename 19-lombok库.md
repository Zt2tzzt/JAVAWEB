# Lombok 库

Lombok 是一个实用的 Java 类库，可以通过简单的注解，来简化和消除一些必须有，但显得很臃肿的 Java 代码。

比如：对于实体（pojo）类来说，可以通过注解的形式：自动生成

- 构造器
- getter、setter 方法
- `equals`、`hashcode` 方法
- `toString` 方法；

并可以自动化生成日志变量，简化 java 开发、提高效率。

## 一、Lombok 常用注解

### 1.@Getter、@Setter 注解

为实体类所有的属性提供 `get`、`set` 方法

### 2.@ToString 注解

为实体类自动生成易阅读的 `toString` 方法

### 3.@EqualsAndHashCode 注解

根据实体类所拥有的除了 `static` 修饰的字段之外，自动重写 `equals` 方法和  `hashCode` 方法

### 4.@Data 注解

提供了更综合的生成代码功能（`@Getter`  + `@Setter` + `@ToString` + `@EqualsAndHashCode`）

### 5.@NoArgsConstructor 注解

为实体类，生成无参的构造器方法。

### 6.@AllArgsConstructor 注解

为实体类，生成除了 static 修饰的字段之外，带有各参数的构造器方法。

## 二、Lombok 的使用

在基于 Maven 构建的 Spring Boot 项目中，添加 lombok 依赖。

- Spring Boot 父工程中，管理了 Lombok 的版本号。
- 所以，在 pom.xml 中，引入 Lombok 依赖，不用指定版本号。

demo-project/springbot-mybatis-quickstart/pom.xml

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```

在实体（pojo）类上，添加注解：

demo-project/springbot-mybatis-quickstart/src/main/java/com/kkcf/pojo/User.java

```java
package com.kkcf.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    // 实体类中的属性，推荐使用包装类型
    private Integer id;
    private String name;
    private Short age;
    private Short gender;
    private String phone;
}
```

Lombok 的注意事项：

- Lombok 会在程序**编译时**，自动生成对应的 java 代码，再编译成字节码文件。
- 在编辑器中使用 lombok 时，还需要安装一个 lombok 的插件（IDEA 中已自带）
