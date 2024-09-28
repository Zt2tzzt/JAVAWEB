# Spring 事务管理

事务是什么？



事务的操作



案例，删除部门 ，并删除部门下的所有员工

在 service 层的 DeptServiceImpl 实现类的 removeById 方法中，补充逻辑：



在 EmpMapper 接口中，新增 deleteByDeptId 方法，使用注解的方式，执行 SQL 语句。

在 DeptServiceImpl 类的 removeById 方法中，执行删除部门，删除部门逻辑的方法。

在逻辑中，模拟一个异常 `int i = 1/0`

在逻辑开始前，开启事务；在逻辑结束后 ，提交事务；如果逻辑执行失败，则回滚事务。



@Transactional 是 Spring 提供的事务管理注解。

可在方法，类，接口上使用。

一般在 Service（业务）层，执行多次数据库操作的方法（增、删、改）上，来控制事务。

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

        int i = 1 / 0;

        // 删除部门
        return deptMapper.deleteDeptById(id);
    }
}
```

在配置文件中，开启 Spring 事务管理的日志开关。

demo-project/javaweb-practise/src/main/resources/application.yml

```yaml
#spring事务管理日志
logging:
  level:
    org.springframework.jdbc.support.JdbcTransactionManager: debug
```



@Transactional 注解的使用细节。

在默认情况下，该注解只会在 RuntimeException 出现时，进行事务回滚。

使用 rollbackFor 属性，进行配置。

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



propagation 属性，配置事务的传播行为。

当一个事务方法，被另一个事务方法控制时，这个事务方法，应该如何进行事务控制。

常见的事务传播行为有：

- 只需掌握两个



案例理解：解散部门操作

要求：无论操作是否成功，都要记录日志。

使用 try…finally 代码块，讲记录日志的逻辑，放在 finally 代码块中。

> IDEA 插件 [Grep Console](https://plugins.jetbrains.com/plugin/7125-grep-console) 用于高亮展示特定的日志。

demo-project/javaweb-practise/src/main/java/com/kkcf/mapper/DeptLogMapper.java

```java
package com.kkcf.mapper;

import com.kkcf.pojo.DeptLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeptLogMapper {
    @Insert("INSERT INTO dept_log(create_time, description) VALUES (#{createTime}, #{description})")
    int insert(DeptLog deptLog);
}
```

demo-project/javaweb-practise/src/main/java/com/kkcf/service/DeptLogService.java

```java
package com.kkcf.service;

import com.kkcf.pojo.DeptLog;

public interface DeptLogService {
    int add(DeptLog deptLog);
}
```

demo-project/javaweb-practise/src/main/java/com/kkcf/service/impl/DeptLogServiceImpl.java

```java
package com.kkcf.service.impl;

import com.kkcf.mapper.DeptLogMapper;
import com.kkcf.pojo.DeptLog;
import com.kkcf.service.DeptLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeptLogServiceImpl implements DeptLogService {
    @Autowired
    private DeptLogMapper deptLogMapper;

    @Transactional
    @Override
    public int add(DeptLog deptLog) {
        return deptLogMapper.insert(deptLog);
    }
}
```

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
