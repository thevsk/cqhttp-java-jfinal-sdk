# 项目已经停止更新，如有需要请选择其他cqhttp java框架

# cqhttp-java-jfinal-sdk 

> 感谢 [@richardchien](https://github.com/richardchien) 提供 [richardchien/coolq-http-api](https://github.com/richardchien/coolq-http-api)

基于 [酷Q](https://cqp.cc/) 与 [richardchien/coolq-http-api](https://github.com/richardchien/coolq-http-api) 的 JAVA SDK， 框架使用 [jfinal/jfinal](https://github.com/jfinal/jfinal)， 依赖 maven

## 下载

### 下载项目

```base
git clone https://github.com/thevsk/cqhttp-java-jfinal-sdk.git
```

### 下载项目所需jar

使用 maven 下载，或者手动根据 pom.xml 内 jar 信息手动下载 jar

## 使用方法

在包 `src/main/java/top/thevsk/services` 内新建 Service ，例

```java
@BotService
public class HelloWorldService {
    
    @BotMessage(filter = "eq:helloWorld")
    public void share(ApiRequest request, ApiResponse response) {
        response.reply(top.thevsk.utils.CQUtils.at(request.getUserId()) + "hello world!");
    }
}
```

### 示例代码详解

`@BotService` 注解，声明这是一个 botService

`@BotMessage(filter = "eq:helloWorld")` 注解，此方法接收到的上报 post_type 为 message ，并且只有收到消息内容为 helloWorld 才会执行方法

`ApiRequest` 参数，接收到方法时，里面包含了所有的上报信息

`ApiResponse` 快捷回复，里面包含了经常调用的 API 接口，如 response.reply(String message) ， 接受到群消息则回复群消息，接收到私聊则回复私聊等便捷方法

## 注解

### @BotService

使用在类 Class 上

声明这是一个 botService

### @BotMessage

使用在方法 Method 上，在 @BotService 的类内部

加了这个注解的方法会接受到 HTTP API 插件上报 post_type 为 message 的消息 (收到消息)

注解参数：

1. MessageType 枚举 Enum 类型，默认值 DEFAULT(所有) ，再次细化接受的消息类型，可以使用的有 PRIVATE(私聊消息)、 GROUP(群消息)、 DISCUSS(讨论组消息)

1. filter 字符串 String 类型，用来过滤消息的内容，格式为 ("key:value1,value2|key:value1,value2")，目前可用的过滤条件有 eq(相等)、 like(包含)、 startWith(以...开头)、 groupId(群号)、 userId(用户QQ号)；也可以自行增加，在 `top.thevsk.interceptor.MessageFilterInterceptor` 内

### @BotRequest

使用在方法 Method 上，在 @BotService 的类内部

加了这个注解的方法会接受到 HTTP API 插件上报 post_type 为 request 的消息 (加好友请求、加群请求/邀请)

注解参数：

1. RequestType 枚举 Enum 类型，默认值 DEFAULT(所有) ，再次细化接受的请求类型，可以使用的有 FRIEND(好友通知)、 GROUP(群通知)

### @BotEvent

使用在方法 Method 上，在 @BotService 的类内部

加了这个注解的方法会接受到 HTTP API 插件上报 post_type 为 event 的消息 (群、讨论组变动等非消息类事件)

注解参数：

1. EventType 枚举 Enum 类型，默认值 DEFAULT(所有)，再次细化接受的通知类型，可以使用的有 GROUP_UPLOAD(群文件上传)、 GROUP_ADMIN(群管理员变动)、 GROUP_DECREASE(群成员减少)、 GROUP_INCREASE(群成员增加)、 FRIEND_ADD(好友添加)

## 调用 API 插件

所有的方法都在包 `top.thevsk.api` 内，共有四个类，所有的方法皆为 `static`，ApiGet(获取个人资料，群等)、ApiSend(发送消息)、ApiSet(群禁言踢人，处理加群加好友请求等)、ApiSystem(获取插件或酷Q的信息及清理缓存)

## utils

1. `top.thevsk.utils.CQUtils` 提供了所有的CQ码和几个CQ码解析方法，解析CQ码方法：getUserIdInCqAtMessage(从CQAt中解析出userId)、 getUrlInCqImage(从CQImage解析出网络url)

1. `top.thevsk.utils.SQLiteUtils` 自用的 SQLite 数据库，包含链接指定文件，增删改查，新建表等方法，若要使用 SQLite 数据库必须添加 `sqlite-jdbc` 在 pom.xml 文件中，使用方法在类的 main 方法中

## config文件

src/main/resources

```
#服务端口
server.port=7500
#Config路径
config.class.path=top.thevsk.config.HttpConfig
#扫描BotService包路径，用半角逗号分隔
bot.service.packages=top.thevsk.services
#CoolQ HTTP API配置
http.api.secret=thevsk
http.api.access_token=thevsk
http.api.url=http://127.0.0.1:5700/
```

## 启动项目

本项目用 Jetty 启动

在 `top.thevsk.start.JettyStart` 内

运行 main 方法

## 打包发布

使用 mvn package 命令打包项目

在项目下 target 文件夹内，拷贝 cqhttp-java-jfinal-sdk-{version}.jar 文件，lib 文件夹，config.properties 文件到服务器

在服务器用命令
```
java -jar cqhttp-java-jfinal-sdk-{version}.jar
```

或者后台执行(Linux)
```
nohup java -jar cqhttp-java-jfinal-sdk-{version}.jar > server.log 2>&1 &
```

实时查看日志(Linux)
```
tail -f server.log
```

服务器停止服务(Linux)

```
ps aux | grep cqhttp-java-jfinal-sdk
```
```
kill -9 {id}
```

## BUG提交及联络方式

若有BUG请加我QQ或者发邮件

QQ：[2522534416](http://wpa.qq.com/msgrd?v=3&uin=2522534416&site=qq&menu=yes)

QQ：[1916079648](http://wpa.qq.com/msgrd?v=3&uin=1916079648&site=qq&menu=yes)

邮箱：zafkielcn@gmail.com
