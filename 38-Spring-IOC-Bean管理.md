# Spring IOC Bean 管理

Spring 提供了 @Component 注解，以及它的三个衍生注解 @Controller、@Service、@Repository 来声明 IOC 容器中的 Bean 对象；

在 Spring 中，也可以使用依赖注入（DI），为应用程序注入运行时需要的 Bean 对象。

## 一、IOC 容器获取 Bean 对象

Spring Boot 项目启动时，会自动创建 IOC 容器（Spring 容器），并将创建 Bean 对象，存放在 IOC 容器当中。

应用程序在运行时，需要依赖的 Bean 对象，直接进行依赖注入即可。

从 IOC 容器中，获取到 Bean 对象，要先拿到 `ApplicationContext` 类型的 IOC 容器对象，

在 Spring 环菌中，可直接将 `ApplicationContext` 类型的  IOC 容器对象，注入到应用程序（类）中。

通过 IOC 容器对象，获取 Bean 对象，主要有三种方式：

方式一：根据 name，获取 Bean 对象（带类型转换）。

```java
Object getBean(String name)
```

方式二：根据类型，获取 Bean 对象。

```java
<T> T getBean(Class<T> requiredType)
```

方式三：根据 name 和类型，获取 Bean 对象

```java
<T> T getBean(String name, Class<T> requiredType)
```

在测试类中，注入 `ApplicationContext` IOC 容器对象，利用它获取到 Bean 对象：

demo-project/javaweb-practise/src/test/java/com/kkcf/BeanTest.java

```java
package com.kkcf;

import com.kkcf.controller.DeptController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
public class BeanTest {
    @Autowired
    private ApplicationContext applicationContext; // IOC 容器对象

    @Test
    public void testGetBean() {
        // 根据 Bean 对象的名称，获取；需要做类型强转
        DeptController bean1 = (DeptController) applicationContext.getBean("deptController"); // 如果在声明 Bean 对象时，没有指定名称，默认就是类名首字母小写。
        System.out.println(bean1);

        // 根据 Bean 的类型获取
        DeptController bean2 = applicationContext.getBean(DeptController.class);
        System.out.println(bean2);

        // 根据 Bean 的名称和类型获取
        DeptController bean3 = applicationContext.getBean("deptController", DeptController.class);
        System.out.println(bean3);
    }
}
```

- Bean 对象，在声明时，如果没有指定名称，那么默认就是类型首字母小写。

执行测试方法，观察控制台输出：

```sh
com.kkcf.controller.DeptController@4d8f2cfd
com.kkcf.controller.DeptController@4d8f2cfd
com.kkcf.controller.DeptController@4d8f2cfd
```

发现：三次获取出的 Bean 对象，地址值一样，说明是同一个对象；

IOC 容器（Spring 容器）中，Bean 对象默认是**单例**的（只有一个 Bean 对象）。

> “Spring 项目启动时，会把 Bean 都创建好，放在 IOC 容器中”，这只针对默认的单例 Bean 对象；
>
> 事实上，Bean 对象，还会受到作用域，和延迟初始化的影响；

案例理解：在 DeptController 类中，加入空参构造方法，在其中加入打印语句，用于记录对象的创建。

demo-project/javaweb-practise/src/main/java/com/kkcf/controller/DeptController.java

```java
@Slf4j
@RestController
@RequestMapping("/depts")
public class DeptController {
    @Autowired
    private DeptService deptService;

    public DeptController() {
        System.out.println("创建了 DeptController");
    }

   // ……
}
```

在测试类中，获取十次 DeptController 的 Bean 对象：

demo-project/javaweb-practise/src/test/java/com/kkcf/BeanTest.java

```java
package com.kkcf;

import com.kkcf.controller.DeptController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
public class BeanTest {
    @Autowired
    private ApplicationContext applicationContext; // IOC 容器对象

    @Test
    public void testBeanScope() {
        for (int i = 0; i < 10; i++) {
            DeptController bean = applicationContext.getBean(DeptController.class);
            System.out.println(bean);
        }
    }
}
```

执行测试方法：

1. 在加载 Spring 环菌时，DeptController 的 Bean 对象，就创建了。

   ```sh
   创建了 DeptController
   ```

2. 测试方法执行，获取到了 10 个相同的 Bean 对象。

    ```sh
    com.kkcf.controller.DeptController@77d95e5a
    com.kkcf.controller.DeptController@77d95e5a
    com.kkcf.controller.DeptController@77d95e5a
    com.kkcf.controller.DeptController@77d95e5a
    com.kkcf.controller.DeptController@77d95e5a
    com.kkcf.controller.DeptController@77d95e5a
    com.kkcf.controller.DeptController@77d95e5a
    com.kkcf.controller.DeptController@77d95e5a
    com.kkcf.controller.DeptController@77d95e5a
    com.kkcf.controller.DeptController@77d95e5a
    ```

## 二、Bean 对象延迟加载

默认的单例 Bean 对象，会在容器启动时被创建；

### 1.@Lazy 注解

可以使用 @Lazy 注解，来延迟默认单例 Bean 对象的初始化，直到第一次使用时，再创建 Bean 对象。

在 DeptController 类上，加上 @Lazy 注解。

demo-project/javaweb-practise/src/main/java/com/kkcf/controller/DeptController.java

```java
@Lazy
@Slf4j
@RestController
@RequestMapping("/depts")
public class DeptController {
    @Autowired
    private DeptService deptService;

    public DeptController() {
        System.out.println("创建了 DeptController");
    }

   // ……
}
```

在测试类中，获取十次 DeptController 的 Bean 对象：

demo-project/javaweb-practise/src/test/java/com/kkcf/BeanTest.java

```java
package com.kkcf;

import com.kkcf.controller.DeptController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
public class BeanTest {
    @Autowired
    private ApplicationContext applicationContext; // IOC 容器对象

    @Test
    public void testBeanScope() {
        for (int i = 0; i < 10; i++) {
            DeptController bean = applicationContext.getBean(DeptController.class);
            System.out.println(bean);
        }
    }
}
```

执行测试方法，发现：

1. 加载 Spring 环菌时，没有创建 DeptController 的 Bean 对象；
2. 而是在测试方法执行，获取 Bean 对象时，Bean 对象才被创建。

获取到了 10 次相同的 Bean 对象。

```sh
创建了 DeptController
com.kkcf.controller.DeptController@1fa18f87
com.kkcf.controller.DeptController@1fa18f87
com.kkcf.controller.DeptController@1fa18f87
com.kkcf.controller.DeptController@1fa18f87
com.kkcf.controller.DeptController@1fa18f87
com.kkcf.controller.DeptController@1fa18f87
com.kkcf.controller.DeptController@1fa18f87
com.kkcf.controller.DeptController@1fa18f87
com.kkcf.controller.DeptController@1fa18f87
com.kkcf.controller.DeptController@1fa18f87
```

## 三、Bean 对象作用域

要设置 Bean 对象为非单例，那么需要设置 Bean 的作用域。

Spring 的 Bean 对象，支持五种作用域。如下表所示，后三种在 web 环境才生效：

| 作用域      | 说明                                                    |
| ----------- | ------------------------------------------------------- |
| singleton   | 默认值，容器内同名称的 Bean 对象，只有一个实例（单例）  |
| prototype   | 每次使用该 Bean 对象时，会创建新的实例（非单例）        |
| request     | 每个请求范围内，会创建新的实例（用于 Web 环境中，了解） |
| session     | 每个会话范围内，会创建新的实例（用于 Web 环境中，了解） |
| application | 每个应用范围内，会创建新的实例（用于 Web 环境中，了解） |

### 1.@Scope 注解

@Scope 注解，可以配置 Bean 的作用域。

在 DeptController 类上，加上 @Scope 注解，并指定它作为 IOC 容器管理的 Bean 对象的生命周期作用域为 prototype

demo-project/javaweb-practise/src/main/java/com/kkcf/controller/DeptController.java

```java
@Scope("prototype")
@Slf4j
@RestController
@RequestMapping("/depts")
public class DeptController {
    @Autowired
    private DeptService deptService;

    public DeptController() {
        System.out.println("创建了 DeptController");
    }

    // ……
}
```

在测试类中，获取十次 DeptController 的 Bean 对象：

demo-project/javaweb-practise/src/test/java/com/kkcf/BeanTest.java

```java
package com.kkcf;

import com.kkcf.controller.DeptController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
public class BeanTest {
    @Autowired
    private ApplicationContext applicationContext; // IOC 容器对象

    @Test
    public void testBeanScope() {
        for (int i = 0; i < 10; i++) {
            DeptController bean = applicationContext.getBean(DeptController.class);
            System.out.println(bean);
        }
    }
}
```

执行测试方法，观察控制台输出：

```sh
创建了 DeptController
com.kkcf.controller.DeptController@5627b8eb
创建了 DeptController
com.kkcf.controller.DeptController@49fe0bcd
创建了 DeptController
com.kkcf.controller.DeptController@3516b881
创建了 DeptController
com.kkcf.controller.DeptController@6be8ce1b
创建了 DeptController
com.kkcf.controller.DeptController@e3c36d
创建了 DeptController
com.kkcf.controller.DeptController@397a10df
创建了 DeptController
com.kkcf.controller.DeptController@39a865c1
创建了 DeptController
com.kkcf.controller.DeptController@141dfcf9
创建了 DeptController
com.kkcf.controller.DeptController@a7bbdbc
创建了 DeptController
com.kkcf.controller.DeptController@63eea8c4
```

可见：每次获取 Bean 对象时，都会创建新的 Bean 对象。

在实际开发当中，绝大部分的 Bean 对象是单例的，不需要配置作用域。

## 四、第三方 Bean 对象管理

前面配置的 Bean 对象，都是项目中自定义的类。要声明这些 Bean，非常简单：

- 只需要在类上标注 @Component 以及它的三个衍生注解（@Controller、@Service、@Repository），即可。

然而，在项目开发中，用到的某个类，不是自己编写的，而是引入的第三方依赖提供的，这时就需要使用 @Bean 注解。

### 1.@Bean 注解

要管理的 Bean 对象，来自于第三方（不是自定义），是无法用 @Component 及衍生注解声明 Bean 的；需要用到 @Bean 注解。

比如：引入 dom4j 依赖，解析 xml 文件。

demo-project/javaweb-practise/pom.xml

```xml
<dependency>
    <groupId>org.dom4j</groupId>
    <artifactId>dom4j</artifactId>
    <version>2.1.3</version>
</dependency>
```

解决方案一：在启动类（引导类）里，定义 @Bean 注解标注的方法，用于 Spring 项目在启动时创建 Bean 对象，并让如 IOC 容器中管理。

demo-project/javaweb-practise/src/main/java/com/kkcf/JavawebPractiseApplication.java

```java
package com.kkcf;

import org.dom4j.io.SAXReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;

import javax.xml.transform.sax.SAXResult;

@ServletComponentScan // 当前项目开启了对 JavaWeb（Servlet）组件的支持。
@SpringBootApplication
public class JavawebPractiseApplication {
    public static void main(String[] args) {
        SpringApplication.run(JavawebPractiseApplication.class, args);
    }

    @Bean // 声明第三方 bean 对象，将当前方法的返回值，交给 IOC 容器管理，成为 IOC 容器的 Bean。
    public SAXReader saxReader() {
        return new SAXReader();
    }
}
```

这种做法不推荐，因为要保证启动类的纯粹性。

解决方案二：在 @Configuration 声明的配置类中，定义 @Bean 注解标注的方法。

- 可以通过 @Bean 注解的 name 或 value 属性，来指定 Bean 对象的名称；
- 但在实际开发中，一般不指定，默认是方法名称。

demo-project/javaweb-practise/src/main/java/com/kkcf/config/CommonCofig.java

```java
package com.kkcf.config;

import org.dom4j.io.SAXReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonCofig {

    @Bean // 声明第三方 bean 对象，将当前方法的返回值，交给 IOC 容器管理，成为 IOC 容器的 Bean。
    public SAXReader saxReader() {
        return new SAXReader();
    }
}
```

- 在方法上，使用 @Bean 注解，Spring 容器在启动时，会自动调用该方法，并将方法的返回值，声明为 Spring 容器中的 Bean 对象。

xml 文件

demo-project/javaweb-practise/src/main/resources/1.xml

```xml
<?xml version="1.0" encoding="utf-8" ?>
<emp>
    <name>tom</name>
    <age>18</age>
</emp>
```

在测试类中，注入 SAXReader 的 Bean 对象，并使用。

demo-project/javaweb-practise/src/test/java/com/kkcf/BeanTest.java

```java
package com.kkcf;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BeanTest {
    @Autowired
    private SAXReader saxReader;

    @Test
    public void testThirdBean() throws DocumentException {
        //SAXReader saxReader = new SAXReader();

        Document document = saxReader.read(this.getClass().getClassLoader().getResource("1.xml"));
        Element rootElement = document.getRootElement();
        String name = rootElement.element("name").getText();
        String age = rootElement.element("age").getText();

        System.out.println(name + ":" + age);
    }
}
```

执行测试方法 `testThirdBean`，控制台输出：

```sh
tom:18
```

如果第三方 Bean 对象，创建时需要依赖其它 Bean 对象，那么在 @Bean 注解标注的方法中，设置形参即可；

IOC 容器会根据类型，自动装配（依赖注入）。

demo-project/javaweb-practise/src/main/java/com/kkcf/config/CommonCofig.java

```java
package com.kkcf.config;

import com.kkcf.service.DeptService;
import org.dom4j.io.SAXReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonCofig {

    @Bean // 声明第三方 bean 对象，将当前方法的返回值，交给 IOC 容器管理，成为 IOC 容器的 Bean。
    public SAXReader saxReader(DeptService deptService) {
        // Spring 根据 bean 的依赖关系，自动注入 deptService Bean 对象。
        System.out.println(deptService);
        return new SAXReader();
    }
}
```

> 使用 @Configuration 可声明配置类；
>
> @Configuration 注解底层，也是用了 @Component 注解，为配置类生成 Bean 对象，并进行管理。

### 2.@Bean 与 @Component 选择

- 自定义类，交给 IOC 容器管理，使用 @Component 以及它的衍生注解来声明。
- 引入的第三方依赖中提供的类，交给 IOC 容器管理。那么要在配置类中定义一个方法，加上一个 @Bean 注解，来声明第三方的 Bean 对象。
