# SpringBoot 请求处理

在之前开发的 SpringBoot 入门程序中，使用 Controller 控制器进行了 Http GET 请求的处理

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

## 一、Tomcat 与 Controller 控制器的关系

Spring Boot 内置的 Tomcat 服务器，并不能识别项目中的 Controller 类。它们是怎么联系起来的？

- Tomcat 是 Servlet 容器，所以它能识别 Servlet 规范，
- 而在基于 Spring Boot 开发的 Web 应用程序中，提供了一个核心的 Servlet 程序 DispatchServlet，它实现了 Servlet 规范中的接口。

所以 SpringBoot 项目中，对于 Http 请求的处理过程是：

1. 浏览器（客户端）发送请求到 Spring Boot 后端服务；
2. Spring Boot 后端服务，通过 DispatchServlet 将请求转给 Controller 处理器。
3. Controller 处理器处理完成后，将响应放回给 DispatchServlet；
4. DispatchServlet 再将响应返回给浏览器（客户端）。

## 二、DispatchServlet 核心控制器

DispatchServlet 类的继承体系如下

![DispatchServlet继承体系](NoteAssets/DispatchServlet继承体系.png)

- 可以看到，Servlet 继承自 DispatchServlet。

可以看出 DispatchServlet 是非常核心的类，在应用程序中被称为“**核心控制器**”或”**前端控制器**“

![SpringBoot请求响应1](NoteAssets/SpringBoot请求响应1.png)

## 三、HttpServletRequest 请求对象、HttpServletResponse 响应对象

浏览器（客户端）发送的请求，在 Spring Boot 项目中，会经过 Tomcat 封装成一个 `HttpServletRequest` 对象（请求对象）；

- 应用程序通过该对象，来获取请求信息，进行处理。

在 Spring Boot 项目中，可使用 `HttpServletResponse` 对象（请求对象），来封装要返回的响应信息。

- Tomcat 会将该对象中封装的响应信息，返回给浏览器（客户端）。

![SpringBoot请求响应2](NoteAssets/SpringBoot请求响应2.png)

开发者需要重点关注 Controller  控制器，在其中获取请求信息，处理请求，返回响应。

## 四、SpringBoot 请求处理

客户端在向服务器发起请求时，传递参数的方式是：

- GET 请求，传递的是一些 query 字符串形式的参数。或者通过路径传递参数。
- POST 请求，在请求体（body）中以多种形式（比如： x-www-form-urlencoded、json）方式传递的参数

### 1.简单参数处理

在 Spring Boot 后端程序中，接收传递过来的参数，有两种方式：

- 原始方式
- SpringBoot 方式

#### 1.原始方式处理

Tomcat 接收到 http 请求时：会把请求的相关信息，封装到 `HttpServletRequest` 请求对象中

在原始的 Web 程序当中，需要通过 Servlet 中提供的 API：`HttpServletRequest` 请求对象，获取请求的相关信息。

- 比如：获取请求参数：

demo-project/springboot-web-quickstart/src/main/java/com/kkcf/controller/RequestController.java

```java
package com.kkcf.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestController {
    @RequestMapping("/simpleParam")
    public String simplePath(HttpServletRequest req) {
        String name = req.getParameter("name");
        String ageStr = req.getParameter("age");

        int age = Integer.parseInt(ageStr);
        System.out.println(name + " " + age);

        return "haha";
    }
}
```

- `getParameter` 方法，取到的参数，都是字符串类型的。

在客户端，发送请求 `localhost:8080/simpleParam?name=zzt&age=18` 测试。

这种方式，仅做了解。在实际开发中不会使用。

#### 2.Spring Boot 方式处理

在 Spring Boot 项目中，对原始的 `HttpServletRequest` 请求对象 API 进行了封装；

使得在 Controller 控制器的方法中，接收参数的形式更加简单。

- 如果是处理简单参数，**参数名与形参变量名相同**，即可接收参数，会进行**自动类型转换**。

demo-project/springboot-web-quickstart/src/main/java/com/kkcf/controller/RequestController.java

```java
package com.kkcf.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestController {
    @RequestMapping("/simpleParam")
    public String simplePath(String name, int age) {
        System.out.println(name + " " + age);

        return "haha";
    }
}
```

参数名与形参变量名不相同，那么形参接收到的值为 `null`；

在客户端，发送请求 `localhost:8080/simpleParam?name=zzt&age=18` 测试。

#### 1.@RequestParam 注解

如果要将不同的参数名和形参变量名映射起来，要使用 `@RequestParam` 注解

##### 1.value 属性

在方法形参前面，加上 `@RequestParam` 注解，然后通过 `value` 属性作为请求参数名，从而完成映射。代码如下：

demo-project/springboot-web-quickstart/src/main/java/com/kkcf/controller/RequestController.java

```java
package com.kkcf.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestController {
    @RequestMapping("/simpleParam")
    public String simplePath(@RequestParam(value = "name") String username, int age) {
        System.out.println(username + " " + age);

        return "haha";
    }
}
```

在客户端，发送请求 `localhost:8080/simpleParam?name=zzt&age=18` 测试。

##### 2.required 属性

`@RequestParam` 注解中的 `required` 属性，默认为 `true`，表示该请求参数必须传递，如果不传递将报错

如果参数是可选的，要手动将 `required` 属性设置为 `false` 。

demo-project/springboot-web-quickstart/src/main/java/com/kkcf/controller/RequestController.java

```java
package com.kkcf.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestController {
    @RequestMapping("/simpleParam")
    public String simplePath(@RequestParam(value = "name", required = false) String username, int age) {
        System.out.println(username + " " + age);

        return "haha";
    }
}
```

在客户端，发送请求 `localhost:8080/simpleParam?name=zzt&age=18` 测试。

### 2.实体参数处理

请求在传递简单参数时，请求中有多少个请求参数，后端 Controller 控制器的处理方法中，形参就要有多少个。

如果请求参数比较多，通过上述的方式一个参数一个参数的接收，会比较繁琐。

可以考虑将请求参数，封装到一个实体（pojo）类的对象中。

- 要遵守**请求参数名与实体类的属性名相同**的规则。

创建一个实体（pojo）类 User：

demo-project/springboot-web-quickstart/src/main/java/com/kkcf/pojo/User.java

```java
package com.kkcf.pojo;

public class User {
    private String name;
    private int age;

    public User() {
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
```

在 Controller 控制器中，定义处理方法：

demo-project/springboot-web-quickstart/src/main/java/com/kkcf/controller/RequestController.java

```java
package com.kkcf.controller;

import com.kkcf.pojo.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestController {
    @RequestMapping("/simplePojo")
    public String simplePojo(User user) {
        System.out.println(user);

        return "haha";
    }
}
```

在客户端，发送请求 `localhost:8080/simplePojo?name=zzt&age=18` 测试。

#### 1.实体类的封装

User 实体类中，还有一个 Address 类型的属性。

demo-project/springboot-web-quickstart/src/main/java/com/kkcf/pojo/User.java

```java
package com.kkcf.pojo;

public class User {
    private String name;
    private int age;
    private Address address;

    // constructor……

    // getter、setter……

    // toString()……
}
```

Address 类

demo-project/springboot-web-quickstart/src/main/java/com/kkcf/pojo/Address.java

```java
package com.kkcf.pojo;

public class Address {
    private String province;
    private String city;

    // constructor……

    // getter、setter……

    // toString()……
}
```

Controller 控制器中的处理方法：

demo-project/springboot-web-quickstart/src/main/java/com/kkcf/controller/RequestController.java

```java
package com.kkcf.controller;

import com.kkcf.pojo.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestController {
    @RequestMapping("/complexPojo")
    public String complexPojo(User user) {
        System.out.println(user);

        return "haha";
    }
}
```

测试，在客户端发起的请求 url 应为：`localhost:8080/simplePojo?name=zzt&age=18&address.province=广东&address.city="深圳"`

### 3.数组参数处理

浏览器（客户端）发送请求，传递数组参数的形式可以是：

- `localhost:8080/arrayParam?hobby=唱&hobby=跳&hobby=rap&hobby=篮球`
- `localhost:8080/arrayParam?hobby=唱,跳,rap,篮球`

#### 1.使用数组接收

直接使用数组类型的形参，来接收请求的参数。

Controller 控制器：

demo-project/springboot-web-quickstart/src/main/java/com/kkcf/controller/RequestController.java

```java
package com.kkcf.controller;

import com.kkcf.pojo.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class RequestController {
    @RequestMapping("/arrayParam")
    public String arrayParam(String[] hobby) {
        System.out.println(Arrays.toString(hobby));

        return "haha";
    }
}
```

#### 2.使用集合接收

要求请求参数名，与形参集合对象名相同；且请求参数为多个，

要使用 `@RequestParam` 注解绑定请求参数与集合类型的形参之间的关系。

Controller 控制器：

demo-project/springboot-web-quickstart/src/main/java/com/kkcf/controller/RequestController.java

```java
package com.kkcf.controller;

import com.kkcf.pojo.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class RequestController {
    @RequestMapping("/listParam")
    public String listParam(@RequestParam List<String> hobby) {
        System.out.println(hobby);

        return "haha";
    }
}
```

### 4.日期参数处理

在一些特殊的需求中，可能会涉及到请求传递日期类型的数据。比如：

- `localhost:8080/dateParam?updateTime=2024-09-06 11:05:33`

日期的格式多种多样（如：2022-12-12 10:05:45 、2022/12/12 10:05:45）；

#### 1.@DateTimeFormat 注解

那么对于日期类型的参数，在进行封装的时候，需要通过 `@DateTimeFormat` 注解，以及其 `pattern` 属性来设置日期的格式。

- 客户端请求传递的日期参数，必须按照指定的格式传递。
- 服务器端 Controller 控制器的方法中，要使用 Date 或 LocalDateTime 类型，来接收传递的参数。
- 请求参数的名称，与 Controller 控制器的方法中形参名称相同。

demo-project/springboot-web-quickstart/src/main/java/com/kkcf/controller/RequestController.java

```java
package com.kkcf.controller;

import com.kkcf.pojo.User;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
public class RequestController {
    @RequestMapping("/dateParam")
    public String dateParam(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime updateTime) {
        System.out.println(updateTime);

        return "haha";
    }
}
```

### 5.JSON 参数处理

客户端请求传递 JSON 格式的参数，通常使用 POST 请求，比如：

- `localhost:8080/jsonParam`

并在请求体中携带如下 json 参数：

```json
{
  "name": "zzt",
  "age": 18,
  "address": {
    "province": "广东",
    "city": "深圳"
  }
}
```

#### 1.@RequestBody 注解

服务器端 Controller 控制器中的方法，处理 JSON 类型的参数：

- 一般使用实体类，接收 JSON 格式的参数。SON数据键名与形参对象属性名相同，定义POJO类型形参即可接收参数。
- 要使用 `@RequestBody` 标识形参。

Controller 控制器

demo-project/springboot-web-quickstart/src/main/java/com/kkcf/controller/RequestController.java

```java
package com.kkcf.controller;

import com.kkcf.pojo.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestController {
    @RequestMapping("/jsonParam")
    public String jsonParam(@RequestBody User user) {
        System.out.println(user);

        return "haha";
    }
}
```

### 6.路径（路由）参数处理

实际开发中，经常会直接在请求的 URL 中，传递参数。比如：

- `localhost:880/pathParam/1/zzt`

#### 1.@PathVariable 注解

服务器端 Controller 控制器的处理方法，要使用 `@RequestMapping` 和 `@PathVariable` 注解来处理

```java
package com.kkcf.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class RequestController {
    @RequestMapping("/pathParam/{id}/{name}")
    public String pathParam(@PathVariable Integer id, @PathVariable String name) {
        System.out.println(id + " " + name);

        return "haha";
    }
}
```