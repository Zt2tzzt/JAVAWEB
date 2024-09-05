# Http 协议

浏览器会为请求，自动加上协议，比如 http 协议。

HTTP（Hyper Text Transfer Protocol）超文本传输协议，规定了浏览器（客户端）与服务器之间数据传输的规则。

- 客户端按照固定格式，发送数据给服务端，以便服务端解析；
- 服务端按照固定格式，返回数据给客户端，以便客户端解析。

![http协议](NoteAssets/http协议.png)

HTTP 协议，是互联网上应用最为广泛的一种网络协议。

启动上文中的 SpringBoot 后端服务，在浏览器中请求 `http://localhost:8080/hello`。

打开浏览器调试工具，进入 Network，查看原始的 Request Header 如下：

```txt
GET /hello HTTP/1.1
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cache-Control: max-age=0
Connection: keep-alive
Host: localhost:8080
Sec-Fetch-Dest: document
Sec-Fetch-Mode: navigate
Sec-Fetch-Site: none
Sec-Fetch-User: ?1
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36
sec-ch-ua: "Chromium";v="128", "Not;A=Brand";v="24", "Google Chrome";v="128"
sec-ch-ua-mobile: ?0
sec-ch-ua-platform: "Windows"
```

- 这就是浏览器（客户端）发送给服务器的原始数据。

查看原始的 Response Header 如下：

```txt
HTTP/1.1 200
Content-Type: text/html;charset=UTF-8
Content-Length: 10
Date: Tue, 03 Sep 2024 12:03:03 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```

- 这就是服务端返回给浏览器的原始数据。

## 一、Http 协议特点

Http 协议，有如下特点：

- **基于 TCP 协议**：面向连接，安全。
- **基于请求-响应模型:**：一次请求，对应一次响应（先请求，后响应）
- **无状态协议:**  对于数据没有记忆能力。每次请求-响应都是独立的。
  - 优点:  速度快。
  - 缺点:  多次请求间不能共享数据。解决方案：会话技术（Cookie、Session）。JWT 令牌技术；

> TCP 协议是一种面向连接的（建立连接之前要经过三次握手）、可靠的、基于字节流的传输层通信协议，
>
> TCP 协议在数据传输方面更安全

## 二、Http 协议格式

HTTP 协议又分为：请求协议，和响应协议：

- 请求协议：浏览器将数据以请求格式发送到服务器
  - 包括：**请求行**、**请求头** 、**请求体**
- 响应协议：服务器将数据以响应格式返回给浏览器
  - 包括：**响应行** 、**响应头** 、**响应体**

### 1.Http 请求协议

#### 1.Http 请求行

以上方的原始 Request Header 为例：

下方是 http 协议请求的**请求行**：

```txt
GET /hello HTTP/1.1
```

- `GET` 是**请求方式**。
- `/hello` 是**请求路径**。
- `HTTP/1.1` 是**请求协议**。

在 HTTP1.1 版本中，浏览器访问服务器的几种请求方式：

| 请求方式 | 请求说明                                                     |
| :------: | :----------------------------------------------------------- |
| **GET**  | 获取资源。向特定的资源发出请求。例：`http://www.baidu.com/s?wd=itheima` |
| **POST** | 传输实体主体。向指定资源提交数据进行处理请求（例：上传文件），数据被包含在请求体中。 |
| OPTIONS  | 返回服务器针对特定资源所支持的HTTP请求方式。因为并不是所有的服务器都支持规定的方法，为了安全有些服务器可能会禁止掉一些方法，例如：DELETE、PUT 等。那么 OPTIONS 就是用来询问服务器支持的方法。 |
|   HEAD   | 获得报文首部。HEAD 方法类似 GET 方法，但是不同的是 HEAD 方法不要求返回数据。通常用于确认 URI 的有效性及资源更新时间等。 |
|   PUT    | 修改（覆盖）操作，比如传输文件。PUT 方法可用来传输文件。类似 FTP 协议，文件内容包含在请求报文的实体中，然后请求保存到 URL 指定的服务器位置。 |
|  DELETE  | 删除操作。请求服务器删除 Request-URI 所标识的资源            |
|  TRACE   | 追踪路径。回显服务器收到的请求，主要用于测试或诊断           |
| CONNECT  | 要求用隧道协议连接代理。HTTP/1.1 协议中预留给能够将连接改为管道方式的代理服务器 |

#### 2.Http 请求头

下方是 http 协议请求的请求头：

- 它是以键值对的方式存在的，冒号左边是键，右边是值。

```txt
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7
Accept-Encoding: gzip, deflate, br, zstd
Accept-Language: zh-CN,zh;q=0.9
Cache-Control: max-age=0
Connection: keep-alive
Host: localhost:8080
Sec-Fetch-Dest: document
Sec-Fetch-Mode: navigate
Sec-Fetch-Site: none
Sec-Fetch-User: ?1
Upgrade-Insecure-Requests: 1
User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36
sec-ch-ua: "Chromium";v="128", "Not;A=Brand";v="24", "Google Chrome";v="128"
sec-ch-ua-mobile: ?0
sec-ch-ua-platform: "Windows"
```

常见的 Http 请求头有：

- `Host`：表示请求的主机名
- `User-Agent`：浏览器版本。例如：
  - Chrome 浏览器的标识类似 Mozilla/5.0 ...Chrome/79；
  - IE 浏览器的标识类似 Mozilla/5.0 (Windows NT ...)like Gecko。
- `Accept`：表示浏览器能接收的资源类型，如 `text/*`，`image/*` 或者 `*/*` 表示所有；
- `Accept-Language`：表示浏览器偏好的语言，服务器可以据此返回不同语言的网页；
- `Accept-Encoding`：表示浏览器可以支持的压缩类型，例如 gzip、deflate 等。
- `Content-Type`：请求主体的数据类型。
- `Content-Length`：数据主体的大小（单位：字节）。

#### 3.Http 请求体

请求体，用于携带请求参数。

请求体，和请求头之间，有一个空行隔开，用于标记请求头结束；如下图所示：

![Http请求体](NoteAssets/Http请求体.png)

GET 请求没有请求体，它的请求参数在请求行中携带。

GET 请求和其它（POST）请求的区别：

| 区别方式     | GET 请求                                   | POST 请求            |
| ------------ | ------------------------------------------ | -------------------- |
| 请求参数     | 请求参数在请求行中                         | 请求参数在请求体中   |
| 请求参数长度 | 请求参数长度有限制，浏览器不同限制也不同   | 请求参数长度没有限制 |
| 安全性       | 安全性低，因为请求参数暴露在浏览器地址栏中 | 安全性相对高         |

> Chrome 浏览器查看请求体路径：开发者调制工具 -> Network -> payload

### 2.Http 响应协议

#### 1.Http 响应行

以上方的原始 Response Header 为例：

```txt
HTTP/1.1 200 OK
```

- `HTTP/1.1` 是请求协议；
- `200` 是响应状态码；
- `OK` 是响应的描述信息。

常见的响应状态码有：

| 状态码分类 | 说明                                                         |
| ---------- | ------------------------------------------------------------ |
| 1xx        | **响应中**，临时状态码。表示请求已经接受，告诉客户端应该继续请求或者如果已经完成则忽略，比如 WebSocket 中返回的响应状态码。 |
| 2xx        | **成功**，表示请求已经被成功接收，处理已完成                 |
| 3xx        | **重定向**，重定向到其它地方，让客户端再发起一个请求以完成整个处理 |
| 4xx        | **客户端错误**，处理发生错误，责任在客户端，如：客户端的请求一个不存在的资源，客户端未被授权，禁止访问等 |
| 5xx        | **服务器端错误**，处理发生错误，责任在服务端，如：服务端抛出异常，路由出错，HTTP 版本不支持等 |

常见的响应状态码以及描述有：

| 状态码  | 英文描述                               | 解释                                                         |
| ------- | -------------------------------------- | ------------------------------------------------------------ |
| ==200== | **`OK`**                               | 客户端请求成功，即**处理成功**，这是我们最想看到的状态码     |
| 302     | **`Found`**                            | 指示所请求的资源已移动到由 `Location` 响应头给定的 URL，浏览器会自动重新访问到这个页面 |
| 304     | **`Not Modified`**                     | 告诉客户端，你请求的资源至上次取得后，服务端并未更改，你直接用你本地缓存吧。隐式重定向。 |
| 400     | **`Bad Request`**                      | 客户端请求有**语法错误**，不能被服务器所理解                 |
| 403     | **`Forbidden`**                        | 服务器收到请求，但是**拒绝提供服务**，比如：没有权限访问相关资源 |
| ==404== | **`Not Found`**                        | **请求资源不存在**，一般是 URL 有误，或者网站资源被删除了 |
| 405     | **`Method Not Allowed`**               | 请求方式有误，比如应该用 GET 请求方式的资源，用了 POST       |
| 428     | **`Precondition Required`**            | **服务器要求有条件的请求**，告诉客户端要想访问该资源，必须携带特定的请求头 |
| 429     | **`Too Many Requests`**                | 指示用户在给定时间内发送了**太多请求**（“限速”），配合 Retry-After(多长时间后可以请求)响应头一起使用 |
| 431     | **`Request Header Fields Too Large`** | **请求头太大**，服务器不愿意处理请求，因为它的头部字段太大。请求可以在减少请求头域的大小后重新提交。 |
| ==500== | **`Internal Server Error`**            | **服务器发生不可预期的错误**。服务器出异常了，赶紧看日志去吧 |
| 503     | **`Service Unavailable`**              | **服务器尚未准备好处理请求**，服务器刚刚启动，还未初始化好   |

更多状态码，见[状态码大全](https://cloud.tencent.com/developer/chapter/13553 )。

#### 2.Http 响应头

下方是 http 响应头：

- 它同样是以键值对的方式存在的，冒号左边是键，右边是值。

```txt
Content-Type: text/html;charset=UTF-8
Content-Length: 10
Date: Tue, 03 Sep 2024 12:03:03 GMT
Keep-Alive: timeout=60
Connection: keep-alive
```

常见的 Http 响应头有：

- `Content-Type`：表示该响应内容的类型，例如 text/html，image/jpeg、 application/json；
- `Content-Length`：表示该响应内容的长度（字节数）；
- `Content-Encoding`：表示该响应压缩算法，例如 gzip ；
- `Cache-Control`：指示客户端应如何缓存，例如 max-age=300 表示可以最多缓存 300 秒 ;
- `Set-Cookie`: 告诉浏览器为当前页面所在的域设置 cookie ;

#### 3.Http 响应体

响应体，用于携带响应参数。

响应体，和响应头之间，有一个空行隔开，如下图所示：

![Http响应体](NoteAssets/Http响应体.png)

## 三、Http 协议解析

浏览器往往内置了解析 Http 协议的工具，它会自动发送 Http 请求和解析 Http 响应。

Java 程序可以使用原生的方式，进行基于 TCP 协议协议的 Web 服务器开发。如下方代码所示：

- 主要使用到的是 `ServerSocket` 和 `Socket` API

demo-project/springboot-web-quickstart/src/main/java/com/kkcf/test/Server.java

```java
package com.kkcf.test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/*
 * 自定义web服务器
 */
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(10086); // 监听指定端口
        System.out.println("server is running...");

        while (true) {
            Socket sock = ss.accept();
            System.out.println("connected from " + sock.getRemoteSocketAddress());
            Thread t = new Handler(sock);
            t.start();
        }
    }
}

class Handler extends Thread {
    Socket sock;

    public Handler(Socket sock) {
        this.sock = sock;
    }

    public void run() {
        try (InputStream input = this.sock.getInputStream();
             OutputStream output = this.sock.getOutputStream()) {
            handle(input, output);
        } catch (Exception e) {
            try {
                this.sock.close();
            } catch (IOException ioe) {
            }
            e.printStackTrace();
            System.out.println("client disconnected.");
        }
    }

    private void handle(InputStream input, OutputStream output) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        // 读取 HTTP 请求:
        boolean requestOk = false;
        String first = reader.readLine();
        if (first.startsWith("GET / HTTP/1.")) {
            requestOk = true;
        }
        for (; ; ) {
            String header = reader.readLine();
            if (header.isEmpty()) {
                // 读取到空行时, HTTP Header 读取完毕
                break;
            }
            System.out.println(header);
        }
        System.out.println(requestOk ? "Response OK" : "Response Error");

        if (!requestOk) {
            // 发送错误响应:
            writer.write("HTTP/1.0 404 Not Found\r\n");
            writer.write("Content-Length: 0\r\n");
            writer.write("\r\n");
            writer.flush();
        } else {
            // 发送成功响应；读取 html 文件，转换为字符串
            FileInputStream fis = new FileInputStream("src/main/java/com/kkcf/test/html/a.html");
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            StringBuilder data = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                data.append(line);
            }
            br.close();
            int length = data.toString().getBytes(StandardCharsets.UTF_8).length;

            writer.write("HTTP/1.1 200 OK\r\n");
            writer.write("Connection: keep-alive\r\n");
            writer.write("Content-Type: text/html\r\n");
            writer.write("Content-Length: " + length + "\r\n");
            writer.write("\r\n"); // 空行标识 Header 和 Body 的分隔
            writer.write(data.toString());
            writer.flush();
        }
    }
}
```

Http 协议，它的请求和响应协议，都有固定的格式，

所以有很多现成的基于 Java 开发的使用 Http 协议的 Web 服务器，比如：**Tomcat**。
