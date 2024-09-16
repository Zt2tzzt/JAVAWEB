# JDBC 介绍

JDBC 是什么

JDBC 是一套规范（接口），没有提供具体的实现方式。

由各个数据库厂商，提供具体的实现。也称为 Java 程序的数据库驱动。

- MySQL 的 Java 驱动 mysql-connector-j，



JDBC 的实际应用，仅了解：



JDBC 与 MyBatis 进行对比。



Spring Boot 底层会采用数据库连接池奇数，统一管理分配这些连接。

- 每次执行 SQL 时，只需要从连接池中获取连接，然后执行 SQL，执行完成后，再将连接归还给连接池。
- 这样就形成了连接的复用，避免频繁的创建、释放连接造成的资源的浪费。



在 MyBatis 中，只需要关注两个方面

- application.properties
- Mapper接口（编写SQL语句）