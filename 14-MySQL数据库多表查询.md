# MySQL 数据库多表查询

数据准备：创建部门表 dept 和员工表 emp，并初始化数据。

```mysql
-- 部门管理
CREATE TABLE dept
(
    id          INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name        VARCHAR(10) NOT NULL UNIQUE COMMENT '部门名称',
    create_time DATETIME    NOT NULL COMMENT '创建时间',
    update_time DATETIME    NOT NULL COMMENT '修改时间'
) COMMENT '部门表';

INSERT INTO dept (id, name, create_time, update_time)
VALUES (1, '学工部', now(), now()),
       (2, '教研部', now(), now()),
       (3, '咨询部', now(), now()),
       (4, '就业部', now(), now()),
       (5, '人事部', now(), now());

-- 员工管理
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

INSERT INTO emp
(id, username, password, name, gender, image, job, entrydate, dept_id, create_time, update_time)
VALUES (1, 'jinyong', '123456', '金庸', 1, '1.jpg', 4, '2000-01-01', 2, now(), now()),
       (2, 'zhangwuji', '123456', '张无忌', 1, '2.jpg', 2, '2015-01-01', 2, now(), now()),
       (3, 'yangxiao', '123456', '杨逍', 1, '3.jpg', 2, '2008-05-01', 2, now(), now()),
       (4, 'weiyixiao', '123456', '韦一笑', 1, '4.jpg', 2, '2007-01-01', 2, now(), now()),
       (5, 'changyuchun', '123456', '常遇春', 1, '5.jpg', 2, '2012-12-05', 2, now(), now()),
       (6, 'xiaozhao', '123456', '小昭', 2, '6.jpg', 3, '2013-09-05', 1, now(), now()),
       (7, 'jixiaofu', '123456', '纪晓芙', 2, '7.jpg', 1, '2005-08-01', 1, now(), now()),
       (8, 'zhouzhiruo', '123456', '周芷若', 2, '8.jpg', 1, '2014-11-09', 1, now(), now()),
       (9, 'dingminjun', '123456', '丁敏君', 2, '9.jpg', 1, '2011-03-11', 1, now(), now()),
       (10, 'zhaomin', '123456', '赵敏', 2, '10.jpg', 1, '2013-09-05', 1, now(), now()),
       (11, 'luzhangke', '123456', '鹿杖客', 1, '11.jpg', 5, '2007-02-01', 3, now(), now()),
       (12, 'hebiweng', '123456', '鹤笔翁', 1, '12.jpg', 5, '2008-08-18', 3, now(), now()),
       (13, 'fangdongbai', '123456', '方东白', 1, '13.jpg', 5, '2012-11-01', 3, now(), now()),
       (14, 'zhangsanfeng', '123456', '张三丰', 1, '14.jpg', 2, '2002-08-01', 2, now(), now()),
       (15, 'yulianzhou', '123456', '俞莲舟', 1, '15.jpg', 2, '2011-05-01', 2, now(), now()),
       (16, 'songyuanqiao', '123456', '宋远桥', 1, '16.jpg', 2, '2007-01-01', 2, now(), now()),
       (17, 'chenyouliang', '123456', '陈友谅', 1, '17.jpg', NULL, '2015-03-21', NULL, now(), now());
```

直接进行多表查询，得到结果的总记录数，是两张表的笛卡尔积。

- 比如 dept 表中有 5 条记录；emp 表中有 17 条记录；那么执行下方的 SQL 语句，会得到 5 * 17 = 85 条数据。

```mysql
SELECT * FROM dept, emp;
```

去除掉笛卡尔积中无效的数据，可以加入一些条件，比如：

```mysql
SELECT * FROM emp, dept WHERE emp.dept_id = dept.id;
```

消除笛卡尔乘积中，无效的记录，有两种方式：

- 连接查询
  - 内连接
  - 外连接
    - 左外连接
    - 右外连接
- 子查询

## 一、连接查询

### 1.内连接

内连接，查询的是两张表，或多占表有交集的部分，即下图 A∩B 部分：

![连接查询](NoteAssets/连接查询.png)

内连接从语法上可以分为：

- 隐式内连接

- 显式内连接

隐式内连接语法：

```mysql
SELECT 字段列表 from 表1, 表2 WHERE 条件 ... ;
```

- 查询时，字段使用 `表名.字段名` 来指定。

显式内连接语法：

```mysql
SELECT 字段列表 FROM 表1 [INNER] JOIN 表2 ON 连接条件 ... ;
```

案例理解；查询员工的姓名及所属的部门名称

```mysql
SELECT emp.name, dept.name FROM emp, dept WHERE emp.dept_id = dept.id;
-- 或者 👇
SELECT emp.name, dept.name FROM emp INNER JOIN dept ON emp.dept_id = dept.id;
```

查询时，也可以为表起别名；格式如下：

- tableA  AS  别名1  ,  tableB  AS  别名2 ;

- tableA  别名1  ,  tableB  别名2 ;

```mysql
SELECT e.name '员工姓名', d.name '部门名称'
FROM emp e
         INNER JOIN dept d ON e.dept_id = d.id;
```

- 一共查询到了 16 条数据。

一旦为表起了别名，就不能再使用表名来指定对应的字段了，只能使用别名来指定字段。

### 2.外连接

外连接分为两种：左外连接，右外连接。

#### 1.左外连接

左外连接语法：

```mysql
SELECT 字段列表 FROM 表1 LEFT [OUTER] JOIN 表2 ON 连接条件 ... ;
```

左外连接，查询的是左表的全部，以及左表与右表有交集的部分。即下图的 A 和 A∩B 部分。

![连接查询](NoteAssets/连接查询.png)

案例理解；查询员工表中，所有员工的姓名，和对应的部门名称。

```mysql
SELECT e.name '员工姓名', d.name '部门姓名'
FROM emp e
         LEFt JOIN dept d ON e.dept_id = d.id;
```

- 一共查询到了 17 条记录，其中左表与右表的交集 16 条记录， 左表 1 条记录。

#### 2.右外连接

右外连接语法：

```mysql
SELECT 字段列表 FROM 表1 RIGHT [OUTER] JOIN 表2 ON 连接条件 ... ;
```

右外连接，查询到是右表的全部，以及右表与左表有交集的部分，即下图的 B 和 A∩B 部分。

![连接查询](NoteAssets/连接查询.png)

案例理解：查询部门表中，所有部门的名称, 和对应的员工名称。

```mysql
SELECT e.name '员工姓名', d.name '部门姓名'
FROM emp e
         RIGHT JOIN dept d ON e.dept_id = d.id;
```

- 一共查询到了 18 条记录，其中左表与右表的交集 16 条记录， 右表 2 条记录。

左外连接和右外连接可以相互替换，只需要调整连接查询时SQL语句中表的先后顺序就可以了。

使用左外连接，实现上方查询的结果：

```mysql
SELECT e.name '员工姓名', d.name '部门姓名'
FROM dept d
         LEFT JOIN emp e ON e.dept_id = d.id;
```

所以在实际开发中，习惯都使用左外连接。

## 二、子查询

SQL 语句中，嵌套 SQL 语句，称为嵌套查询，又称子查询。

子查询的形式有很多，比如：

```mysql
SELECT * FROM t1 WHERE column1 = (SELECT column1 FROM t2 ...);
```

子查询**外部的**的语句，可以是 SELECT、INSERT、UPDATE、DELETE 语句；最为常见的是 SELECT 语句。

子查询，有如下分类：

- 标量子查询：子查询结果为单个值，即一行一列；
- 列子查询：子查询结果为一列，但可以是多行；
- 行子查询：子查询结果为一行，但可以是多列；
- 表子查询：子查询结果为多行多列，相当于子查询结果是一张表。

### 1.标量子查询

标量子查询，返回的结果，是单个值（数字、字符串、日期……）；

这是子查询最简单的形式，

标量子查询中，常用的操作符有：`=`、`<>`、`>`、`>=`、`<`、`<=`。   



列子查询



行子查询

组合值查询，优化的写法



表子查询。

