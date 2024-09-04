# Tomcat 服务器

服务器分为：

- 硬件服务器，也称伺服器。是提供计算服务的设备。
- 软件服务器，运行的应用程序，能够接收客户端请求，并根据请求给客户端响应数据

Web 服务器是一个应用程序（软件），它对 HTTP 协议的请求、响应操作进行封装，使得开发者不用自行解析 http 协议规则，让 Web 开发更加便捷。

Web 服务器主要功能是"提供网上信息浏览服务"。

## 一、Tomcat 服务器介绍

[Tomcat](https://tomcat.apache.org) 服务器软件是 Apache 软件基金会的一个核心项目。它是一个免费的开源的 web 应用服务器。

Tomcat 服务器仅支持 Servlet、JSP 等少量 JavaEE 规范，所以是一个轻量级 Web 服务器。

> JavaEE（Java Enterprise Edition）规范是指 Java 企业级开发的技术规范总和；
>
> 其中包含 13 项技术规范：JDBC、JNDI、EJB、RMI、JSP、Servlet、XML、JMS、Java IDL、JTS、JTA、JavaMail、JAF

Tomcat 服务器，也被称为 Web 容器、Servlet 容器；JavaWeb 程序需要依赖 Tomcat 服务器才能运行。

## 二、Tomcat 服务器基本使用

### 1.Tomcat 下载

下载：从[官方网站](https://tomcat.apache.org)下载

### 2.Tomcat 安装

安装：解压压缩包；

- 最好解压到一个不包含中文和空格的目录，因为后期在部署项目的时候，如果路径有中文或者空格可能会导致程序部署失败

目录结构如下：

```shell
├─📁 bin/--------------- # Tomcat 可执行文件
├─📁 conf/-------------- # Tomcat 配置文件
├─📁 lib/--------------- # Tomcat 依赖的 jar 包
├─📁 logs/-------------- # Tomcat 日志文件
├─📁 temp/-------------- # Tomcat 临时文件
├─📁 webapps/----------- # Tomcat 应用发布目录
├─📁 work/-------------- # Tomcat 工作目录
├─📄 BUILDING.txt
├─📄 CONTRIBUTING.md
├─📄 LICENSE
├─📄 NOTICE
├─📄 README.md
├─📄 RELEASE-NOTES
└─📄 RUNNING.txt
```

### 3.Tomcat 卸载

卸载：删除解压的目录；

### 4.Tomcat 启动

Tomcat 启动：执行 `bin/startup.bat`

- Tomcat 服务器启动后，窗口不会关闭，表示 Tomcat 服务器正在运行。

Tomcat 启动的过程中，遇到控制台有中文乱码时，可以通常修改 `conf/logging.prooperties` ；配置文件中的如下部分解决：

```properties
java.util.logging.ConsoleHandler.encoding = GBK
```

> Tomcat 启动时，常见的问题：
>
> 问题一：Tomcat启动时，窗口一闪而过；
>
> - 解决方法：检查 `JAVA_HOME` 环境变量是否正确配置，Tomcat 启动时，要依赖 Java 环菌。
>
> 问题二：端口号冲突。体现在如下报错中：
>
> ```sh
> Caused by: java.net.BindException: Address already in use: bind
>         at java.base/sun.nio.ch.Net.bind0(Native Method)
>         at java.base/sun.nio.ch.Net.bind(Net.java:555)
> ```
>
> - 解决方案：换 Tomcat 端口号，修改 `conf/server.xml` 文件：
>
> ```xml
> <Connector port="8080" protocol="HTTP/1.1"
>            connectionTimeout="20000"
>            redirectPort="8443"
>            maxParameterCount="1000"/>
> ```

HTTP 协议，默认端口号为 80，如果将 Tomcat 端口号改为 80，那么访问 Tomcat 时，将不用输入端口号。

### 5.Tomcat 关闭

Tomcat 服务器关闭，有三种方式：

- 方式一：强制关闭：直接关闭 Tomcat 启动窗口（不建议）
- 方式二：正常关闭：执行 `bin\shutdown.bat`
- 方式三：正常关闭：在 Tomcat 启动窗口中按下 Ctrl+C

### 6.Tomcat 静态资源部署

将静态资源目录，放到 `webapps` 目录下，即可完成部署。

- 比如现有 haha 目录，下面有 haha.html 文件，将 haha 目录放到 webapps 目录下，就可以通过 `localhost:8080/haha/haha.html` 访问到静态资源。
- 不能放单个文件，要放目录。
