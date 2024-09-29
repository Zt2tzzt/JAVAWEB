# Spring Aop

AOP 是 Spring 框架的第二大核心，

## Aop 是什么

AOP（Aspect Oriented Programming）面向切面编程，说白了，就是面向特定方法编程。 

面向一个，或多个方法进行编程。

场景：项目中开发了很多的业务功能，计算每一个业务方法的耗时。有两种解决方案；

- 方案一：为每一个业务方法，添加计时的逻辑。这种做法繁琐，会让代码显得臃肿。
- 方案二：AOP 面向切面编程。

AOP 面向方法编程，可在程序运行期间，在不修改源代码的基础上，对已有方法进行无侵入性的增强（解耦）。

要想完成统计各个业务方法执行耗时的需求，只需要定义一个模板方法，将记录方法执行耗时这一部分公共的逻辑代码，定义在模板方法当中，在这个方法开始运行之前，来记录这个方法运行的开始时间，在方法结束运行的时候，再来记录方法运行的结束时间，中间就来运行原始的业务方法。





模板方法中对应的逻辑，就是创建出来的代理对象方法的逻辑。



AOP 面向切面编程是一种思想；

这种思想最主流的实现方式，就是动态代理。



## Spring AOP 的使用

案例理解：使用 Spring AOP，统计各个业务方法的耗时。

1.引入依赖

demo-project/javaweb-practise/pom.xml

```xml
<!-- Spring Boot AOP-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

2.编写 AOP 程序，针对特定方法，根据业务需求进行编程。

demo-project/javaweb-practise/src/main/java/com/kkcf/aop/TimeAspect.java

```java
package com.kkcf.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class TimeAspect {
    @Around("execution(* com.kkcf.service.*.*(..))") // 切入点表达式。
    public Object recordTime(ProceedingJoinPoint pjp) throws Throwable {
        // 1.记录开始时间
        long start = System.currentTimeMillis();

        // 2.执行目标方法
        Object result = pjp.proceed();

        // 3.记录结束时间
        long end = System.currentTimeMillis();
        log.info("{} 方法耗时：{} ms", pjp.getSignature(), end - start);

        return result;
    }
}
```

在接口测试工具中，调用登录接口。

观察控制台输出：

```sh
2024-09-29T14:07:01.207+08:00  INFO 20504 --- [javaweb-practise] [nio-8080-exec-1] com.kkcf.aop.TimeAspect: Emp com.kkcf.service.impl.EmpServiceImpl.loginEmp(Emp) 方法耗时：658 ms
```



AOP 可以有很多应用场景，比如：

- 记录操作日志；
- 权限控制；
- 事务管理
- ……

> Spring 事务管理，底层就是使用 AOP 来实现的。



## AOP 的优势



## AOP 核心概念

连接点：JoinPoint，指的是可以被 AOP 控制的方法（暗含方法执行时的相关信息）

入门程序中，所有方法，都是可以被 AOP 控制的方法，所有方法都是连接点。



通知：Advice，指的是重复的逻辑，也就是共性功能（最终体现未一个方法）。



切入点：PointCut，指的是匹配连接点的条件。通知仅会在切入点方法执行时被应用。



通知与切入点结合在一起，就形成了一个切面。切面描述了当前 AOP 程序，针对于那个原始方法，在什么时候执行什么样的操作。

即通知与切人点的对应关系。

@Aspect 注解标识的类，一般称为切面类。



目标对象：通知应用的对象，称为目标对象。



## AOP 的执行流程

基于目标对象，生成代理对象。

Spring 底层另外的代理技术 CGLIB 动态代理。



## AOP 的通知类型

五种通知类型：

@After 也称最终通知

```java
package com.kkcf.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class MyAspect {
    @Around("execution(* com.kkcf.service.impl.DeptServiceImpl.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        log.info(" AOP ADVICE around before...");

        Object result = pjp.proceed();

        log.info(" AOP ADVICE around after...");

        return result;
    }

    @Before("execution(* com.kkcf.service.impl.DeptServiceImpl.*(..))")
    public void before() {
        log.info(" AOP ADVICE before...");
    }

    @After("execution(* com.kkcf.service.impl.DeptServiceImpl.*(..))")
    public void after() {
        log.info(" AOP ADVICE after...");
    }

    @AfterReturning("execution(* com.kkcf.service.impl.DeptServiceImpl.*(..))")
    public void afterReturning() {
        log.info(" AOP ADVICE afterReturning...");
    }

    @AfterThrowing("execution(* com.kkcf.service.impl.DeptServiceImpl.*(..))")
    public void afterThrowing() {
        log.info(" AOP ADVICE afterThrowing...");
    }
}
```

AfterThrowing 和 AfterRunning 是互斥的。

Around 环绕通知。



注意事项。



使用 @PointCut 注解，将切入点表达式抽取出来

可以在其它类中，使用抽取出来的切入点表达式。

```java
package com.kkcf.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class MyAspect {
    @Pointcut("execution(* com.kkcf.service.impl.DeptServiceImpl.*(..))")
    public void pt() {
    }

    @Around("pt()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        log.info(" AOP ADVICE around before...");

        Object result = pjp.proceed();

        log.info(" AOP ADVICE around after...");

        return result;
    }

    @Before("pt()")
    public void before() {
        log.info(" AOP ADVICE before...");
    }

    @After("pt()")
    public void after() {
        log.info(" AOP ADVICE after...");
    }

    @AfterReturning("pt()")
    public void afterReturning() {
        log.info(" AOP ADVICE afterReturning...");
    }

    @AfterThrowing("pt()")
    public void afterThrowing() {
        log.info(" AOP ADVICE afterThrowing...");
    }
}
```

## AOP 的通知执行顺序

当多个切面的切入点，都匹配到了目标方法。目标方法运行时，多个通知都会被执行，它们的执行顺序是怎样的？

默认和类名的排序有关；

Spring 提供了 @Order 注解，来控制切面的执行顺序。

- 对于原始方法运行前的通知，数字越小，优先级越高
- 对于原始方法运行后的通知，数字越小，优先级越低



demo-project/javaweb-practise/src/main/java/com/kkcf/aop/MyAspectA.java

```java
package com.kkcf.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(2)
@Aspect
public class MyAspectA {
    @Before("com.kkcf.aop.MyAspect.pt()")
    public void before() {
        log.info("MyAspectA.before……");
    }

    @After("com.kkcf.aop.MyAspect.pt()")
    public void after() {
        log.info("MyAspectA.after……");
    }
}
```



demo-project/javaweb-practise/src/main/java/com/kkcf/aop/MyAspectB.java

```java
package com.kkcf.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(1)
@Aspect
public class MyAspectB {
    @Before("com.kkcf.aop.MyAspect.pt()")
    public void before() {
        log.info("MyAspectB.before……");
    }

    @After("com.kkcf.aop.MyAspect.pt()")
    public void after() {
        log.info("MyAspectB.after……");
    }
}
```

观察控制台日志输出：

```sh
2024-09-29T16:41:03.419+08:00  INFO 14000 --- [javaweb-practise] [nio-8080-exec-1] com.kkcf.aop.MyAspectB                   : MyAspectB.before……
2024-09-29T16:41:03.419+08:00  INFO 14000 --- [javaweb-practise] [nio-8080-exec-1] com.kkcf.aop.MyAspectA                   : MyAspectA.before……
2024-09-29T16:41:03.788+08:00  INFO 14000 --- [javaweb-practise] [nio-8080-exec-1] com.kkcf.aop.MyAspectA                   : MyAspectA.after……
2024-09-29T16:41:03.788+08:00  INFO 14000 --- [javaweb-practise] [nio-8080-exec-1] com.kkcf.aop.MyAspectB                   : MyAspectB.after……
```

## AOP 的切入点表达式

一共有两种：

execution 切入点表达式

- `包名.类名` 不建议省略；
- 可以基于接口进行匹配。

execution 切入点表达式，通配符。

- `*` 号
- `..` 号

匹配两个完全不相同的方法，使用 `||` 逻辑运算符进行连接。