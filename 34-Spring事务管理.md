# Spring 事务管理

## 一、事务是什么（回顾）

在数据库阶段，已经介绍过事务；

**事务**是一组操作的集合，它是一个不可分割的工作单位。

事务会把所有的操作作为一个整体，一起向数据库提交或者是撤销操作请求。所以这组操作要么同时成功，要么同时失败。

事务的操作主要有三步：

1. 一组操作开始前，开启事务：start transaction / begin ;
2. 这组操作全部成功后，提交事务：commit ;
3. 中间任何一个操作出现异常，回滚事务：rollback ;

## 二、Spring 中的事务

案例：解散部门；

需求：当部门解散后，不仅需要把部门信息删除，还需要把该部门下的员工数据也删除了。

实现步骤：

1. 根据部门 ID 删除部门数据；
2. 根据部门 ID 删除该部门下的员工。

在 EmpMapper 接口中，新增 `deleteByDeptId` 方法，用于根据部门 ID 删除员工。

使用注解的方式，执行 SQL 语句。

demo-project/javaweb-practise/src/main/java/com/kkcf/mapper/EmpMapper.java

```java
package com.kkcf.mapper;

import com.kkcf.pojo.Emp;
import org.apache.ibatis.annotations.Delete;

@Mapper
public interface EmpMapper {
    @Delete("DELETE FROM emp WHERE dept_id = #{deptId}")
    void deleteByDeptId(int detpId);
}
```

在 Service 层的 DeptServiceImpl 实现类的 `removeById` 方法中，补充根据部门 ID 删除员工的逻辑：

- 在逻辑中，模拟一个算数运算（运行时）异常 `int i = 1/0`。

```java
package com.kkcf.service.impl;

import com.kkcf.mapper.DeptMapper;
import com.kkcf.mapper.EmpMapper;
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int removeById(Integer id) throws Exception {
        // 删除部门
        deptMapper.deleteDeptById(id);

        int i = 1 / 0;

        // 删除部门下的员工
        empMapper.deleteByDeptId(id);
    }
}
```

重启服务，使用接口测试工具，访问删除部门的接口；发现部门删除了，但员工未删除。造成了数据的不一致

为了解决这个问题，需要在 Spring Boot 项目中，开启事务。

## 三、@Transactional 注解

Spring 框架，已封装好了事务控制，只需通过一个注解 @Transactional 来开启，提交，回滚事务。

- 在逻辑执行开始之前，开启事务；
- 逻辑执行完毕之后，提交事务；
- 如果在这个逻辑执行的过程中，出现了异常，就会进行事务的回滚操作。

@Transactional 注解，可在方法，类，接口上使用。用于

- 方法：当前方法交给 Spring 进行事务管理；
- 类：当前类中所有的方法，都交给 Spring 进行事务管理；
- 接口：接口下所有的实现类中所有的方法，都交给 Spring 进行事务管理。

@Transactional 注解：一般用在 Service（业务）层方法上，来控制事务。

- 因为在 Service（业务）层中，一个业务功能，可能会包含多个数据访问的操作（增、删、改）。

案例理解：在 Service 层的 DeptServiceImpl 类中的 `removeById` 方法上，加上 @Transactional 注解。

demo-project/javaweb-practise/src/main/java/com/kkcf/service/impl/DeptServiceImpl.java

```java
package com.kkcf.service.impl;

import com.kkcf.mapper.DeptMapper;
import com.kkcf.mapper.EmpMapper;
import com.kkcf.pojo.Dept;
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

    @Transactional
    @Override
    public int removeById(Integer id) {
        // 删除部门下的员工
        empMapper.deleteByDeptId(id);

        int i = 1 / 0; // 模拟出现异常

        // 删除部门
        return deptMapper.deleteDeptById(id);
    }
}
```

在配置文件 application.yml 中，开启 Spring 事务管理的日志；

即可在控制台看到和事务相关的日志信息。

demo-project/javaweb-practise/src/main/resources/application.yml

```yaml
#spring 事务管理日志
logging:
  level:
    org.springframework.jdbc.support.JdbcTransactionManager: debug
```

使用接口测试工具，访问部门删除接口，发现部门和部门下的员工都未删除。说明事务回滚了。

@Transactional 注解中，有两个常见的属性：

### 1.rollbackFor 属性，指定异常类型的回滚

@Transactional 注解，在默认情况下，只会在**运行时（RuntimeException）异常**出现时，进行事务回滚。

使用 `rollbackFor` 属性，进行配置：

- `@Transactional(rollbackFor = Exception.class)`；指定**所有异常类型**，都会进行回滚。

在 DeptServiceImpl 类的 `removeById` 方法中，模拟编译时异常出现。

demo-project/javaweb-practise/src/main/java/com/kkcf/service/impl/DeptServiceImpl.java

```java
package com.kkcf.service.impl;

import com.kkcf.mapper.DeptMapper;
import com.kkcf.mapper.EmpMapper;
import com.kkcf.pojo.Dept;
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int removeById(Integer id) throws Exception {
        // 删除部门下的员工
        empMapper.deleteByDeptId(id);

        if (true) {
            throw new Exception("出错啦……");
        }

        // 删除部门
        return deptMapper.deleteDeptById(id);
    }
}
```

使用接口测试工具，访问部门删除接口，发现部门和部门下的员工都未删除。表示事务回滚了。

### 2.propagation  属性，指定事务传播行为

`propagation` 属性，用于配置事务的传播行为。

事务的传播行为，指的是当一个事务方法，被另一个事务方法调用时，这个事务方法应该如何进行事务控制。

比如：A 方法，B 方法。都添加了 @Transactional 注解，表示这两个方法都具有事务，而在 A 方法中又去调用了 B 方法。

![事务的传播行为](NoteAssets/事务的传播行为.png)

- A 方法运行的时候，首先会开启一个事务，
- B 方法自身也具有事务；
- A 方法中调用了 B 方法，那么 B 方法在运行时，到底是加入 A 方法的事务中，还是新建一个事务？
- 这就涉及到了事务的传播行为。

在 @Transactional 注解中，使用属性 `propagation`，来指定传播行为。

常见的事务传播行为有

| 属性值               | 含义                                                         |
| -------------------- | ------------------------------------------------------------ |
| REQUIRED（默认）     | 默认值，需要事务，有则加入，无则创建新事务                   |
| REQUIRES_NEW（常用） | 需要新事务，无论有无，总是创建新事务                         |
| SUPPORTS             | 支持事务，有则加入，无则在无事务状态中运行                   |
| NOT_SUPPORTED        | 不支持事务，在无事务状态下运行,如果当前存在已有事务,则挂起当前事务 |
| MANDATORY            | 必须有事务，否则抛异常                                       |
| NEVER                | 必须没事务，否则抛异常                                       |
| ……                   | ……                                                           |

- `REQUIRED`：大部分情况下用该传播行为。
- `REQUIRES_NEW`：不希望事务之间相互影响时，使用该传播行为。
  - 比如：下订单前需要记录日志，不论订单保存成功与否，都需要保证日志记录能够记录成功。

案例理解：解散部门时，需要记录操作日志；

要求：无论操作是否成功，都要记录日志。

实现步骤：

1. 执行解散部门的业务：先删除部门，再删除部门下的员工（已实现）；
2. 记录解散部门的日志，到日志表（未实现）。

在数据库中，创建 dept_log 表，用于记录部门操作日志：

```mysql
DROP TABLE IF EXISTS dept_log;
CREATE TABLE IF NOT EXISTS dept_log
(
    id          INT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    content     TEXT     NOT NULL COMMENT '日志内容'
) COMMENT '部门日志表';
```

创建实体（pojo）类 DeptLog：

demo-project/javaweb-practise/src/main/java/com/kkcf/pojo/DeptLog.java

```java
package com.kkcf.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeptLog {
    private Integer id;
    private LocalDate createTime;
    private String description;
}
```

在 Mapper 层，创建接口 DeptLogMapper，使用注解的方式处理 SQL。

demo-project/javaweb-practise/src/main/java/com/kkcf/mapper/DeptLogMapper.java

```java
package com.kkcf.mapper;

import com.kkcf.pojo.DeptLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeptLogMapper {
    @Insert("INSERT INTO dept_log (create_time, description) VALUES (#{createTime}, #{description})")
    int insert(DeptLog deptLog);
}
```

在 Service 层中，创建接口 DeptLogService

demo-project/javaweb-practise/src/main/java/com/kkcf/service/DeptLogService.java

```java
package com.kkcf.service;

import com.kkcf.pojo.DeptLog;

public interface DeptLogService {
    int add(DeptLog deptLog);
}
```

在 Service 层，创建 DeptLogServiceImpl 类，实现 DeptLogService 接口。

- 使用 `@Transactional` 注解的 `propagation` 属性，指定事务传播行为。

demo-project/javaweb-practise/src/main/java/com/kkcf/service/impl/DeptLogServiceImpl.java

```java
package com.kkcf.service.impl;

import com.kkcf.mapper.DeptLogMapper;
import com.kkcf.pojo.DeptLog;
import com.kkcf.service.DeptLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeptLogServiceImpl implements DeptLogService {
    @Autowired
    private DeptLogMapper deptLogMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public int add(DeptLog deptLog) {
        return deptLogMapper.insert(deptLog);
    }
}
```

在 Service 层的 DeptServiceImpl 中，实现删除部门，记录日志的逻辑：

- 删除部门的操作，无论成功与否，都要进行日志记录。
- 使用 try…finally 代码块，将记录日志的逻辑，放在 finally 代码块中。

demo-project/javaweb-practise/src/main/java/com/kkcf/service/impl/DeptServiceImpl.java

```java
package com.kkcf.service.impl;

import com.kkcf.mapper.DeptLogMapper;
import com.kkcf.mapper.DeptMapper;
import com.kkcf.mapper.EmpMapper;
import com.kkcf.pojo.Dept;
import com.kkcf.pojo.DeptLog;
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int removeById(Integer id) throws Exception {
        try {
            // 删除部门下的员工
            empMapper.deleteByDeptId(id);

            // 模拟bi
            if (true) {
                throw new Exception("出错啦……");
            }

            // 删除部门
            return deptMapper.deleteDeptById(id);
        } finally {
            DeptLog deptLog = new DeptLog();
            deptLog.setCreateTime(LocalDate.now());
            deptLog.setDescription("执行了解散部门的操作，此次解散的部门是 " + id + " 号部门");

            deptLogService.add(deptLog);
        }
    }
}
```

> IDEA 插件 [Grep Console](https://plugins.jetbrains.com/plugin/7125-grep-console) 用于高亮展示指定的日志。

DeptServiceImpl 中 `removeById` 方法运行时，会开启一个事务。

当调用 `deptLogService.add(deptLog)` 时，也会创建一个新的事务，

当 `add` 方法运行完毕后，事务就已经提交了。 即使外部的事务出现异常，内部已经提交的事务也不会回滚，

因为这是两个独立的事务。
