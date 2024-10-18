# Spring Boot 原理之自动配置 Conditional 条件装配

## 一、Conditional 条件装配注解

在第三方依赖中（比如 gson）自动配置类声明的 Bean 对象，除了在方法上加了一个 `@Bean` 注解外，还会经常用到一个以 `@Conditional`  开头的注解。

GsonAutoConfiguration.class

```java
import ……

@AutoConfiguration
@ConditionalOnClass({Gson.class})
@EnableConfigurationProperties({GsonProperties.class})
public class GsonAutoConfiguration {
    public GsonAutoConfiguration() {
    }

    @Bean
    @ConditionalOnMissingBean
    public GsonBuilder gsonBuilder(List<GsonBuilderCustomizer> customizers) {
        GsonBuilder builder = new GsonBuilder();
        customizers.forEach((c) -> {
            c.customize(builder);
        });
        return builder;
    }

    @Bean
    @ConditionalOnMissingBean
    public Gson gson(GsonBuilder gsonBuilder) {
        return gsonBuilder.create();
    }

    // ……
}
```

`@Conditional` 开头的这些注解，都是条件装配注解。

- 用于按照一定的条件进行判断，在满足给定条件后，才会注册对应的 Bean 对象到 Spring IOC 容器中。
- 可用在方法、类上。
  - 当用在类上时，整个类的 Bean 定义，是否加载到 Spring 容器中，取决于给定的条件；
  - 当用在方法上时，会根据条件决定是否执行该方法来生成对应的 Bean。

`@Condition` 本身是一个注解，它派生出了大量的注解，常见的有以下三个：

### 1.@ConditionalOnClass 注解

`@ConditionalOnClass` 注解：判断环境中是否有对应字节码文件，是，则注册 Bean 到 IOC 容器。

判断的方式有两种：

- `name` 属性，指定字节码文件的全类名。
- `value` 属性（可省略），指定字节码文件对象。

在第三方依赖的 `HeaderConfig` 配置类中，声明 Bean 对象的 `headerParser` 方法上，加上 `@ConditionalOnClass` 注解；

- 指定环境中存在 `io.jsonwebtoken.Jwts` 类时，才会将该 Bean 加入 IOC 容器；

com/example/HeaderConfig.java

```java
package com.example;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HeaderConfig {
    @ConditionalOnClass(name = "io.jsonwebtoken.Jwts")
    @Bean
    public HeaderParser headerParser() {
        return new HeaderParser();
    }
}
```

运行测试方法 `testHeaderParser`，获取 `HeaderParser` 的 Bean 对象。

demo-project/javaweb-practise/src/test/java/com/kkcf/AutoConfigurationTest.java

```java
package com.kkcf;

import com.example.HeaderParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
public class AutoConfigurationTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testHeaderParser() {
        System.out.println(applicationContext.getBean(HeaderParser.class));
    }
}
```

观察控制台输出：

```sh
com.example.HeaderParser@66a99584
```

说明 IOC 容器中已加载 Bean 对象 headerParser；

- 因为 io.jsonwebtoken.Jwts 字节码文件，在启动 Spring Boot 程序时已存在，
- 所以创建了 `HeaderParser` 的 Bean 对象，并注册到 IOC 容器中。

现在将 pom.xml 文件中，jwt 相关依赖注释掉：

demo-project/javaweb-practise/pom.xml

```xml
<!-- JWT -->
<!--<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.1</version>
</dependency>-->
```

再次执行测试方法 `testHeaderParser`，观察控制台输出：

```sh
org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'com.example.HeaderParser' available
```

说明 IOC 容器中没有加载 Bean 对象 `headerParser`；

### 2.@ConditionalOnMissingBean 注解

`@ConditionalOnMissingBean` 注解应用场景：通常是用来设置默认的 Bean 对象。

`@ConditionalOnMissingBean` 用于根据类型或名称，来判断 IOC 容器环境中没有对应的 Bean 对象；没有才注册 Bean 到 IOC 容器。

判断的方式有三种：

- 没有属性，表示标注方法的类、方法声明的 Bean 对象。
- `name` 属性，指定字节码文件的全类名。
- `value` 属性（可省略），指定字节码文件对象。

在第三方依赖的 `HeaderConfig` 配置类中，声明 Bean 对象的 `headerParser` 方法上，加上 `@ConditionalOnMissingBean` 注解。

com/example/HeaderConfig.java

```java
package com.example;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HeaderConfig {
    @ConditionalOnMissingBean // 不存在该类型的 Bean，才会将该 Bean 加入 IOC 容器。
    @Bean
    public HeaderParser headerParser() {
        return new HeaderParser();
    }
}
```

执行测试方法 `testHeaderParser`，获取 `HeaderParser` 的 Bean 对象。

demo-project/javaweb-practise/src/test/java/com/kkcf/AutoConfigurationTest.java

```java
package com.kkcf;

import com.example.HeaderParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
public class AutoConfigurationTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testHeaderParser() {
        System.out.println(applicationContext.getBean(HeaderParser.class));
    }
}
```

观察控制台输出：

```sh
com.example.HeaderParser@212c0aff
```

说明 IOC 容器中已加载 Bean 对象 `headerParser`；

- 因为 Spring Boot 在调用 `@Bean` 标识的 `headerParser` 方法前，IOC 容器中是没有 `HeaderParser` 类型的 Bean 对象；
- 所以 `headerParser` 对象正常创建，并注册到 IOC 容器中。

在第三方依赖的 `HeaderConfig` 配置类中，修改 `@ConditionalOnMissingBean` 注解的使用：

com/example/HeaderConfig.java

```java
package com.example;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HeaderConfig {
    @ConditionalOnMissingBean(name = "deptController2") // 不存在指定名称的 Bean，才会将该 Bean 加入 IOC 容器
    @Bean
    public HeaderParser headerParser() {
        return new HeaderParser();
    }
}
```

执行测试方法 `testHeaderParser`，获取 `HeaderParser` 的 Bean 对象。观察控制台输出：

```sh
com.example.HeaderParser@7f5ecada
```

说明 IOC 容器中已加载 Bean 对象 `headerParser`；

- 因为 Spring Boot 在调用配置类中 `@Bean` 标识的 `headerParser` 方法前，IOC 容器里没有名称为 `deptController2` 的 Bean 对象；
- 所以 Bean 对象 `headerParser` 正常创建，并注册到 IOC 容器中。

在第三方依赖的 `HeaderConfig` 配置类中，再次修改 `@ConditionalOnMissingBean` 注解的使用：

com/example/HeaderConfig.java

```java
package com.example;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HeaderConfig {
    @ConditionalOnMissingBean(HeaderConfig.class) // 不存在指定类型的 bean，才会将 bean 加入 IOC 容器
    @Bean
    public HeaderParser headerParser() {
        return new HeaderParser();
    }
}
```

执行测试方法 `testHeaderParser`，获取 `HeaderParser` 的 Bean 对象。观察控制台输出：

```sh
org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'com.example.HeaderParser' available
```

说明 IOC 容器中没有加载 Bean 对象 `headerParser`；

- 因为 `HeaderConfig` 类上有 `@Configuration` 注解，其中包含了 `@Component` 注解；
- 所以 Spring Boot 项目在启动时，会创建 `HeaderConfig` 的 Bean 对象，并注册到 IOC 容器中。
- 当 IOC 容器中，有 `HeaderConfig` 类型的 Bean 对象存在，就不会把 `HeaderParser` 类型的对象注册到 IOC 容器中了。

### 3.@ConditionalOnProperty 注解

`@ConditionalOnProperty` 注解， 应用场景：使用 Spring Boot 整合第三方依赖时。

`@ConditionalOnProperty` 注解：判断配置文件中有对应属性和值，才注册 Bean 到 IOC 容器中。

先在 application.yml 配置文件中，添加如下的键值对：

```yaml
name: zzt
```

在第三方依赖的 `HeaderConfig` 配置类中，声明 Bean 时，使用 `@ConditionalOnProperty` 注解，指定一个条件。

- `name` 属性，用于指定配置文件中的属性名；
- `havingValue` 属性，用于指定配置文件中的属性值。

com/example/HeaderConfig.java

```java
package com.example;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HeaderConfig {
    @ConditionalOnProperty(name = "name", havingValue = "zzt") // 配置文件中存在指定属性名与值，才会将 Bean 加入 IOC 容器
    @Bean
    public HeaderParser headerParser() {
        return new HeaderParser();
    }
}
```

执行测试方法 `testHeaderParser`，获取 `HeaderParser` 的 Bean 对象。观察控制台输出：

```sh
com.example.HeaderParser@74cd8c55
```

在第三方依赖的 `HeaderConfig` 配置类中，修改 `@ConditionalOnProperty` 注解：

- `havingValue` 属性的值改为 `"zzt2"`；

com/example/HeaderConfig.java

```java
package com.example;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HeaderConfig {
    @ConditionalOnProperty(name = "name", havingValue = "zzt2") // 配置文件中存在指定属性名与值，才会将 Bean 加入 IOC 容器
    @Bean
    public HeaderParser headerParser() {
        return new HeaderParser();
    }
}
```

执行测试方法 `testHeaderParser`，获取 `HeaderParser` 的 Bean 对象。观察控制台输出：

```sh
org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'com.example.HeaderParser' available
```
