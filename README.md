# IRC Base

IRCBase 是一个基于Java和SmartSocket的我的世界Mod IRC框架，项目包括客户端(client)、服务器(server)和通用(common)三个部分，支持多种消息类型和自定义包处理。

## TODO

- 实现指令系统
- 实现权限系统

## 特性

- 支持同步游戏中用户名（你可以在游戏中看到其他用户）
- 使用SmartSocket进行通信
- 支持List, Map, Set等合集序列化发送

## 构建

- clone本项目
- 导入IntelliJ IDEA，打开Maven菜单，点击IRCBase(root) -> Lifecycle -> package
- 复制`client/target/client-1.0-SNAPSHOT.jar`到你的客户端依赖
- 在你的客户端中初始化IRC

## 使用（客户端）

初始化IRCTransport（参数为你的服务器ip，端口，事件处理器）

你可以查看[IRCTest.java](client/src/test/java/IRCTest.java)，包含基本用例

一些其他示例：

### 判断是否为客户端用户

```java
ircTransport.isUser(entity.getName())
```

### 获取客户端用户名

```java
ircTransport.getName(entity.getName())
```

## 使用（服务端）

你可以在[IRCServer.java 源代码](server/src/main/java/us/cubk/irc/server/IRCServer.java)中修改端口（默认为`8888`）

构建之后查看`server/target/server-1.0-SNAPSHOT.jar`，使用命令`java -jar server-1.0-SNAPSHOT.jar`在你的服务器上启动服务端即可