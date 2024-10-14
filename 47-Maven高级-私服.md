# Maven 高级之私服

前面介绍分模块设计时，提到拆分的模块，是可以在同一个公司各个项目组之间进行共享的。

这个模块的资源共享，就需要通过 Maven 私服来实现。

## 一、Maven 私服是什么

Maven 私服，就是架设在局域网内的一台服务器，是一种特殊的远程仓库。用来代理位于外部的中央仓库，解决团队内部的资源共享与资源同步问题。

有了私服后：

- A 团队可以把 jar 包，直接上传到私服上。
- B 团队连接私服。根据依赖的坐标信息，将对应的 jar 包下载到本地仓库；

![Maven私服](NoteAssets/Maven私服.png)

## 二、Maven 依赖查询顺序

项目中需要使用第三方依赖，本地仓库没有，会自动连接私服下载，私服没有，会自动连接中央仓库下载；

然后将下载的依赖，存储在私服仓库，及本地仓库中。

## 三、Maven 私服中的仓库

Maven 私服提供三种仓库：

- RELEASE：存储自己开发的 RELEASE（发行版本）的资源。
- SNAPSHOT：存储自己开发的 SNAPSHOT（快照版本）的资源。
- Central：存储从中央仓库下载下来的依赖。

> 依赖（jar 包）版本分为：
>
> - RELEASE（发行）版本：功能趋于稳定、当前更新停止，可以用于发行的版本，存储在私服中的 RELEASE 仓库中。
> - SNAPSHOT（快照）版本：功能不稳定、尚处于开发中的版本，即快照版本，存储在私服的 SNAPSHOT 仓库中。

Maven 模块默认是 snapshot 版本。只要不是 snapshot 版本，都会上传到 release 仓库。

## 四、Maven 私服上传和下载

资源上传与下载，我们需要做三步配置，执行一条指令。

1. 在 Maven 配置文件中，配置访问私服的用户名、密码。
2. 在 Maven 配置文件中，配置连接私服的 url 地址。
3. 在项目的 pom.xml 文件中，配置上传资源的 url 地址。

配置好了上述三步后，要上传资源到私服仓库，就执行 Maven 生命周期 `deploy`。

![Maven私服的上传和下载](NoteAssets/Maven私服的上传和下载.png)

为模拟企业开发，假设在服务器（192.168.150.101）上搭建了 Maven 私服，并在 8081 端口上开发服务；

- 可以访问私服测试：`http://192.168.150.101:8081`

### 1.Maven 配置文件中，配置访问私服的用户名、密码

找到本地 Maven 安装目录下 `conf/settings.xml` 配置文件。在其中进行配置：

分别配置 RELEASES 和 SNAPSHOT 两个仓库的访问用户名和密码。

```xml
<servers>
  <server>
      <id>maven-releases</id>
      <username>admin</username>
      <password>admin</password>
  </server>

  <server>
      <id>maven-snapshots</id>
      <username>admin</username>
      <password>admin</password>
  </server>
</servers>
```

### 2.Maven 配置文件中，配置连接私服的 url 地址

找到本地 Maven 安装目录下 `conf/settings.xml` 配置文件。在其中进行配置：

```xml
<mirrors>
  <mirror>
    <id>maven-public</id>
    <mirrorOf>*</mirrorOf>
    <url>http://192.168.150.101:8081/repository/maven-public/</url>
  </mirror>
</mirrors>

……

<profiles>
  <profile>
      <id>allow-snapshots</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <repositories>
          <repository>
              <id>maven-public</id>
              <url>http://192.168.150.101:8081/repository/maven-public/</url>
              <releases>
                <enabled>true</enabled>
              </releases>
              <snapshots>
                <enabled>true</enabled>
              </snapshots>
          </repository>
      </repositories>
  </profile>
</profiles>
```

- 默认 SHAPSHOT 版本依赖不会下载，要配置 `<enable>` 标签。

### 3.项目的 pom.xml 文件中，配置上传资源的 url 地址

在 Maven 工程的 pom.xml 配置文件中，配置工程打包后的 jar 包，要往私服的哪个仓库中上传；配置两个。

demo-project/javaweb-parent/pom.xml

```xml
<distributionManagement>
    <!-- RELEASE 版本的发布地址 -->
    <repository>
        <id>maven-releases</id>
        <url>http://192.168.150.101:8081/repository/maven-releases/</url>
    </repository>

    <!-- SNAPSHOT 版本的发布地址 -->
    <snapshotRepository>
        <id>maven-snapshots</id>
        <url>http://192.168.150.101:8081/repository/maven-snapshots/</url>
    </snapshotRepository>
</distributionManagement>
```

配置完成之后，就可以在 javaweb-parent 中执行 Maven 的 `deploy` 生命周期，将项目打包后生成的 jar 包发布到私服仓库中。

- 如果项目是 SNAPSHOT 版本，jar 包会上传到 SNAPSHOT 仓库中；
- 否则上传到 RELEASE 仓库中。

其他项目组的，就可以直接通过依赖的坐标引入对应的依赖，本地仓库没有，就会自动从私服仓库中下载。
