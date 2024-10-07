# Spring Boot 属性配置优先级

## 一、Spring Boot 三种配置文件

前面已经介绍过，Spring Boot 项目，支持的三种配置文件格式，优先级排名是（从高到低）：

1. application.properties
2. application.yml
3. application.yaml

在项目开发中，推荐只使用一个配置文件格式,，最为主流的就是 yml 格式。

## 二、Spring Boot 二种外部属性配置

为了增强程序的扩展性，在 Spring Boot 项目中，除了支持配置文件的配置方式外，还支持另外两种常见的配置方式：

### 1.Java 系统属性

Spring Boot 还支持 Java 系统属性的配置方式，以配置 Tomcat 服务器端口号为例，格式如下：

```java
-Dserver.port=9000
```

### 2.命令行参数

Spring Boot 还支持命令行参数的配置方式，以配置 Tomcat 服务器端口号为例，格式如下：

```sh
--server.port=10010
```

## 三、IDEA 设置 Java 系统属性、命令行参数

在 IDEA 中，运行程序，可配置项目的 Java 系统属性，命令行参数。

左上方点击要运行的方法 -> Edit Configurations -> Modify Options -> Add VM options 、Program arguments

![idea设置Java系统属性和命令行参数](NoteAssets/idea设置Java系统属性和命令行参数.jpg)

命令行参数，优先级高于 Java 系统属性；

Java 系统属性，优先级高于三种配置文件。

## 四、Maven 项目打包

Spring Boot 项目进行打包时，需要引入 `spring-boot-maven-plugin` 插件（基于官方骨架创建的项目，会自动添加该插件）

demo-project/javaweb-practise/pom.xml

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <excludes>
                    <exclude>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                    </exclude>
                </excludes>
            </configuration>
        </plugin>
    </plugins>
</build>
```

IDEA 打包 Maven 项目：

- 在 IDEA Maven 面板中，找到当前项目：Lifecycle -> 双击 package

基于 Maven 的 Java 项目打包：执行 Maven 的打包指令 package。将项目打包成 jar 包；

## 五、命令行设置 Java 系统属性、命令行参数

项目打包上线后，要通过命令，设置 Java 系统属性、命令行参数

- 以配置 Tomcat 服务器端口号为例，执行 `java -jar` 命令，运行 jar 包

```sh
java -Dserver.port=9000 -jar jar包名称 --server.port=10010
```

## 六、Spring Boot 属性配置优先级总结

在 Spring Boot 项目中，常见的属性配置方式有五种，

- 三种配置文件；
- 二种外部属性的配置（Java 系统属性、命令行参数)。

它们的优先级从高到低)：

1. 命令行参数（--xxx=xxx）
2. java 系统属性（-Dxxx=xxx）
3. application.properties、
4. application.yml
5. application.yaml
