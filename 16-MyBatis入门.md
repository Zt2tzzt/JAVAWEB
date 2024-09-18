# MyBatis 入门

在客户端工具（Navicat、DataGrip）中，编写 SQL 语句，发给 MySQL 数据库管理系统（DBMS），由数据库管理系统（DBMS）执行 SQL 语句，并返回执行结果。

- 增、删、改操作，返回受影响行数；
- 查操作：返回结果集（查询的结果）

后端开发人员，通常会使用程序，来完成对数据库的操作。

## 一、MyBatis 是什么

Java 程序操作数据库，现在主流的框架是：MyBatis。

MyBatis 是一款优秀的**持久层框架**，用于简化 JDBC 的开发。

- 持久层，指的是数据访问层（dao），是用来操作数据库的。
- 框架：是一套可重用的、通用的、软件基础代码模型。在框架的基础上进行软件开发更加高效、规范、通用、可拓展。

> JDBC 是 SUM 公司提供的 JavaEE 13 项操作规范之一，声明了 Java 程序操作数据库的规范。

## 二、MyBatis 历史

[MyBatis](https://mybatis.org/mybatis-3/zh_CN/index.html) 本是 Apache 的一个开源项目 iBatis；

2010 年，这个项目由 Apache 迁移到了 google code，并且改名为 MyBatis 。

2013 年 11 月，迁移到 Github。

## 三、MyBatis 入门程序

MyBatis 入门程序。

### 1.MyBatis 入门程序准备工作

Ⅰ、创建 Spring Boot 项目；

1. 进入创建项目窗口：File -> New -> Project；
2. 选择左侧 Spring Boot；
3. 填写右侧信息：
   - Language 设为 Java；
   - Type 设为 Maven；
   - Group 为组织名，比如设为 `com.kkcf`
   - artifact 为项目名，比如设为 `springboot-mybatis-quickstart`；设置完成后，上方 name 会跟着修改。
   - Pacakge name 为要在项目中创建的包的层级，比如设为 `com.kkcf`
   - Java 为 Java 版本；比如设为 17；
   - Pacakging 为打包方式，比如设为 Jar；
4. 点击 Next，进入下一步；
5. 选择 Spring Boot 版本，默认是最新版本。
6. 在下方，勾选 Web -> Spring Web；
7. 在下方，勾选 SQL -> MyBatis Framework；勾选 SQL -> MySQL Driver；
8. 点击 Create，联网创建项目；

创建项目后 pom.xml 文件如下方所示：

demo-project/springbot-mybatis-quickstart/pom.xml

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
    <artifactId>springbot-mybatis-quickstart</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>springbot-mybatis-quickstart</name>
    <description>springbot-mybatis-quickstart</description>
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
        <!-- Spring Boot 起步依赖-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- Spring Boot MyBatis 起步依赖-->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>3.0.3</version>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter-test</artifactId>
            <version>3.0.3</version>
            <scope>test</scope>
        </dependency>

        <!-- MySQL 驱动包-->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
        <!-- MySQL 上一个版本的驱动包-->
        <!--<dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>-->
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

Ⅱ、准备数据库，创建用户表 user；并初始化数据

```mysql
-- 用户表
CREATE TABLE user
(
    id     INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    name   VARCHAR(100) COMMENT '姓名',
    age    TINYINT UNSIGNED COMMENT '年龄',
    gender TINYINT UNSIGNED COMMENT '性别, 1:男, 2:女',
    phone  VARCHAR(11) COMMENT '手机号'
) COMMENT '用户表';

-- 测试数据
INSERT INTO user(id, name, age, gender, phone)
VALUES (NULL, '白眉鹰王', 55, '1', '18800000000');
INSERT INTO user(id, name, age, gender, phone)
VALUES (NULL, '金毛狮王', 45, '1', '18800000001');
INSERT INTO user(id, name, age, gender, phone)
VALUES (NULL, '青翼蝠王', 38, '1', '18800000002');
INSERT INTO user(id, name, age, gender, phone)
VALUES (NULL, '紫衫龙王', 42, '2', '18800000003');
INSERT INTO user(id, name, age, gender, phone)
VALUES (NULL, '光明左使', 37, '1', '18800000004');
INSERT INTO user(id, name, age, gender, phone)
VALUES (NULL, '光明右使', 48, '1', '18800000005');
```

创建实体（pojo）类 User

demo-project/springbot-mybatis-quickstart/src/main/java/com/kkcf/pojo/User.java

```java
package com.kkcf.pojo;

public class User {
    // 实体类中的属性，推荐使用包装类型
    private Integer id;
    private String name;
    private Short age;
    private Short gender;
    private String phone;

    // constructors ……

    // getter、setter ……

    // toString ……
}
```

### 2.MyBatis 入门程序配置数据库连接

Spring Boot 整合的 MyBatis 矿建，配置数据库连接信息在 application.properties 配置文件中。

demo-project/springbot-mybatis-quickstart/src/main/resources/application.properties

```properties
# 驱动类名称
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
# 数据库连接 url
spring.datasource.url=jdbc:mysql://localhost:3306/xxxxxx
# 连接数据库用户名
spring.datasource.username=xxxxxx
# 连接数据库密码
spring.datasource.password=xxxxxx
```

### 3.MyBatis 入门程序编写 SQL 语句

在 MyBatis 框架中，可以通过注解，或 XML 映射文件方式，编写 SQL 语句。

创建 mapper 包，相当于三层架构中的 dao 包，用于存放 MyBatis 框架中用到的 Mapper 接口。

- 在 mapper 包中，一般只需要定义接口，不需要定义实现类，接口名一般为 `xxxMapper`；
- `@Mapper` 注解，表示在程序运行时，MyBatis 框架会自动生成该接口的实现类对像（代理对象，Bean 对象），并且将该对象，交给 IOC 容器管理。
- 在 `@Select` 注解中，写 SQL 语句。
- 查询语句会将查询结果，自动封装到接口的返回值中。

demo-project/springbot-mybatis-quickstart/src/main/java/com/kkcf/mapper/UserMapper.java

```java
package com.kkcf.mapper;

import com.kkcf.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper // 在程序运行时，MyBatis 框架会自动生成该接口的实现类对象（代理对象），并且将该对象，交给 IOC 容器管理。
public interface UserMapper {
    @Select("SELECT * FROM user")
    List<User> userlist();
}
```

### 4.MyBatis 入门程序单元测试

Spring Boot 工程，在 src 下的 test 目录下，已经自动创建好了测试类 `SpringbotMybatisQuickstartApplicationTests`，

- `该类上的 @SpringBootTest` 注解，是 Spring Boot 整合单元测试注解，表示该测试类，已经与 Spring Boot 整合。
  - 类中的单元测试方法，在运行时，会自动通过引导类 `SpringbotMybatisQuickstartApplication`，加载 Spring Boot 环菌，并创建 IOC 容器。
  - 要测试哪个 bean 对象，就可直接通过 `@Autowired` 注解，将其注入。

在其中，编写测试方法 `testListUser`

demo-project/springbot-mybatis-quickstart/src/test/java/com/kkcf/SpringbotMybatisQuickstartApplicationTests.java

```java
package com.kkcf;

import com.kkcf.mapper.UserMapper;
import com.kkcf.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest // Spring Boot 整合单元测试的注解：类中的单元测试方法在运行时，会自动加载 Spring Boot 环菌，并创建 IOC 容器。
class SpringbotMybatisQuickstartApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testListUser() {
        List<User> users = userMapper.listUser();

        users.stream().forEach(System.out::println);
    }

}
```

## 四、IDEA 配置 MyBatis SQL 提示

在 `UserMapper` 类中：

- 右键 `@Select` 注解中的 SQL 语句 -> 点击 “Show Context Actions” -> 点击 “Language Injection Setting” -> ID 选择 MySQL；

会发现，查询的表 user 报红色。这是因为在 IDEA 中，没有配置要操作的数据库的连接。所以 IDEA 没法给出 SQL 语句正确性的提示。

- 在 IDEA 右侧 Database 面板中，配置目标数据库的连接即可。
