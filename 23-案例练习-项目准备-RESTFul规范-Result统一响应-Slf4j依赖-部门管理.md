# 案例练习之部门管理、RESTFul 规范、统一响应 Result、Slf4j 依赖

## 一、准备工作

### 1.1.数据库准备

准备数据库，表结构，数据。

```mysql
USE javawebdb;

-- 部门管理
DROP TABLE IF EXISTS dept;
CREATE TABLE dept
(
    id          INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name        VARCHAR(10) NOT NULL UNIQUE COMMENT '部门名称',
    create_time DATETIME    NOT NULL COMMENT '创建时间',
    update_time DATETIME    NOT NULL COMMENT '修改时间'
) COMMENT '部门表';
-- 部门表测试数据
INSERT INTO dept (id, name, create_time, update_time)
VALUES (1, '学工部', NOW(), NOW()),
       (2, '教研部', NOW(), NOW()),
       (3, '咨询部', NOW(), NOW()),
       (4, '就业部', NOW(), NOW()),
       (5, '人事部', NOW(), NOW());


-- 员工管理(带约束)
DROP TABLE IF EXISTS emp;
CREATE TABLE emp
(
    id          INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    username    VARCHAR(20)      NOT NULL UNIQUE COMMENT '用户名',
    password    VARCHAR(32) DEFAULT '123456' COMMENT '密码',
    name        VARCHAR(10)      NOT NULL COMMENT '姓名',
    gender      TINYINT UNSIGNED NOT NULL COMMENT '性别, 说明: 1 男, 2 女',
    image       VARCHAR(300) COMMENT '图像',
    job         TINYINT UNSIGNED COMMENT '职位, 说明: 1 班主任,2 讲师, 3 学工主管, 4 教研主管, 5 咨询师',
    entrydate   DATE COMMENT '入职时间',
    dept_id     INT UNSIGNED COMMENT '部门ID',
    create_time DATETIME         NOT NULL COMMENT '创建时间',
    update_time DATETIME         NOT NULL COMMENT '修改时间'
) COMMENT '员工表';
-- 员工表测试数据
INSERT INTO emp
(id, username, password, name, gender, image, job, entrydate, dept_id, create_time, update_time)
VALUES (1, 'jinyong', '123456', '金庸', 1, '1.jpg', 4, '2000-01-01', 2, NOW(), NOW()),
       (2, 'zhangwuji', '123456', '张无忌', 1, '2.jpg', 2, '2015-01-01', 2, NOW(), NOW()),
       (3, 'yangxiao', '123456', '杨逍', 1, '3.jpg', 2, '2008-05-01', 2, NOW(), NOW()),
       (4, 'weiyixiao', '123456', '韦一笑', 1, '4.jpg', 2, '2007-01-01', 2, NOW(), NOW()),
       (5, 'changyuchun', '123456', '常遇春', 1, '5.jpg', 2, '2012-12-05', 2, NOW(), NOW()),
       (6, 'xiaozhao', '123456', '小昭', 2, '6.jpg', 3, '2013-09-05', 1, NOW(), NOW()),
       (7, 'jixiaofu', '123456', '纪晓芙', 2, '7.jpg', 1, '2005-08-01', 1, NOW(), NOW()),
       (8, 'zhouzhiruo', '123456', '周芷若', 2, '8.jpg', 1, '2014-11-09', 1, NOW(), NOW()),
       (9, 'dingminjun', '123456', '丁敏君', 2, '9.jpg', 1, '2011-03-11', 1, NOW(), NOW()),
       (10, 'zhaomin', '123456', '赵敏', 2, '10.jpg', 1, '2013-09-05', 1, NOW(), NOW()),
       (11, 'luzhangke', '123456', '鹿杖客', 1, '11.jpg', 5, '2007-02-01', 3, NOW(), NOW()),
       (12, 'hebiweng', '123456', '鹤笔翁', 1, '12.jpg', 5, '2008-08-18', 3, NOW(), NOW()),
       (13, 'fangdongbai', '123456', '方东白', 1, '13.jpg', 5, '2012-11-01', 3, NOW(), NOW()),
       (14, 'zhangsanfeng', '123456', '张三丰', 1, '14.jpg', 2, '2002-08-01', 2, NOW(), NOW()),
       (15, 'yulianzhou', '123456', '俞莲舟', 1, '15.jpg', 2, '2011-05-01', 2, NOW(), NOW()),
       (16, 'songyuanqiao', '123456', '宋远桥', 1, '16.jpg', 2, '2007-01-01', 2, NOW(), NOW()),
       (17, 'chenyouliang', '123456', '陈友谅', 1, '17.jpg', NULL, '2015-03-21', NULL, NOW(), NOW());
```

### 1.2.项目创建

创建 Spring Boot Web 结合 MyBatis 的项目：

- 引入 Spring Boot 起步依赖：
  - spring-boot-starter-web
  - spring-boot-starter-test

- 引入 Spring Boot 整合 Mybatis 的起步依赖：
  - mybatis-spring-boot-starter
  - mybatis-spring-boot-starter-test

- 引入 MySQL 驱动
  - mysql-connector-j

- 引入 Lombok 依赖。
  - lombok

在 application.properties 配置文件中。配置数据库连接信息。

准备 Controller、Service、Mapper 层的基础结构。

## 二、RESTFul 开发规范

本项目使用 RESTful 规范。

REST（Representational State Transfer）表述性状态转换，是一种软件架构风格。它与传统规范的区别如下：

传统请求 URL：

```sh
http://localhost:8080/user/getById?id=1     GET：查询 id 为 1 的用户
http://localhost:8080/user/saveUser         POST：新增用户
http://localhost:8080/user/updateUser       POST：修改用户
http://localhost:8080/user/deleteUser?id=1  GET：删除 id 为 1 的用户
```

REST 风格 URL

```sh
http://localhost:8080/users/1  GET：查询 id 为 1 的用户
http://localhost:8080/users    POST：新增用户
http://localhost:8080/users    PUT/PATCH：修改用户
http://localhost:8080/users/1  DELETE：删除 id 为 1 的用户
```

可以看到，基于 REST 风格，定义的 URL，更加简洁、更加规范、更加优雅。

在 REST 风格的 URL中，通过四种请求方式，来对数据进行增、删、改、查操作。

- GET：查询
- POST：新增
- PUT / PATCH ：修改
- DELETE：删除

RESTFul 风格，两点注意：

- REST 是风格，是约定方式，不是规定，可以打破。
- 描述模块的功能，通常使用名词复数，即名词后加 `s` 来描述；表示的是此类资源，而非单个资源。
  - 比如：`users`、`emps`、`books`…

总结：RESTFul 风格，通过 URL 定位要操作的资源，通过 HTTP 动词（请求方式）来描述具体的操作。

### 1.@RequestMapping 注解指定请求方式

在 Controller 控制器类中，定义了请求处理方法；

使用 `@RequestMapping` 注解，指定请求方式（比如：GET、POST…），有两种方式：

- 方式一，使用 `@RequestMapping` 注解的 `method` 属性：

  ```java
  @RequestMapping(value = "/depts", method = RequestMethod.GET)
  ```

- 方式二：使用 `@RequestMapping` 注解的衍生注解，比如：

  ```java
  @GetMapping("/depts")
  ```

### 2.@RequestMapping 抽取公共路径

`@RequestMapping` 注解在类上使用，用于抽取控制器类中处理请求方法的公共路径。

demo-project/javaweb-practise/src/main/java/com/kkcf/controller/EmpController.java

```java
@RequestMapping("/emps")
public class EmpController {
    // ……
}
```

## 三、统一响应 Result 类

统一响应结果 `Result` 类的实现：

demo-project/javaweb-practise/src/main/java/com/kkcf/pojo/Result.java

```java
package com.kkcf.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;// 响应码，1 代表成功; 0 代表失败
    private String msg;  // 响应码 描述字符串
    private T data; // 返回的数据

    /**
     * 此静态方法用于：创建一个成功响应
     *
     * @return 成功响应
     */
    public static <T> Result<T> success() {
        return new Result<T>(1, "success", null);
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
    public static <T> Result<T> error(String msg) {
        return new Result<T>(0, msg, null);
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

## 四、slf4j 依赖

使用 Logback 依赖记录日志，引入 Slf4j 依赖。

demo-project/javaweb-practise/pom.xml

```xml
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
</dependency>
```

Lombok 提供了注解 `@Slf4j`，在类上使用，用于在类中定义日志记录的静态常量对象。

```java
public static final Logger log = LoggerFactory.getLogger(DeptController.class);
```

Slf4j 的使用：

- `{}` 用于占位符。

```java
log.info("查询结果：{}", result);
```

## 五、部门管理

### 5.1.部门列表查询

#### 5.1.1.部门列表查询基本信息

请求路径：/depts

请求方式：GET

接口描述：该接口用于部门列表数据查询

#### 5.1.2.部门列表查询请求参数

无

#### 5.1.3.部门列表查询响应数据

参数格式：application/json

参数说明：

| 参数名         | 类型      | 是否必须 | 备注                           |
| -------------- | --------- | -------- | ------------------------------ |
| code           | number    | 必须     | 响应码，1 代表成功，0 代表失败 |
| msg            | string    | 非必须   | 提示信息                       |
| data           | object[ ] | 非必须   | 返回的数据                     |
| \|- id         | number    | 非必须   | id                             |
| \|- name       | string    | 非必须   | 部门名称                       |
| \|- createTime | string    | 非必须   | 创建时间                       |
| \|- updateTime | string    | 非必须   | 修改时间                       |

响应数据样例：

```json
{
  "code": 1,
  "msg": "success",
  "data": [
    {
      "id": 1,
      "name": "学工部",
      "createTime": "2022-09-01T23:06:29",
      "updateTime": "2022-09-01T23:06:29"
    },
    {
      "id": 2,
      "name": "教研部",
      "createTime": "2022-09-01T23:06:29",
      "updateTime": "2022-09-01T23:06:29"
    }
  ]
}
```

#### 5.1.4.部门列表查询接口开发

创建 `DeptMapper` 接口，在其中添加 `selectDepts` 方法：

demo-project/javaweb-practise/src/main/java/com/kkcf/mapper/DeptMapper.java

```java
package com.kkcf.mapper;

import com.kkcf.pojo.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DeptMapper {
    @Select("SELECT id, name, create_time, update_time FROM dept")
    List<Dept> selectDepts();
}
```

创建 Service 层接口 `DeptService`：在其中添加 `list` 方法。

demo-project/javaweb-practise/src/main/java/com/kkcf/service/impl/DeptServiceImpl.java

```java
package com.kkcf.service;

import com.kkcf.pojo.Dept;

import java.util.List;

public interface DeptService {
    List<Dept> list();
}
```

创建 Service 层实现类 `DeptServiceImpl`；在其中实现 `list` 方法。

demo-project/javaweb-practise/src/main/java/com/kkcf/service/impl/DeptServiceImpl.java

```java
package com.kkcf.service.impl;

import com.kkcf.mapper.DeptMapper;
import com.kkcf.pojo.Dept;
import com.kkcf.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptMapper deptMapper;

    @Override
    public List<Dept> list() {
        return deptMapper.selectDepts();
    }
}
```

创建 Controller 层控制器类 `DeptController`；在其中定义 `list` 方法。

demo-project/javaweb-practise/src/main/java/com/kkcf/controller/DeptController.java

```java
package com.kkcf.controller;

import com.kkcf.pojo.Dept;
import com.kkcf.pojo.Result;
import com.kkcf.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class DeptController {
    @Autowired
    private DeptService deptService;

    @GetMapping("/depts")
    public Result<List<Dept>> list() {
        log.info("查询全部部门信息");

        List<Dept> depts = deptService.list();
        log.info("查询结果：{}", depts);

        return Result.success(depts);
    }
}
```

### 5.2.删除部门

#### 5.2.1.删除部门基本信息

> 请求路径：/depts/{id}
>
> 请求方式：DELETE
>
> 接口描述：该接口用于根据 ID 删除部门数据

#### 5.2.2.删除部门请求参数

参数格式：路径参数

参数说明：

| 参数名 | 类型   | 是否必须 | 备注    |
| ------ | ------ | -------- | ------- |
| id     | number | 必须     | 部门 ID |

请求参数样例：

```url
/depts/1
```

#### 5.2.3.删除部门响应数据

参数格式：application/json

参数说明：

| 参数名 | 类型   | 是否必须 | 备注                           |
| ------ | ------ | -------- | ------------------------------ |
| code   | number | 必须     | 响应码，1 代表成功，0 代表失败 |
| msg    | string | 非必须   | 提示信息                       |
| data   | object | 非必须   | 返回的数据                     |

响应数据样例：

```json
{
    "code":1,
    "msg":"success",
    "data":null
}
```

#### 5.2.4.删除部门接口开发

在 `DeptMapper` 接口中，定义 `deleteDeptById` 方法：

demo-project/javaweb-practise/src/main/java/com/kkcf/mapper/DeptMapper.java

```java
package com.kkcf.mapper;

import com.kkcf.pojo.Dept;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DeptMapper {
    @Delete("DELETE FROM dept WHERE id = #{id}")
    int deleteDeptById(Integer id);
}
```

在 `DeptService` 接口中，定义 `removeById` 方法：

demo-project/javaweb-practise/src/main/java/com/kkcf/service/DeptService.java

```java
package com.kkcf.service;

import com.kkcf.pojo.Dept;

import java.util.List;

public interface DeptService {
    List<Dept> list();

    int removeById(Integer id);
}
```

在 `DeptServiceImpl` 实现类中，实现 `removeById` 方法：

demo-project/javaweb-practise/src/main/java/com/kkcf/service/impl/DeptServiceImpl.java

```java
package com.kkcf.service.impl;

import com.kkcf.mapper.DeptMapper;
import com.kkcf.pojo.Dept;
import com.kkcf.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptMapper deptMapper;

    @Override
    public int removeById(Integer id) {
        return deptMapper.deleteDeptById(id);
    }
}
```

在控制器类 `DeptController` 中，定义 `removeById` 方法：

demo-project/javaweb-practise/src/main/java/com/kkcf/controller/DeptController.java

```java
package com.kkcf.controller;

import com.kkcf.pojo.Dept;
import com.kkcf.pojo.Result;
import com.kkcf.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class DeptController {
    @Autowired
    private DeptService deptService;

    /**
     * 此方法用于：删除部门信息，根据部门 Id
     *
     * @param id 部门 Id
     * @return Result
     */
    @DeleteMapping("/depts/{id}")
    public Result removeById(@PathVariable Integer id) {
        log.info("删除部门信息，id：{}", id);
        int i = deptService.removeById(id);

        return i > 0 ? Result.success(null)
                : Result.error("删除部门失败", null);
    }
}
```

### 5.3.添加部门

#### 5.3.1.添加部门基本信息

> 请求路径：/depts
>
> 请求方式：POST
>
> 接口描述：该接口用于添加部门数据

#### 5.3.2.添加部门请求参数

格式：application/json

参数说明：

| 参数名 | 类型   | 是否必须 | 备注     |
| ------ | ------ | -------- | -------- |
| name   | string | 必须     | 部门名称 |

请求参数样例：

```json
{
  "name": "销售部"
}
```

#### 5.3.3.添加部门响应数据

参数格式：application/json

参数说明：

| 参数名 | 类型   | 是否必须 | 备注                           |
| ------ | ------ | -------- | ------------------------------ |
| code   | number | 必须     | 响应码，1 代表成功，0 代表失败 |
| msg    | string | 非必须   | 提示信息                       |
| data   | object | 非必须   | 返回的数据                     |

响应数据样例：

```json
{
    "code":1,
    "msg":"success",
    "data":null
}
```

#### 5.3.4.添加部门接口开发

在 `DeptMapper` 接口中，定义 `insertDept` 方法：

demo-project/javaweb-practise/src/main/java/com/kkcf/mapper/DeptMapper.java

```java
package com.kkcf.mapper;

import com.kkcf.pojo.Dept;
import org.apache.ibatis.annotations.Insert;

@Mapper
public interface DeptMapper {
    @Insert("INSERT INTO dept(name, create_time, update_time) VALUES (#{name}, #{createTime}, #{updateTime});")
    int insertDept(Dept dept);
}
```

在 Service 层接口 DeptService 中，定义方法 `addDept`。

demo-project/javaweb-practise/src/main/java/com/kkcf/service/DeptService.java

```java
package com.kkcf.service;

import com.kkcf.pojo.Dept;

import java.util.List;

public interface DeptService {
    int addDept(Dept dept);
}
```

在 Service 层实现类 `DeptServiceImpl` 中，实现方法 `addDept`：

demo-project/javaweb-practise/src/main/java/com/kkcf/service/impl/DeptServiceImpl.java

```java
package com.kkcf.service.impl;

import com.kkcf.mapper.DeptMapper;
import com.kkcf.pojo.Dept;
import com.kkcf.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptMapper deptMapper;

    @Override
    public int addDept(Dept dept) {
        // 补充数据
        dept.setCreateTime(LocalDate.now());
        dept.setUpdateTime(LocalDate.now());
        return deptMapper.insertDept(dept);
    }
}
```

在 Controller 层的 `DeptController` 类中，定义方法 `addDept`

demo-project/javaweb-practise/src/main/java/com/kkcf/controller/DeptController.java

```java
package com.kkcf.controller;

import com.kkcf.pojo.Dept;
import com.kkcf.pojo.Result;
import com.kkcf.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/depts")
public class DeptController {
    @Autowired
    private DeptService deptService;

    /**
     * 从方法用于：新增部门信息
     * @param dept 新增部门信息
     * @return Result
     */
    @PostMapping
    public Result<Null> addDept(@RequestBody Dept dept) {
        log.info("新增部门信息：{}", dept);

        int i = deptService.addDept(dept);
        return i > 0 ? Result.success(null)
                : Result.error("新增部门失败", null);
    }
}
```

### 5.4.根据 ID 查询（回显）

#### 5.4.1.根据 ID 查询基本信息

> 请求路径：/depts/{id}
>
> 请求方式：GET
>
> 接口描述：该接口用于根据 ID 查询部门数据

#### 5.4.2.根据 ID 查询请求参数

参数格式：路径参数

参数说明：

| 参数名 | 类型   | 是否必须 | 备注    |
| ------ | ------ | -------- | ------- |
| id     | number | 必须     | 部门 ID |

请求参数样例：

```url
/depts/1
```

#### 5.4.3.根据 ID 查询响应数据

参数格式：application/json

参数说明：

| 参数名         | 类型   | 是否必须 | 备注                           |
| -------------- | ------ | -------- | ------------------------------ |
| code           | number | 必须     | 响应码，1 代表成功，0 代表失败 |
| msg            | string | 非必须   | 提示信息                       |
| data           | object | 非必须   | 返回的数据                     |
| \|- id         | number | 非必须   | id                             |
| \|- name       | string | 非必须   | 部门名称                       |
| \|- createTime | string | 非必须   | 创建时间                       |
| \|- updateTime | string | 非必须   | 修改时间                       |

响应数据样例：

```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "id": 1,
    "name": "学工部",
    "createTime": "2022-09-01T23:06:29",
    "updateTime": "2022-09-01T23:06:29"
  }
}
```

#### 5.4.4.根据 ID 查询接口开发

在 `DeptMapper` 接口中，定义 `selectDeptById` 方法：

demo-project/javaweb-practise/src/main/java/com/kkcf/mapper/DeptMapper.java

```java
package com.kkcf.mapper;

import com.kkcf.pojo.Dept;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DeptMapper {
    @Select("SELECT id, name, create_time, update_time FROM dept WHERE id = #{id}")
    Dept selectDeptById(Integer id);
}
```

在 Service 层接口 `DeptService` 中，定义 `getById` 方法

demo-project/javaweb-practise/src/main/java/com/kkcf/service/DeptService.java

```java
package com.kkcf.service;

import com.kkcf.pojo.Dept;

import java.util.List;

public interface DeptService {
    Dept getById(Integer id);
}
```

在 Service 层实现类 `DeptServiceImpl` 中，实现 `getById` 方法：

demo-project/javaweb-practise/src/main/java/com/kkcf/service/impl/DeptServiceImpl.java

```java
package com.kkcf.service.impl;

import com.kkcf.mapper.DeptMapper;
import com.kkcf.pojo.Dept;
import com.kkcf.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptMapper deptMapper;

    @Override
    public Dept getById(Integer id) {
        return deptMapper.selectDeptById(id);
    }
}
```

在 Controller 层的 `DeptController` 类中，定义处理方法 `getById`:

demo-project/javaweb-practise/src/main/java/com/kkcf/controller/DeptController.java

```java
package com.kkcf.controller;

import com.kkcf.pojo.Dept;
import com.kkcf.pojo.Result;
import com.kkcf.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/depts")
public class DeptController {
    @Autowired
    private DeptService deptService;

    /**
     * 此方法用于：根据部门 Id 查询部门信息
     *
     * @param id 部门 Id
     * @return Result<Dept>
     */
    @GetMapping("/{id}")
    public Result<Dept> getById(@PathVariable Integer id) {
        log.info("根据 id 查询部门信息，id：{}", id);

        Dept dept = deptService.getById(id);
        return dept != null ? Result.success(dept)
                : Result.error("根据 id 查询部门信息失败", null);
    }
}
```

### 5.5.修改部门

#### 5.5.1.修改部门基本信息

> 请求路径：/depts
>
> 请求方式：PATCH
>
> 接口描述：该接口用于修改部门数据

#### 5.5.2.修改部门请求参数

格式：application/json

参数说明：

| 参数名 | 类型   | 是否必须 | 备注     |
| ------ | ------ | -------- | -------- |
| id     | number | 必须     | 部门ID   |
| name   | string | 必须     | 部门名称 |

请求参数样例：

```json
{
  "id": 1,
  "name": "教研部"
}
```

#### 5.5.3.修改部门响应数据

参数格式：application/json

参数说明：

| 参数名 | 类型   | 是否必须 | 备注                           |
| ------ | ------ | -------- | ------------------------------ |
| code   | number | 必须     | 响应码，1 代表成功，0 代表失败 |
| msg    | string | 非必须   | 提示信息                       |
| data   | object | 非必须   | 返回的数据                     |

响应数据样例：

```json
{
    "code":1,
    "msg":"success",
    "data":null
}
```

在 `DeptMapper` 接口中，定义方法 `updateDept`。

demo-project/javaweb-practise/src/main/java/com/kkcf/mapper/DeptMapper.java

```java
package com.kkcf.mapper;

import com.kkcf.pojo.Dept;
import org.apache.ibatis.annotations.*;

@Mapper
public interface DeptMapper {
    @Update("UPDATE dept SET name = #{name}, update_time = #{updateTime} WHERE id = #{id}")
    int updateDept(Dept dept);
}
```

在 Service 层的 `DeptService` 接口中，定义方法 `updateDept`：

demo-project/javaweb-practise/src/main/java/com/kkcf/service/DeptService.java

```java
package com.kkcf.service;

import com.kkcf.pojo.Dept;

import java.util.List;

public interface DeptService {
    int updateDept(Dept dept);
}
```

在 Service 层的 `DeptServiceImpl` 实现类中，实现方法 `updateDept`。

demo-project/javaweb-practise/src/main/java/com/kkcf/service/impl/DeptServiceImpl.java

```java
package com.kkcf.service.impl;

import com.kkcf.mapper.DeptMapper;
import com.kkcf.pojo.Dept;
import com.kkcf.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptMapper deptMapper;

    @Override
    public int updateDept(Dept dept) {
        dept.setUpdateTime(LocalDate.now());
        return deptMapper.updateDept(dept);
    }
}
```

在 Controller 层的控制器类 `DeptController` 中，定义方法 `updateDept`。

demo-project/javaweb-practise/src/main/java/com/kkcf/controller/DeptController.java

```java
package com.kkcf.controller;

import com.kkcf.pojo.Dept;
import com.kkcf.pojo.Result;
import com.kkcf.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/depts")
public class DeptController {
    @Autowired
    private DeptService deptService;

    /**
     * 此方法用于：修改部门信息
     *
     * @param dept 修改部门信息
     * @return Result<Null>
     */
    @PatchMapping
    public Result<Null> updateDept(@RequestBody Dept dept) {
        log.info("修改部门信息：{}", dept);

        int i = deptService.updateDept(dept);
        return i > 0 ? Result.success(null)
                : Result.error("修改部门信息失败", null);
    }
}
```
