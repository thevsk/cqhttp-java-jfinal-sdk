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

#### 注解参数

1. MessageType 枚举 Enum 类型，默认值 DEFAULT(所有) ，再次细化接受的消息类型，可以使用的有 PRIVATE(私聊消息)、 GROUP(群消息)、 DISCUSS(讨论组消息)

1. filter 字符串 String 类型，用来过滤消息的内容，格式为 ("key:value1,value2|key:value1,value2")，目前可用的过滤条件有 eq(相等)、 like(包含)、 startWith(以...开头)、 groupId(群号)、 userId(用户QQ号)；也可以自行增加，在 `top.thevsk.interceptor.MessageFilterInterceptor` 内

### @BotRequest

使用在方法 Method 上，在 @BotService 的类内部

加了这个注解的方法会接受到 HTTP API 插件上报 post_type 为 request 的消息 (加好友请求、加群请求/邀请)

#### 注解参数

1. RequestType 枚举 Enum 类型，默认值 DEFAULT(所有) ，再次细化接受的请求类型，可以使用的有 FRIEND(好友通知)、 GROUP(群通知)

### @BotEvent

使用在方法 Method 上，在 @BotService 的类内部

加了这个注解的方法会接受到 HTTP API 插件上报 post_type 为 event 的消息 (群、讨论组变动等非消息类事件)

#### 注解参数

1. EventType 枚举 Enum 类型，默认值 DEFAULT(所有)，再次细化接受的通知类型，可以使用的有 GROUP_UPLOAD(群文件上传)、 GROUP_ADMIN(群管理员变动)、 GROUP_DECREASE(群成员减少)、 GROUP_INCREASE(群成员增加)、 FRIEND_ADD(好友添加)

