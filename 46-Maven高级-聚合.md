# Maven 高级之聚合

## 一、Maven 聚合使用场景

经过上文分模块设计和继承后，现在 javaweb-practise 模块

- 该模块父工程是 javaweb-parent 模块，
- 该模块又依赖了 javaweb-pojo、javaweb-utils 模块。

将 Maven 项目 javaweb-practise 打包时，执行 Maven 生命周期 package

- 打包失败，因为在 Maven 本地仓库中，没有 javaweb-pojo、javaweb-utils 两个依赖。和 javaweb-parent 父工程的依赖。
- Maven 会在本地仓库查找这些依赖。

在 Maven 项目 javaweb-parent、javaweb-pojo、javaweb-utils 中，分别执行生命周期 install，将三个依赖安装到本地仓库。

再次打包 javaweb-practise 项目，打包成功了。

如果开发一个大型项目，拆分的模块很多，模块之间的依赖关系错综复杂，那要进行项目打包、安装操作，是非常繁琐的。

要解决这个问题，就要使用 Maven 的聚合，来实现项目的一键构建（清理、编译、测试、打包、安装等）。

## 二、Maven 聚合是什么

Maven 聚合，就是将多个模块，组织成一个整体，一键进行项目的构建。

聚合工程：一个不局有业务功能的“空”工程，有且仅有一个 pom.xml 文件；

- 一般来说，继承关系中的父工程，与聚合关系中的聚合工程是同一个。

聚合的作用：快速构建项目。无需根据依赖关系手动构建，直接在聚合工程上构建即可。

## 三、Maven 聚合案例理解

在 Maven 聚合工程的 pom.xml 文件中，通过 `<moudules>` 和 `<module>` 标签，设置当前聚合工程所包含的子模块的名称。

javaweb-parent 可以既是父工程，又是聚合工程。

demo-project/javaweb-parent/pom.xml

```xml
<!-- 聚合其它模块-->
<modules>
    <module>../javaweb-practise</module>
    <module>../javaweb-pojo</module>
    <module>../javaweb-utils</module>
</modules>
```

现在，进行编译、打包、安装操作，就无需在每一个模块上操作了。只需要在聚合工程上，统一进行操作就可以了。

在 javaweb-parent 工程的 Maven 生命周期中，执行 clean 指令。

- 发现 javaweb-practise、javaweb-pojo、javaweb-utils 模块都执行了 clean 操作。

在 javaweb-parent 工程的 Maven 生命周期中，执行 package 指令。

- 发现 javaweb-practise、javaweb-pojo、javaweb-utils 模块都都进行了打包，并在 target 目录下生成了 jar 包。

## 四、Maven 继承与聚合总结

作用：

- 聚合，用于快速构建项目。

- 继承，用于简化依赖配置、统一管理依赖。

相同点：

- 聚合工程，和用于继承的父工程的 pom.xml 文件，打包方式都为 pom。

- 聚合，与继承都属于设计型模块，并无实际的模块内容。

不同点：

- 聚合，指的是在聚合工程中配置关系，聚合工程可以感知到参与聚合的模块有哪些。

- 继承，指的是在子工程中配置关系，父工程无法感知哪些子工程继承了自己。
