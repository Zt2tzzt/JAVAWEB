# Maven

[Maven](https://maven.apache.org) 是 apache 旗下的一个开源项目，是一款用于管理和构建 Java 项目的工具。

## 一、Maven 的作用

Maven 的作用主要有：

- 依赖管理：方便快捷的管理项目依赖的资源（jar 包），避免版本冲突的问题。
- 统一项目结构：提供标准统一的项目结构。
- 项目构建：标准跨平台（Win、Mac、Linux）的自动化项目构建方式。

### 1.Maven 依赖管理

创建一个 Maven 工程（项目）：

1. 在其中不需要手动倒入 Jar 包，只需要在 pom.xml 中描述要使用的 Jav 包信息；
2. Maven 会自动联网下载。

### 2.Maven 统一项目结构

在传统的 Java IDE eclipse、MyEclipse 和现代 Java IDE IDEA 中，创建的 Java 工程（project 项目）结构都是有差异的。

而使用 Maven 创建的工程（项目），可以实现项目结构的统一。

Maven 创建的工程，项目结构如下：

```shell
${maven-project}
|-- src
| |-- main # 实际项目资源
| | -- java # Java 源代码目录
| | -- resources # 配置文件目录
| |-- test # 测试项目资源
| | -- java # 测试源代码目录
| | -- resources # 测试配置文件目录
|-- pom.xml # 项目配置文件
|-- LICENSE.txt
|-- NOTICE.txt
|-- README.txt
```

### 3.Maven 项目构建

普通 Java 工程（项目）开发完成后，要经历变编译、测试、打包、发布的几个步骤。

在 Maven 工程中，提供了一套标准的指令，用于项目开发完成后的清理、编译、测试、打包、发布

- 比如：在 Maven 工程中，执行 compile 编译命令，会将整个工程进行编译，并把编译后的结果，放到 target 目录下。
- 比如：在 Maven 工程中，执行 package 打包命令，会将整个工程进行打包，并把打包后的 jar 包，放到 target 目录下。

## 二、Maven 的概述

Apache Maven 是一个项目管理和构建工具，它基于项目对象模型（POM）的概念，通过一小段描述信息，来管理项目的构建。

- POM（Project Object Model），表示项目对象模型。

Maven 构建项目生命周期的各个阶段，如下图所示：

- 其中通过 Maven 的各个插件，完成不同的功能。
- 各个插件的构建操作，会产生不同的文件；比如：编译阶段会有字节码文件产生，打包阶段会有 jar 包产生。

![maven生命周期](/Users/zetian/workshop/tutorial/JAVAWEB/NoteAssets/Maven生命周期.png)

## 三、Maven 仓库

Maven 仓库，用于存储资源，管理各种 jar 包。

- 本地仓库：自己计算机上的一个目录；
- [中央仓库](http://repo1.maven.org/maven2)：由 Maven 团队维护的全球唯一的远程仓库。
- 远程仓库：一般由公司团队搭建的私有仓库。

Maven 会先在本地仓库查找需要的 Jar 包进行关联，如果没有，会去远程仓库（私服）下载 Jar 包到本地。如果也没有，就会去中央仓库下载 Jar 包到远程仓库。

## 四、Maven 安装

Maven 安装主要有 4 步：

1. 解压 maven 安装包；比如：apache-maven-3.6.1-bin-zip；
2. 配置本地仓库，修改 maven 安装目录下的 conf/settings.xml 中的 `<localRepository`> 为一个指定的目录；
3.
