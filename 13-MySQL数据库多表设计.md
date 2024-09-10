# MySQL 数据库多表设计

数据库表结构之间的关系，只有三种：

- 一对多
- 多对多
- 一对一

## 一、一对多

设计部门表，员工表

- 一个部门对应多个员工；
- 一个员工只能有一个部门。

部门表与员工表构成了“一对多”的关系。在这个关系中：

- 部门表，也成为父表；
- 员工表，也成为子表。

将这两个表建立起联系，可以创建外键约束。它让两张表的数据建立连接，保证数据的一致性和完整性。

外键约束，创建的两个场景：

场景一：创建表时指定：

```mysql
CREATE TABLE 表名(
  字段名 数据类型,
  ...
  [CONSTRAINT] [外键名称] FOREIGN KEY(外键字段名) REFERENCES 主表(主表列名)
);
```

场景二：建完表后，添加外键

```mysql
ALTER TABLE 表名 ADD CONSTRAINT 外键名称 FOREIGN KEY(外键字段名) REFERENCES 主表(主表列名);
```

创建一个部门表 dept。并插入数据：

```mysql
CREATE TABLE dept
(
    id          INT UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name        VARCHAR(10) NOT NULL UNIQUE COMMENT '部门名称',
    create_time DATETIME    NOT NULL COMMENT '创建时间',
    update_time DATETIME    NOT NULL COMMENT '修改时间'
) COMMENT '部门表';

-- 部门表测试数据
INSERT INTO dept (id, name, create_time, update_time)
VALUES (1, '学工部', now(), now()),
       (2, '教研部', now(), now()),
       (3, '咨询部', now(), now()),
       (4, '就业部', now(), now()),
       (5, '人事部', now(), now());
```

在员工表 emp 中，增加一个字段，来关联父表的主键。





使用外键约束。

创建一个部门表；

在员工表中，新增一个部门 Id 字段，关联部门 Id。







物理外键的缺点：



逻辑外键概念。