# MySQL 数据库操作 DML

数据操作语言 DML（Data Manipulation Language），用来对数据库表中的记录，进行增、删、改操作。

其中包含：

- 添加数据（INSERT 语句）
- 修改数据（UPDATE 语句）
- 删除数据（DELETE 语句）

## 一、INSERT 语句

### 1.指定字段添加数据

语法如下：

```mysql
INSERT INTO 表名 (字段名1, 字段名2) VALUES (值1, 值2);
```

### 2.全部字段添加数据

语法如下：

```mysql
INSERT INTO 表名 VALUES (值1, 值2, ...);
```

### 3.指定字段批量添加数据

语法如下：

```mysql
INSERT INTO 表名 (字段名1, 字段名2) VALUES (值1, 值2), (值1, 值2), ……;
```

### 4.全部字段批量添加数据

语法如下：

```mysql
INSERT INTO 表名 VALUES (值1, 值2, ...), (值1, 值2, ...), ……;
```

INSERT 语句的三点注意事项：

1. 插入数据时，指定的字段顺序，与值的顺序是一一对应的。

2. 字符串，和日期类型的数据，应该包含在引号（`''`）中。

3. 插入的数据大小，应该在字段的规定范围内。

以上文中，创建的 user 表举例：

案例一：向 user 表的 username、name、gender 字段插入数据。

```mysql
INSERT INTO user (username, name, gender)
VALUES ('admin', '管理员', 1);
```

案例二：向 user 表的所有字段，插入数据。

```mysql
INSERT INTO user
VALUES (NULL, 'kunkun', '蔡徐坤', 26, 1, NOW(), NOW());

-- 或者 👇

INSERT INTO user
VALUES (NULL, 'cxk', '蔡徐坤', 26, 1, DEFAULT, DEFAULT);
```

- MySQL 的 `NOW()` 函数，用于获取当前系统时间。

案例三：批量向 user 表的 username、name、gender 字段，插入数据

```mysql
INSERT INTO user (username, name, gender)
VALUES ('dz', '礼堂丁真', 1),
       ('lx', '龙傲天', 1),
       ('jx', '贾乃亮', 1);
```

案例四：批量向 user 表的所有字段插入数据：

```mysql
INSERT INTO user
VALUES (NULL, 'hh', '黄子韬', 26, 1, DEFAULT, DEFAULT),
       (NULL, 'er', '尔康', 26, 1, DEFAULT, DEFAULT),
       (NULL, 'ff', '范冰冰', 26, 2, DEFAULT, DEFAULT);
```

## 二、UPDATE 语句

### 1.指定条件更新

语法如下：

```mysql
UPDATE 表名 SET 字段名1 = 值1, 字段名2 = 值2, …… WHERE 条件;
```

### 2.全量更新（很少用）

语法如下：

```mysql
UPDATE 表名 SET 字段名1 = 值1, 字段名2 = 值2, ……;
```

案例一：将 user 表中 id 为 1 的用户，姓名 name 字段更新为 `'张三'`

```mysql
UPDATE user SET name = '张三', updated_at = NOW() WHERE id = 1;
```

案例二：将 user 表的所有用户入职日期更新为'2010-01-01'

```mysql
UPDATE user SET employ_date = '2020-01-01', created_at = NOW();
```

注意事项：

1. UPDATE 语句的条件（WHERE 语句）可以有，也可以没有，如果没有条件，则会全量修改整张表的数据。

2. 在修改数据时，一般需要同时修改公共字段 `updated_at`，将其修改为当前操作时间。

## 三 DELETE 语句

### 1.指定条件删除

语法如下：

```mysql
DELETE FROM 表名 WHERE 条件;
```

### 2.全部删除（很少用）

语法如下：

```mysql
DELETE FROM 表名;
```

案例一：删除 user 表中 id 为 1 的员工：

```mysql
DELETE FROM user WHERE id = 1;
```

案例二：删除 user 表中所有员工。

```mysql
DELETE FROM user;
```

DELETE 语句注意事项：

- DELETE 语句的条件可以有，也可以没有，如果没有条件，则会删除表中所有数据。
- DELETE 语句不能删除某一个字段的值，只能使用 UPDATE 语句，将该字段值置为 NULL。
