# Spring Boot 响应处理

Controller 控制器中的方法，除了接收请求外，还可以返回响应。

在前面所编写的 Controller 控制器的方法中，都设置了响应数据。即 `return` 语句返回的结果，

这体现了 `@ResponseBody` 注解的作用。

## 一、@ResponseBody 注解

`@ResponseBody` 注解，可以是类注解，或方法注解；

- 可以写在 Controller 控制器类上，也可以写在其中的方法上。

`@ResponseBody` 注解，作用是将方法返回值，直接响应给客户端；

- 如果返回值类型，是实体（pojo）对象、集合；将会转换为 JSON 格式后，再响应给浏览器。

事实上，在前面编写的 Controller 控制器中，并没有写 `@ResponseBody` 注解。

因为在 Controller 控制器类上，添加的 `@RestController` 注解，它是一个组合注解。

- `@RestController` = `@Controller` + `@ResponseBody`

Spring Boot 的 `@RestController` 注解源码如下：

RestController.class

```java
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.springframework.web.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller
@ResponseBody
public @interface RestController {
    @AliasFor(
        annotation = Controller.class
    )
    String value() default "";
}
```

定义一个 Controller 控制器类 `ResponseController`。

demo-project/springboot-web-quickstart/src/main/java/com/kkcf/controller/ResponseController.java

```java
package com.kkcf.controller;

import com.kkcf.pojo.Address;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ResponseController {
    @RequestMapping("/hi")
    public String hi() {
        return "hi!";
    }

    @RequestMapping("/addr")
    public Address addr() {
        return new Address("广东省", "深圳市");
    }

    @RequestMapping("/listAddr")
    public List<Address> listAddr() {
        return new ArrayList<Address>(List.of(
                new Address("广东省", "深圳市"),
                new Address("广东省", "广州市"),
                new Address("广东省", "珠海市")
        ));
    }
}
```

启动 Spring Boot 项目；

在客户端分别发送如下 GET 请求，进行测试：分别返回了如下响应数据：

`localhost:8080/hi`

```txt
hi!
```

`localhost:8080/addr`

```json
{
    "province": "广东省",
    "city": "深圳市"
}
```

`localhost:8080/listAddr`

```json
[
    {
        "province": "广东省",
        "city": "深圳市"
    },
    {
        "province": "广东省",
        "city": "广州市"
    },
    {
        "province": "广东省",
        "city": "珠海市"
    }
]
```

## 二、统一响应结果

在 Controller 控制器中，使用 `@RequestMapping` 注解的方法，都是一个功能接口。注解中的参数，就是接口路径。

真实的项目开发中，会针对所有的功能接口，定义一个统一的返回结果。

统一的返回结果使用类来描述，在这个结果中包含：

- 响应状态码：当前请求是成功，还是失败。

- 状态码信息：给页面的提示信息。

- 返回的数据：给前端响应的数据（比如：字符串、对象、集合、……）。

Result 实体类：

demo-project/springboot-web-quickstart/src/main/java/com/kkcf/pojo/Result.java

```java
package com.kkcf.pojo;

public class Result<T> {
    private Integer code;//响应码，1 代表成功; 0 代表失败
    private String msg;  //响应码 描述字符串
    private T data; //返回的数据

    public Result() {
    }

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 此静态方法用于；创建一个成功响应
     *
     * @param data 响应的数据
     * @return 成功想要
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>(1, "success", data);
    }

    /**
     * 此静态方法用于：创建一个失败响应
     *
     * @param msg 失败原因
     * @return 失败响应
     */
    public static <T> Result<T> error(String msg, T data) {
        return new Result<T>(0, msg, data);
    }
}
```

重构上方的 Controller 控制器：

demo-project/springboot-web-quickstart/src/main/java/com/kkcf/controller/ResponseController.java

```java
package com.kkcf.controller;

import com.kkcf.pojo.Address;
import com.kkcf.pojo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ResponseController {
    @RequestMapping("/hi")
    public Result<String> hi() {
        return Result.success("hi");
    }

    @RequestMapping("/addr")
    public Result<Address> addr() {
        return Result.success(new Address("广东省", "深圳市"));
    }

    @RequestMapping("/listAddr")
    public Result<List<Address>> listAddr() {
        return Result.success(new ArrayList<Address>(List.of(
                new Address("广东省", "深圳市"),
                new Address("广东省", "广州市"),
                new Address("广东省", "珠海市")
        )));
    }
}
```

## 三、案例练习

加载并解析 XML 文件中的员工数据，完成数据处理，并返回给页面展示。

步骤一；在 `pom.xml` 配置文件中，引入 `dom4j` 依赖，用于解析 XML 文件

demo-project/springboot-web-quickstart/pom.xml

```xml
<dependency>
    <groupId>org.dom4j</groupId>
    <artifactId>dom4j</artifactId>
    <version>2.1.3</version>
</dependency>
```

- 在 IDEA 中，点击 `pom.xml` 配置文件中的刷新按钮，生效修改。

步骤二：引入以下文件：

- `XMLParserUtils` 工具类，用于解析 XML 文件中的数据。demo-project/springboot-web-quickstart/src/main/java/com/kkcf/utils/XmlParserUtils.java
- `Emp` 实体（pojo）类，用于封装员工（Employee）的信息。demo-project/springboot-web-quickstart/src/main/java/com/kkcf/pojo/Emp.java
- `emp.xml` 文件，用于存储员工信息。demo-project/springboot-web-quickstart/src/main/resources/emp.xml

> 基于 Maven 的 Spring Boot 项目，静态资源文件，一般放在 `src/main/resources` 目录下。

步骤三：引入提供的静态页面文件，放在 `src/main/resources/static`

- `emp.html` 静态页面。demo-project/springboot-web-quickstart/src/main/resources/static/emp.html

这里使用老旧的 Vue2 和 ElementUI 做简单的演示：

```vue
<script>
    new Vue({
        el: "#app",
        data() {
            return {
                tableData: []
            }
        },
        mounted() {
            axios.get('/listEmp').then(res => {
                if (res.data.code) {
                    this.tableData = res.data.data;
                }
            });
        },
        methods: {}
    });
</script>
```

- 可见，发送了一个 GET 请求 `/listEmp'`

> 在 Spring Boot 项目中，静态资源默认可以存放的目录：
>
> - classpath:/static/
> - classpath:/public/
> - classpath:/resources/
> - classpath:/META-INF/resources/
>
> `classpath`：代表的是类路径，在 Maven 项目中，指的就是 `src/main/resources` 或者 `src/main/java`
>
> - `src/main/java` 目录是存放 java 代码的。
> - 所以配置文件、静态资源文档，应该放在 `src/main/resources`下。

步骤四：创建 `EmpController` 控制器类，编写 Controller 程序，处理请求，响应数据。

demo-project/springboot-web-quickstart/src/main/java/com/kkcf/controller/EmpController.java

```java
package com.kkcf.controller;

import com.kkcf.pojo.Emp;
import com.kkcf.pojo.Result;
import com.kkcf.utils.XmlParserUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class EmpController {
    @RequestMapping("/listEmp")
    public Result<List<Emp>> listEmp() {
        // 加载并解析 emp.xml 文件
        ClassLoader classLoader = this.getClass().getClassLoader(); // 获取类加载器
        String file = Objects.requireNonNull(classLoader.getResource("emp.xml")).getFile();
        System.out.println("file: " + file);
        List<Emp> empList = XmlParserUtils.parse(file, Emp.class);

        // 对数据进行处理
        empList.forEach(emp -> {
            // 性别
            String gender = emp.getGender();
            String genderStr = switch (gender) {
                case "1" -> "男";
                case "2" -> "女";
                default -> "";
            };
            emp.setGender(genderStr);

            // 工作
            String job = emp.getJob();
            String jobStr = switch (job) {
                case "1" -> "讲师";
                case "2" -> "班主任";
                case "3" -> "就业指导";
                default -> "";
            };
            emp.setJob(jobStr);
        });

        return Result.success(empList);
    }
}
```

在浏览器地址栏中，访问 `http://localhost:8080/emp.html`，请求静态资源；

其中加载的 JS 代码，会发送请求 `/listEmp` 获取数据，并展示在页面上。
