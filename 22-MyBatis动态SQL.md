# MyBatis 动态 SQL

实际开发中，在查询数据时，如果条件为空，一般表示不传。

上文条件查询的接口方法 `selectEmpByCondition` 中，编写的 SQL 语句，将条件直接写死了。

- 如果只传递了参数 name，其它两个字段 gender 和 entrydate 不传，那么这两个参数的值就是 null。这个查询结果，是不正确的。
- 正确的做法应是：传递了参数，再组装这个查询条件；没有传递参数，就不应该组装这个查询条件。

比如：如果姓名输入了"张"，其它表单字段没有填写，那么对应的 SQL 应为：

```mysql
SELECT *
FROM emp
WHERE name LIKE '%张%'
ORDER BY update_time DESC;
```

比如：姓名输入了"张"，性别选择了"男"，其它表单字段没有填写，则对应的 SQL 应为:

```mysql
SELECT *
FROM emp
WHERE name LIKE '%张%'
  AND gender = 1
ORDER BY update_time DESC;
```

SQL 语句，会随着用户的输入，或外部条件的变化，而变化，称为：**动态SQL**。

在 Mybatis 中，提供了很多实现动态 SQL 的标签，学习 Mybatis 中的动态 SQL，就是掌握这些动态 SQL 标签。

## 一、if 标签

`<if>` 标签：用于判断条件是否成立。使用 `test` 属性，进行条件判断：

- 如果条件为 `true`，则拼接 SQL。
- 如果条件为 `false`，则不会拼接 SQL。

`<if>` 标签，语法如下：

```xml
<if test="条件表达式">
   要拼接的 SQL 语句
</if>
```

案例理解：在 XML 映射文件中，把 SQL 语句，改造为动态 SQL 方式

原 SQL 语句：

demo-project/springbot-mybatis-quickstart/src/main/resources/com/kkcf/mapper/EmpMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.kkcf.mapper.EmpMapper">
    <select id="selectEmpByCondition" resultType="com.kkcf.pojo.Emp">
        SELECT id, username, password, name, gender, image, job, entrydate, dept_id, create_time, update_time
        FROM emp
        WHERE name LIKE CONCAT('%', #{name}, '%')
            AND gender = #{gender}
            AND entrydate BETWEEN #{startDate} AND #{endDate}
        ORDER BY entrydate DESC;
    </select>
</mapper>
```

动态 SQL 语句：

demo-project/springbot-mybatis-quickstart/src/main/resources/com/kkcf/mapper/EmpMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.kkcf.mapper.EmpMapper">
    <select id="selectEmpByCondition" resultType="com.kkcf.pojo.Emp">
        SELECT id, username, password, name, gender, image, job, entrydate, dept_id, create_time, update_time
        FROM emp
        WHERE
        <if test="name != null">
            name LIKE CONCAT('%', #{name}, '%')
        </if>
        <if test="gender != null">
            AND gender = #{gender}
        </if>
        <if test="startDate != null and endDate != null">
            AND entrydate BETWEEN #{startDate} AND #{endDate}
        </if>
        ORDER BY entrydate DESC;
    </select>
</mapper>
```

- `<if>` 标签的 `test` 属性，其中的关键字（比如 `and`）都是小写的。

单元测试：

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
    public void testSelectEmpByCondition() {
        //List<Emp> emps = empMapper.selectEmpByCondition("张", (short) 1, LocalDate.of(2010, 1, 1), LocalDate.of(2020, 1, 1));
        List<Emp> emps = empMapper.selectEmpByCondition("张", (short) 1, null, null);

        emps.forEach(System.out::println);
    }
}
```

修改测试方法中的代码如下，

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
    public void testSelectEmpByCondition() {
        //List<Emp> emps = empMapper.selectEmpByCondition("张", (short) 1, LocalDate.of(2010, 1, 1), LocalDate.of(2020, 1, 1));
        List<Emp> emps = empMapper.selectEmpByCondition(null, (short) 1, null, null);

        emps.forEach(System.out::println);
    }
}
```

再次进行测试，发现报错了：

```sh
Caused by: java.sql.SQLSyntaxErrorException: You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near 'AND gender = 1
```

> 框架的报错信息，要从下往上看。

这是因为 SQL 语句中，多用了 `AND` 关键字。

为解决这类问题，就要使用 `<where>` 标签。

## 二、where 标签

`<where>` 标签：

- 只会在子标签有内容的情况下，才插入 `WHERE` 子句；
- 而且会自动去除子句开头的 `AND` 或 `OR` 关键字。

使用 `<where>` 标签，对上方 XML 映射文件，进行优化：

demo-project/springbot-mybatis-quickstart/src/main/resources/com/kkcf/mapper/EmpMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.kkcf.mapper.EmpMapper">
    <select id="selectEmpByCondition" resultType="com.kkcf.pojo.Emp">
        SELECT id, username, password, name, gender, image, job, entrydate, dept_id, create_time, update_time
        FROM emp
        <where>
            <if test="name != null">
                name LIKE CONCAT('%', #{name}, '%')
            </if>
            <if test="gender != null">
                AND gender = #{gender}
            </if>
            <if test="startDate != null and endDate != null">
                AND entrydate BETWEEN #{startDate} AND #{endDate}
            </if>
        </where>
        ORDER BY entrydate DESC;
    </select>
</mapper>
```

- `<where>` 标签下，第一个 `<if>` 子标签中的子句，没有 `AND` 关键字。

## 三、set 标签

`<set>` 标签：

- 会为动态 SQL 语句（`UPDATE` 语句）中，插入 `SET` 关键字；
- 并会删掉额外的 `,` 逗号。

案例理解：编写动态 SQL，实现更新员工功能，

- 如果更新时，有对字段传值，则更新；否则，不更新。

修改 `EmpMapper` 接口中的方法 `updateEmpById`

demo-project/springbot-mybatis-quickstart/src/main/java/com/kkcf/mapper/EmpMapper.java

```java
package com.kkcf.mapper;

import com.kkcf.pojo.Emp;
import org.apache.ibatis.annotations.*;

@Mapper
public interface EmpMapper {
    /*@Update("UPDATE emp SET username = #{username}, name = #{name}, gender = #{gender}, image = #{image}, job = #{job}, entrydate = #{entrydate}, dept_id = #{deptId}, update_time = #{updateTime} WHERE id = #{id};")
    int updateEmpById(Emp emp);*/
    int updateEmpById(Emp emp);
}
```

在 XML 映射文件中，编写 `<update>` 标签中的 SQL 语句，动态更新员工信息。

demo-project/springbot-mybatis-quickstart/src/main/resources/com/kkcf/mapper/EmpMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.kkcf.mapper.EmpMapper">
    <update id="updateEmpById">
        UPDATE emp
        <set>
            <if test="username != null">
                username = #{username},
            </if>
            <if test="name != null">
                name = #{name},
            </if>
            <if test="gender != null">
                gender = #{gender},
            </if>
            <if test="image != null">
                image = #{image},
            </if>
            <if test="job != null">
                job = #{job},
            </if>
            <if test="entrydate != null">
                entrydate = #{entrydate},
            </if>
            <if test="deptId != null">
                dept_id = #{deptId},
            </if>
            <if test="updateTime != null">
                update_time = #{updateTime}
            </if>
        </set>
        WHERE id = #{id};
    </update>
</mapper>
```

> IDEA 中的 MyBatisX 插件，会根据方法名，自动生成补全 XML 映射文件。
>
> - 方法名中有 update，就生成 UPDATE 语句；
> - 方法名中有 select，就生成 SELECT 语句；
> - ……

## 四、foreach 标签

`<foreach>` 标签，用于遍历 Mapper 接口方法中，传递的集合/数组类型的参数。

案例理解：批量删除员工功能实现。

SQL 语句：

```mysql
DELETE
FROM emp
WHERE id IN (1, 2, 3);
```

在 `EmpMapper` 中，定义方法 `deleteEmpByIds`

demo-project/springbot-mybatis-quickstart/src/main/java/com/kkcf/mapper/EmpMapper.java

```java
package com.kkcf.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EmpMapper {
    int deleteEmpByIds(List<Integer> ids);
}
```

`<foreach>` 标签，语法如下：

```xml
<foreach collection="集合名称" item="集合遍历出来的元素/项" separator="每一次遍历使用的分隔符"
         open="遍历开始前拼接的片段" close="遍历结束后拼接的片段">
</foreach>
```

在 XML 映射文件：使用 `<foreach>` 标签，遍历 `deleteEmpByIds` 方法中传递的参数 `ids` 集合

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.kkcf.mapper.EmpMapper">
    <delete id="deleteEmpByIds">
        DELETE FROM emp WHERE id IN
        <foreach collection="ids" item="id" separator="," open="(" close=")">
            #{id}
        </foreach>
    </delete>
</mapper>
```

单元测试：

demo-project/springbot-mybatis-quickstart/src/test/java/com/kkcf/SpringbotMybatisQuickstartApplicationTests.java

```java
package com.kkcf;

import com.kkcf.mapper.EmpMapper;
import com.kkcf.pojo.Emp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest // Spring Boot 整合单元测试的注解：类中的单元测试方法在运行时，会自动加载 Spring Boot 环菌，并创建 IOC 容器。
class SpringbotMybatisQuickstartApplicationTests {
    @Autowired
    private EmpMapper empMapper;

    @Test
    public void testDeleteEmpByIds() {
        int i = empMapper.deleteEmpByIds(List.of(18, 19));

        System.out.println(i); // 2
    }
}
```

## 五、sql、include 标签

在 XML 映射文件中，配置的 SQL，有时可能会存在很多重复的片段；

可以对重复的代码片段，进行抽取：

- `<sql>` 标签：通过属性 `id`，定义可重用的 SQL 片段

- `<include>` 标签：通过属性 `refid`，指定包含的 SQL 片段

它们是配套使用的。用于抽取，和使用 SQL 片段。

现有一个 XML 映射文件。

demo-project/springbot-mybatis-quickstart/src/main/resources/com/kkcf/mapper/EmpMapper.xml

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

    <select id="selectEmpByCondition" resultType="com.kkcf.pojo.Emp">
        SELECT id, username, password, name, gender, image, job, entrydate, dept_id, create_time, update_time FROM emp
        <where>
            <if test="name != null">
                name LIKE CONCAT('%', #{name}, '%')
            </if>
            <if test="gender != null">
                AND gender = #{gender}
            </if>
            <if test="startDate != null and endDate != null">
                AND entrydate BETWEEN #{startDate} AND #{endDate}
            </if>
        </where>
        ORDER BY entrydate DESC;
    </select>
</mapper>
```

将上方 XML 映射文件中，相同的 SQL 片段，进行抽取和复用：

demo-project/springbot-mybatis-quickstart/src/main/resources/com/kkcf/mapper/EmpMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.kkcf.mapper.EmpMapper">
    <sql id="commonSelect">
        SELECT id, username, password, name, gender, image, job, entrydate, dept_id, create_time, update_time FROM emp
    </sql>

    <!-- resultType 指的是单条记录对应的类型-->
    <select id="selectEmpById" resultType="com.kkcf.pojo.Emp">
        <include refid="commonSelect"/>
        WHERE id = #{id};
    </select>

    <select id="selectEmpByCondition" resultType="com.kkcf.pojo.Emp">
        <include refid="commonSelect"/>
        <where>
            <if test="name != null">
                name LIKE CONCAT('%', #{name}, '%')
            </if>
            <if test="gender != null">
                AND gender = #{gender}
            </if>
            <if test="startDate != null and endDate != null">
                AND entrydate BETWEEN #{startDate} AND #{endDate}
            </if>
        </where>
        ORDER BY entrydate DESC;
    </select>
</mapper>
```
