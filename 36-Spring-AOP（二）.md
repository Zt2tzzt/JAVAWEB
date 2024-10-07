# Spring AOP（二）

## 一、AOP 通知执行顺序

在项目开发中，定义了多个切面类，其中多个切入点，都匹配到了同一个目标方法。

此时当目标方法，在运行的时候，这多个切面类里的通知方法都会运行。

它们哪个先运行，哪个后运行？

### 1.Spring AOP 通知默认执行顺序

定义两种类型的通知，进行测试；

- 一种是 @Before 前置通知，一种是 @After 后置通知。

定义切面类 MyAspectA

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

定义切面类 MyAspectB

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
2024-09-29T16:41:03.419+08:00  INFO 14000 --- [javaweb-practise] [nio-8080-exec-1] com.kkcf.aop.MyAspectB                   : MyAspectA.before……
2024-09-29T16:41:03.419+08:00  INFO 14000 --- [javaweb-practise] [nio-8080-exec-1] com.kkcf.aop.MyAspectA                   : MyAspectB.before……
2024-09-29T16:41:03.788+08:00  INFO 14000 --- [javaweb-practise] [nio-8080-exec-1] com.kkcf.aop.MyAspectA                   : MyAspectB.after……
2024-09-29T16:41:03.788+08:00  INFO 14000 --- [javaweb-practise] [nio-8080-exec-1] com.kkcf.aop.MyAspectB                   : MyAspectA.after……
```

可知：切面类里的通知执行顺序，默认和类名的排序有关；

### 2.@Order 注解指定执行顺序

Spring 提供了 @Order 注解，来控制切面的执行顺序。

- 对于原始方法运行前的通知，传入的数字越小，优先级越高；
- 对于原始方法运行后的通知，传入的数字越小，优先级越低。

在切面类 MyAspectA 使用 @Order 注解

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

在切面类 MyAspectB 使用 @Order 注解

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

使用接口测试工具，访问查询部门的接口，观察控制台日志输出：

```sh
2024-09-30T11:57:35.373+08:00  INFO 24216 --- [javaweb-practise] [nio-8080-exec-4] com.kkcf.aop.MyAspectB                   : MyAspectB.before……
2024-09-30T11:57:35.373+08:00  INFO 24216 --- [javaweb-practise] [nio-8080-exec-4] com.kkcf.aop.MyAspectA                   : MyAspectA.before……
2024-09-30T11:57:35.389+08:00  INFO 24216 --- [javaweb-practise] [nio-8080-exec-4] com.kkcf.aop.MyAspectA                   : MyAspectA.after……
2024-09-30T11:57:35.390+08:00  INFO 24216 --- [javaweb-practise] [nio-8080-exec-4] com.kkcf.aop.MyAspectB                   : MyAspectB.after……
```

## 二、AOP 的切入点表达式

从 AOP 的入门程序开始，一直都在使用切入点表达式，来描述切入点。

切入点表达式，用来决定项目中的哪些方法，需要加入通知。

切入点表达式，常见的有两种形式：

- execution(……)：根据方法的签名来匹配；
- @annotation(……) ：根据注解匹配；

### 1.execution 切入点表达式

#### 1.execution 语法

execution 主要根据方法的返回值、包名、类名、方法名、方法参数等等信息来匹配，语法为：

```txt
execution(访问修饰符? 返回值 包名.类名.?方法名(方法参数) throws 异常?)
```

其中带 `?` 的，表示可以省略的部分：

- 访问修饰符：可省略（比如：`public`、`protected`）；

- `包名.类名`：可省略，但不建议省略；

- throws 异常：可省略（方法上声明抛出的异常，不是实际抛出的异常）

示例：

```java
@Before("execution(void com.kkcf.service.impl.DeptServiceImpl.removeById(java.lang.Integer))")
```

execution 切入点表达式，可以基于**接口**进行匹配。

#### 2.execution 通配符

execution 切入点表达式，可用通配符有两个：

- `*` ：单个独立的任意符号，可以通配任意返回值、包名、类名、方法名、任意类型的一个参数，也可以通配包、类、方法名的一部分。
- `..` ：多个连续的任意符号，可以通配任意层级的包，或任意类型、任意个数的参数

execution 切入点表达式，一般用法：

省略方法的修饰符号

```java
execution(void com.kkcf.service.impl.DeptServiceImpl.removeById(java.lang.Integer))
```

使用 `*` 代替返回值类型

```java
execution(* com.kkcf.service.impl.DeptServiceImpl.removeById(java.lang.Integer))
```

使用 `*` 代替包名（一层包使用一个 `*`）

```java
execution(* com.kkcf.*.*.DeptServiceImpl.removeById(java.lang.Integer))
```

使用 `..` 省略包名（表示此包以及此包下的所有子包）

```java
execution(* com..DeptServiceImpl.removeById(java.lang.Integer))
```

使用 `*` 代替类名（表示任意类）

```java
execution(* com..*.removeById(java.lang.Integer))
```

使用 `*` 代替方法名（表示任意方法）

```java
execution(* com..*.*(java.lang.Integer))
```

使用 `*` 代替参数（表示一个任意类型的参数）

```java
execution(* com.kkcf.service.impl.DeptServiceImpl.removeById(*))
```

使用 `..` 省略参数（表示任意类型，任意个数的参数）

```java
execution(* com..*.*(..))
```

可以使用且（`&&`）、或（`||`）、非（`!`），来组合比较复杂的切入点表达式。

- 比如：匹配两个完全不相同的方法，使用 `||` 逻辑运算符进行连接。

```jav
execution(* com.kkcf.service.DeptService.list(..)) || execution(* com.kkcf.service.DeptService.removeById(..)))
```

在项目中，建议使用规范的业务方法命名，方便切入点表达式快速匹配。

- 比如：查询类方法都是 "find" 开头，更新类方法都是 "update" 开头

匹配 DeptServiceImpl 类中以 find 开头的方法

```java
execution(* com.kkcf.service.impl.DeptServiceImpl.find*(..)))
```

描述切入点方法，通常基于接口描述，而不是直接描述实现类，以增强拓展性

```java
execution(* com.kkcf.service.DeptService.*(..)))
```

在满足业务需要的前提下，尽量缩小切入点的匹配范围。

- 比如：包名匹配尽量不使用 ..，而是使用 * 匹配单个包。

```java
execution(* com.kkcf.*.*.DeptServiceImpl.find*(..)))
```

### 2.@annotation 切入点表达式

当要匹配多个在方法签名上无规则的方法，

- 比如：`list()` 和 `removeById()` 这两个方法。

这时使用 execution 切入点表达式来描述，就不是很方便。

因为要将两个 execution 切入点表达式，使用 `||` 组合在一起；

事实上，可以借助于 @annotation 切入点表达式，来描述这一类的切入点，简化切入点表达式的书写。

@annoation 切入点表达式实现步骤：

1. 编写自定义注解；

2. 在业务类要做为连接点的方法上，添加自定义注解。

创建一个包 anno，在其中创建自定义注解 @MyLog：

demo-project/javaweb-practise/src/main/java/com/kkcf/anno/MyLog.java

```java
package com.kkcf.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyLog {
}
```

在 Service 层的 `DeptServiceImpl` 类中的 `list()` 和 `removeById()` 方法上，使用自定义注解 @MyLog

demo-project/javaweb-practise/src/main/java/com/kkcf/service/impl/DeptServiceImpl.java

```java
package com.kkcf.service.impl;

import com.kkcf.anno.MyLog;
import com.kkcf.mapper.DeptMapper;
import com.kkcf.mapper.EmpMapper;
import com.kkcf.pojo.Dept;
import com.kkcf.pojo.DeptLog;
import com.kkcf.service.DeptLogService;
import com.kkcf.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private DeptLogService deptLogService;

    @MyLog
    @Override
    public List<Dept> list() {
        return deptMapper.selectDepts();
    }

    @MyLog
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int removeById(Integer id) throws Exception {
        try {
            // 删除部门下的员工
            empMapper.deleteByDeptId(id);

            //int i = 1 / 0;
            /*if (true) {
                throw new Exception("出错啦……");
            }*/

            // 删除部门
            deptMapper.deleteDeptById(id);
        } finally {
            DeptLog deptLog = new DeptLog();
            deptLog.setCreateTime(LocalDate.now());
            deptLog.setDescription("执行了解散部门的操作，此次解散的部门是 " + id + " 号部门");

            deptLogService.add(deptLog);
        }

        return 0;
    }
}
```

在 aop 包下，创建切面类 MyAspect1

demo-project/javaweb-practise/src/main/java/com/kkcf/aop/MyAspect1.java

```java
package com.kkcf.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class MyAspect1 {
    @Before("@annotation(com.kkcf.anno.MyLog)")
    public void before() {
        log.info("MyAspect1.before ……");
    }

    @After("@annotation(com.kkcf.anno.MyLog)")
    public void after() {
        log.info("MyAspect1.after ……");
    }
}
```

重启服务，使用接口测试工具，访问查询所有部门的接口，观察控制台日志输出：

```sh
2024-10-01T12:00:53.463+08:00  INFO 15848 --- [javaweb-practise] [nio-8080-exec-3] com.kkcf.aop.MyAspect1                   : MyAspect1.before ……
2024-10-01T12:00:53.476+08:00  INFO 15848 --- [javaweb-practise] [nio-8080-exec-3] com.kkcf.aop.MyAspect1                   : MyAspect1.after ……
```

### 3.AOP 切入点表达式总结

execution 切入点表达式：

- 根据指定的方法描述信息，来匹配切入点方法，是比较常用方式；

@annotation 切入点表达式：

- 基于注解的方式，来匹配切入点方法。
- 这种方式多一步操作（需要自定义一个注解）；
- 但是比较灵活。在要匹配的方法上，加上对应的注解即可；

## 三、AOP 连接点

前面介绍 AOP 核心概念时，提到连接点；可以简单理解为被 AOP 控制的方法。

在目标对象当中，所有的方法，都是可以被 AOP 控制的方法。

在 Spring AOP 中，连接点又特指方法的执行。

Spring 用 JoinPoint API 抽象了连接点，用它可获取方法执行时的相关信息。

- 比如：目标类名、方法名、方法参数……。

对于 @Around 通知，获取连接点信息，只能使用 `ProceedingJoinPoint` 类型；

对于其它四种通知，获取连接点信息，只能使用 `JoinPoint` 类型，它是 `ProceedingJoinPoint` 的父类型。

案例理解：在切面类 MyAspect 中的 before、around 方法中，获取连接点相关的信息

demo-project/javaweb-practise/src/main/java/com/kkcf/aop/MyAspect.java

```java
package com.kkcf.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

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

        String className = pjp.getTarget().getClass().getName(); // 获取目标类名
        log.info(" className: {}", className);

        Signature signature = pjp.getSignature(); // 获取目标方法签名
        log.info(" signature: {}", signature);

        String methhodName = pjp.getSignature().getName(); // 获取目标方法名
        log.info(" methhodName: {}", methhodName);

        Object[] args = pjp.getArgs(); // 获取目标方法运行参数
        log.info(" args: {}", Arrays.toString(args));

        Object result = pjp.proceed(args); // 执行原始方法，获取返回值。

        log.info(" AOP ADVICE around after...");

        return result;
    }

    @Before("pt()")
    public void before(JoinPoint jp) {
        log.info(" AOP ADVICE before...");
        String className = jp.getTarget().getClass().getName(); // 获取目标对象类名
        log.info(" className: {}", className);

        Signature signature = jp.getSignature(); // 获取目标方法签名
        log.info(" signature: {}", signature);

        String methodName = jp.getSignature().getName(); // 获取目标方法名
        log.info(" methodName: {}", methodName);

        Object[] args = jp.getArgs(); // 获取目标方法运行参数
        log.info(" args: {}", Arrays.toString(args));
    }
}
```

- `JoinPoint` 类是 org.aspectj.lang 包下的；
- `proceed` 方法，调用有参的方法时，要先获取参数，再将参数传递进去。
- 在 AOP 中，可对连接点对象的返回值，进行篡改。

使用接口测试工具，访问根据 Id 查询部门接口，观察控制台日志输出：

```sh
2024-10-01T12:31:01.315+08:00  INFO 21368 --- [javaweb-practise] [nio-8080-exec-2] com.kkcf.aop.MyAspect                    :  AOP ADVICE around before...
2024-10-01T12:31:01.315+08:00  INFO 21368 --- [javaweb-practise] [nio-8080-exec-2] com.kkcf.aop.MyAspect                    :  className: com.kkcf.service.impl.DeptServiceImpl
2024-10-01T12:31:01.315+08:00  INFO 21368 --- [javaweb-practise] [nio-8080-exec-2] com.kkcf.aop.MyAspect                    :  signature: Dept com.kkcf.service.impl.DeptServiceImpl.getById(Integer)
2024-10-01T12:31:01.315+08:00  INFO 21368 --- [javaweb-practise] [nio-8080-exec-2] com.kkcf.aop.MyAspect                    :  methhodName: getById
2024-10-01T12:31:01.315+08:00  INFO 21368 --- [javaweb-practise] [nio-8080-exec-2] com.kkcf.aop.MyAspect                    :  args: [1]
2024-10-01T12:31:01.315+08:00  INFO 21368 --- [javaweb-practise] [nio-8080-exec-2] com.kkcf.aop.MyAspect                    :  AOP ADVICE before...
2024-10-01T12:31:01.315+08:00  INFO 21368 --- [javaweb-practise] [nio-8080-exec-2] com.kkcf.aop.MyAspect                    :  className: com.kkcf.service.impl.DeptServiceImpl
2024-10-01T12:31:01.315+08:00  INFO 21368 --- [javaweb-practise] [nio-8080-exec-2] com.kkcf.aop.MyAspect                    :  signature: Dept com.kkcf.service.impl.DeptServiceImpl.getById(Integer)
2024-10-01T12:31:01.315+08:00  INFO 21368 --- [javaweb-practise] [nio-8080-exec-2] com.kkcf.aop.MyAspect                    :  methodName: getById
2024-10-01T12:31:01.315+08:00  INFO 21368 --- [javaweb-practise] [nio-8080-exec-2] com.kkcf.aop.MyAspect                    :  args: [1]
2024-10-01T12:31:01.676+08:00  INFO 21368 --- [javaweb-practise] [nio-8080-exec-2] com.kkcf.aop.MyAspect                    :  AOP ADVICE around after...
```

## 四、AOP 案例练习

在案例中，当访问部门管理、员工管理中的增、删、改相关功能接口时，记录详细的操作日志，并保存在数据库中，便于后期数据追踪。

日志信息包含：操作人、操作时间、执行方法的全类名、执行方法名、方法运行时参数、返回值、方法执行时长；

### 1.AOP 案例问题分析

- 项目当中增、删、改相关的方法很多；
- 针对每一个接口方法进行修改，这种做法比较繁琐；

### 2.AOP 案例实现思路

- 记录操作日志的逻辑是通用的、将它抽取出来定义在一个通知方法当中；
- 技术方案选择：通过 AOP 面向切面编程的方式，在不改动原始方法的基础上，对原始的功能进行增强。
- 通知类型选择：操作日志中，涉及到返回值和方法执行时长，所以要使用 AOP 的 @Around 环绕通知。
- 切入点描述选择：由于增、删、改方法名没有规律，所以使用 @annotation 切入点表达式，来描述切入点。

### 3.AOP 案例准备工作

在 Maven 项目的 pom.xml 文件中，引入 Spring AOP 的起步依赖；

demo-project/javaweb-practise/pom.xml

```xml
<!-- Spring Boot AOP-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

准备数据库中表结构，

```mysql
-- 操作日志表
DROP TABLE IF EXISTS operate_log;
CREATE TABLE IF NOT EXISTS operate_log
(
    id            INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    operate_user  INT UNSIGNED COMMENT '操作人',
    operate_time  DATETIME COMMENT '操作时间',
    class_name    VARCHAR(100) COMMENT '操作的类名',
    method_name   VARCHAR(100) COMMENT '操作的方法名',
    method_params VARCHAR(1000) COMMENT '方法参数',
    return_value  VARCHAR(2000) COMMENT '返回值',
    cost_time     BIGINT COMMENT '方法执行耗时, 单位:ms'
) COMMENT '操作日志表';
```

准备与表结构对应的实体（pojo）类 OperateLog：

demo-project/javaweb-practise/src/main/java/com/kkcf/pojo/OperateLog.java

```java
package com.kkcf.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperateLog {
    private Integer id; //主键ID
    private Integer operateUser; //操作人ID
    private LocalDateTime operateTime; //操作时间
    private String className; //操作类名
    private String methodName; //操作方法名
    private String methodParams; //操作方法参数
    private String returnValue; //操作方法返回值
    private Long costTime; //操作耗时
}
```

### 4.AOP 案例开发

定义 OperatorLogMapper 接口，在其中定义 insert 方法。

demo-project/javaweb-practise/src/main/java/com/kkcf/mapper/OperatorLogMapper.java

```java
package com.kkcf.mapper;

import com.kkcf.pojo.OperateLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperatorLogMapper {
    @Insert("INSERT INTO operate_log(operate_user, operate_time, class_name, method_name, method_params, return_value, cost_time) VALUES(#{operateUser},#{operateTime},#{className},#{methodName},#{methodParams},#{returnValue},#{costTime})")
    int insert(OperateLog operateLog);
}
```

在 anno 包下，自定义注解 @Log

demo-project/javaweb-practise/src/main/java/com/kkcf/anno/Log.java

```java
package com.kkcf.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
}
```

在 Service 层的业务实现类 DeptServiceImpl 和 EmpServiceImpl 里的增、删、改方法上，加上 @Log 注解。

DeptServiceImpl  业务实现类：

demo-project/javaweb-practise/src/main/java/com/kkcf/service/impl/DeptServiceImpl.java

```java
package com.kkcf.service.impl;

import com.kkcf.anno.Log;
import com.kkcf.anno.MyLog;
import com.kkcf.mapper.DeptMapper;
import com.kkcf.mapper.EmpMapper;
import com.kkcf.pojo.Dept;
import com.kkcf.pojo.DeptLog;
import com.kkcf.service.DeptLogService;
import com.kkcf.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private DeptLogService deptLogService;

    @Override
    public List<Dept> list() {
        return deptMapper.selectDepts();
    }

    @Log
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int removeById(Integer id) throws Exception {
        try {
            // 删除部门下的员工
            empMapper.deleteByDeptId(id);

            //int i = 1 / 0;
            /*if (true) {
                throw new Exception("出错啦……");
            }*/

            // 删除部门
            deptMapper.deleteDeptById(id);
        } finally {
            DeptLog deptLog = new DeptLog();
            deptLog.setCreateTime(LocalDate.now());
            deptLog.setDescription("执行了解散部门的操作，此次解散的部门是 " + id + " 号部门");

            deptLogService.add(deptLog);
        }

        return 0;
    }

    @Log
    @Override
    public int addDept(Dept dept) {
        dept.setCreateTime(LocalDate.now());
        dept.setUpdateTime(LocalDate.now());
        return deptMapper.insertDept(dept);
    }

    @Override
    public Dept getById(Integer id) {
        return deptMapper.selectDeptById(id);
    }

    @Log
    @Override
    public int updateDept(Dept dept) {
        dept.setUpdateTime(LocalDate.now());
        return deptMapper.updateDept(dept);
    }
}
```

EmpServiceImpl 业务实现类：

demo-project/javaweb-practise/src/main/java/com/kkcf/service/impl/EmpServiceImpl.java

```java
package com.kkcf.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kkcf.anno.Log;
import com.kkcf.mapper.EmpMapper;
import com.kkcf.pojo.Emp;
import com.kkcf.pojo.EmpPageBean;
import com.kkcf.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    private EmpMapper empMapper;

    @Override
    public EmpPageBean listWithPageAndCount(
            int page,
            int pageSize,
            String name,
            int gender,
            LocalDate startDate,
            LocalDate endDate
    ) {
        // 1.设置分页参数
        PageHelper.startPage(page, pageSize);

        // 2.进行分页查询
        List<Emp> list = empMapper.list(name, gender, startDate, endDate);
        Page<Emp> p = (Page<Emp>) list;

        return new EmpPageBean(p.getTotal(), p.getResult());
    }

    @Log
    @Override
    public int removeByIds(int[] ids) {
        return empMapper.deleteByIds(ids);
    }

    @Log
    @Override
    public int addEmp(Emp emp) {
        // 补充基础属性
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());

        return empMapper.insertEmp(emp);
    }

    @Override
    public Emp empById(int id) {
        return empMapper.selectById(id);
    }

    @Log
    @Override
    public int modifyEmp(Emp emp) {
        // 补充数据
        emp.setUpdateTime(LocalDateTime.now());

        return empMapper.updateEmp(emp);
    }

    @Override
    public Emp loginEmp(Emp emp) {
        return empMapper.selectByLogin(emp);
    }
}
```

在 asp 包下，定义切面类 LogAspect，完成记录日志的逻辑：

demo-project/javaweb-practise/src/main/java/com/kkcf/aop/LogAspect.java

```java
package com.kkcf.aop;

import com.alibaba.fastjson.JSONObject;
import com.kkcf.mapper.OperatorLogMapper;
import com.kkcf.pojo.OperateLog;
import com.kkcf.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Component
@Aspect
public class LogAspect {
    @Autowired
    private OperatorLogMapper operatorLogMapper;
    @Autowired
    private HttpServletRequest request;

    @Around("@annotation(com.kkcf.anno.Log)")
    public Object recordLog(ProceedingJoinPoint pjp) throws Throwable {
        // 获取操作人 Id，解析 JWT 令牌
        String token = request.getHeader("token");
        Claims claims = JwtUtil.parseToken(token);
        Integer operatorUserId = (Integer) claims.get("id");

        // 操作时间
        LocalDateTime operateTime = LocalDateTime.now();

        // 操作类名
        String className = pjp.getTarget().getClass().getName();

        // 操作方法名
        String methodName = pjp.getSignature().getName();

        // 操作方法参数
        Object[] args = pjp.getArgs();
        String methodParams = Arrays.toString(args);

        long begin = System.currentTimeMillis();
        Object res = pjp.proceed(args); // 调用原始方法
        long end = System.currentTimeMillis();
        long costTime = end - begin;

        // 方法返回值（使用 fastJSON 依赖将 java 对象转为 JSON 对象）
        String resJSONStr = JSONObject.toJSONString(res);

        // 记录操作日志
        OperateLog operateLog = new OperateLog(null, operatorUserId, operateTime, className, methodName, methodParams, resJSONStr, costTime);
        operatorLogMapper.insert(operateLog);

        log.info("记录操作日志：{}", operateLog);

        return res;
    }
}
```

- 在 Spring AOP 切面类中，要获取请求对象，可以直接注入一个 HttpServletRequest 对象。

重启服务，使用接口测试工具，访问新增部门、更新部门两个接口，观察数据库 operate_log 表中插入的记录。
