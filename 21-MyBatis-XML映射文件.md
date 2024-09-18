# MyBatis XML 映射文件

MyBatis 开发，有两种方式：

- 注解
- XML 映射文件。

Mybatis 的注解方式，主要是来完成一些简单的增、删、改、查功能。

如果需要执行复杂的 SQL 功能，建议使用 XML 映射文件来配置 SQL 语句，也就是将 SQL 语句，写在 XML 配置文件中。

MyBatis 中的 XML 配置文件，又称为 XML 映射文件。

## 一、MyBatis XML 映射文件规范

在 Mybatis 中，使用 XML 映射文件，需要符合下面规范：

1. 一个 Mapper 接口，对应一个 XML 映射文件。
2. XML 映射文件的名称，与  Mapper 接口名称一致；
   - 并且将 XML 映射文件和 Mapper 接口，分别放置在 resources 目录相 java 目录同包下（同包同名）
3. XML 映射文件中，`<mapper>` 标签的 `namespace` 属性值，与 Mapper 接口全限定名保持一致。
4. XML 映射文件中， SQL 语句对应的标签（比如 `<select>` 标签）的
   - `id` 属性值，与 Mapper 接口中的方法名保持一致；
   - `resultType` 属性值，与返回类型的全限定名保持一致。

`<select>` 标签：就是用于编写 SELECT 查询语句的。

- 其 `resultType` 属性，指的是查询返回的单条记录，所对应的类型。

## 二、MyBatis XML 映射文件操作数据库

使用 MyBatis XML 映射文件的方式，执行查询语句：

第一步，创建 Mapper 接口 `EmpMapper`；在其中定义一个方法 `selectEmpById`；

demo-project/springbot-mybatis-quickstart/src/main/java/com/kkcf/mapper/EmpMapper.java

```java
package com.kkcf.mapper;

import com.kkcf.pojo.Emp;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;

@Mapper
public interface EmpMapper {
    /*@Results({
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
            @Result(column = "dept_id", property = "deptId"),
    })
    @Select("SELECT id, username, password, name, gender, image, job, entrydate, dept_id, create_time, update_time FROM emp WHERE id = #{id};")
    Emp selectEmpById(Integer id);*/
    Emp selectEmpById(Integer id);
}
```

第二步：创建 XML 映射文件

- Maven 项目中，要在 resources 目录下，与 EmpMapper 同名同包的路径下创建。即下方目录：
- demo-project/springbot-mybatis-quickstart/src/main/resources/com/kkcf/mapper/EmpMapper.xml

> IDEA 的 Maven 项目，在 resource 文件夹中，创建目录，以 `/` 分隔

第三步：编写 XML 映射文件的约束

- MyBatis XML 映射文件的约束，可在[官网](https://mybatis.org/mybatis-3/zh_CN/getting-started.html#%E6%8E%A2%E7%A9%B6%E5%B7%B2%E6%98%A0%E5%B0%84%E7%9A%84-sql-%E8%AF%AD%E5%8F%A5)中复制下来。

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
```

第四步：配置 `<Mapper>` 标签上的的 `namespace` 属性为 Mapper 接口全限定名

demo-project/springbot-mybatis-quickstart/src/main/resources/com/kkcf/mapper/EmpMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.kkcf.mapper.EmpMapper">

</mapper>
```

第五步：配置 SQL 语句对应的标签（比如 `<select>` 标签）的 `id`，与 Mapper 接口中的方法名一致，并保持返回类型一致。

在其中编写 SQL 语句。

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.kkcf.mapper.EmpMapper">
    <!-- resultType 指的是单条记录对应的类型-->
    <select id="selectEmpById" resultType="com.kkcf.pojo.Emp">
        SELECT id, username, password, name, gender, image, job, entrydate, dept_id, create_time, update_time FROM emp
        WHERE id = #{id};
    </select>
</mapper>
```

第六步：执行单元测试，完成查询操作。

demo-project/springbot-mybatis-quickstart/src/test/java/com/kkcf/SpringbotMybatisQuickstartApplicationTests.java

```java
package com.kkcf;

import com.kkcf.mapper.EmpMapper;
import com.kkcf.pojo.Emp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest // Spring Boot 整合单元测试的注解：类中的单元测试方法在运行时，会自动加载 Spring Boot 环菌，并创建 IOC 容器。
class SpringbotMybatisQuickstartApplicationTests {
    @Autowired
    private EmpMapper empMapper;

    @Test
    public void testSelectEmpById() {
        Emp emp = empMapper.selectEmpById(18);

        System.out.println(emp);
    }
}
```

> 在 IDEA 中，使用 [MyBatisX](https://plugins.jetbrains.com/plugin/10119-mybatisx) 插件，使得 MyBatis 开发更加高效。
