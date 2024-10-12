# Maven 高级之私服

私服是什么？



有了私服后，依赖的查找顺序。



私服 jar 包的上传，下载。

上传：使用 Maven 生命周期 deploy 指令。

下载：在 Maven 配置文件中，配置私服地址。



私服提供的仓库：

- central 中央仓库；
- release 仓库；
- snapshot 仓库。



Maven 模块默认是 snapshot 版本。只要不是 snapshot 版本，都会上传到 release 仓库。



1.在 Maven 安装目录的配置文件中，设置私服的访问用户名和密码；配置两个

2.在 Maven 工程的配置文件中，配置工程的 jar 包，要往私服的哪个仓库中上传；配置两个。

3.设置私服依赖下载的仓库组地址 setting.xml 中的 mirrors，profiles 中配置。

- 默认 snapshot 版本不会下载，要配置 `<enable>` 标签。