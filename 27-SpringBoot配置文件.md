Spring Boot 配置文件

Spring Boot 项目的配置文件是 application.properties。

Spring Boot 提供了 @Value 注解，将配置文件中属性的值，映射到类中的属性。



Spring Boot 提供多种配置方式

- application.properties
- application.yml
- application.yaml，与上方的 yml 扩展名意思一致。



XML、properties、yml 格式的配置文件，进行对比。

Spring Boot 中，仅支持 properties、yml 格式的配置，现代企业开发中，基本都用 yml 格式进行配置。



yml 格式的基本语法

- IDEA 会自动将缩进中的 Tab，转为空格。

```yaml
server:
  port: 9000
```

 yml 数据格式

```yaml
# 定义对象 / Map 集合
users:
  name: "zhangsan"
  age: 18
  address: "北京"

# 定义数组
hobby:
  - sing
  - dance
  - rap
  - basketball
```

使用 application.yml 替换掉 application.properties 配置文件

demo-project/javaweb-practise/src/main/resources/application.properties

```properties
spring.application.name=javaweb-practise

# ?????
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
# ????? url
spring.datasource.url=jdbc:mysql://localhost:3306/javawebdb
# ????????
spring.datasource.username=root
# ???????
spring.datasource.password=wee1219

# ?? MyBatis ??
mybatis.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
# ?? MyBatis ??????
mybatis.configuration.map-underscore-to-camel-case=true

# 单个文件上传大小，最大为 10M
spring.servlet.multipart.max-file-size=10MB
# 单个请求中上传的最大值为 100MB（一个请求中可能包含多个文件）
spring.servlet.multipart.max-request-size=100MB

# 阿里云你 OSS 服务配置
aliyun.oos.endpoint=https://oss-cn-shenzhen.aliyuncs.com
aliyun.oos.accessKeyId=xxxxxxx
aliyun.oos.accessKeySecret=xxxxxx
aliyun.oos.bucketName=xxxxxx
```

demo-project/javaweb-practise/src/main/resources/application.yml

```yaml
spring:
  # 项目名称
  application:
    name: javaweb-practise

  # 数据库配置
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/javawebdb
    username: root
    password: wee1219

  # 文件上传配置
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB

# MyBatis 配置
mybatis:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    map-underscore-to-camel-case: true

aliyun:
  oos:
    endpoint: https://oss-cn-shenzhen.aliyuncs.com
    accessKeyId: xxxxxx
    accessKeySecret: xxxxxx
    bucketName: zetian-bucket
```

@ConfigurationProperties
