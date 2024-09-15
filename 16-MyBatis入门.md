# MyBatis 入门

MyBatis 是什么

持久层框架



JDBC 是 SUM 公司提供的 Java 程序操作数据库的规范。



MyBatis 历史



MyBatis 入门程序。

MaBatis 操作数据库，三个步骤：

1. 准备工作：创建 Spring Boot 项目；准备数据库，创建用户表 user；创建用户类 User
   - 创建 Spring Boot 项目时，勾选 MyBatis Framework 和 MySQL Driver；
2. 引入 MyBatis 的相关依赖，配置 MyBatis；
   - 配置数据库连接信息。
   - Spring Boot 整合的 MyBatis 矿建，配置信息在 application.properties 配置文件中。
3. 编写 SQL 语句（注解，或 XML）
   - 使用 @Mapper 注解；
   - 一般只需要定义接口，不需要定义实现类；Doa 接口名一般为 xxxMapper
   - 在 @Select 注解中，写 SQL 语句。
   - 查询语句会将查询结果，自动封装到接口返回值中