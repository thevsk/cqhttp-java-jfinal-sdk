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
public class TestService {
    
    @BotMessage(filter = "eq:helloWorld")
    public void share(ApiRequest request, ApiResponse response) {
        response.reply(top.thevsk.utils.CQUtils.at(request.getUserId()) + "hello world!");
    }
}
```

### 示例代码详解

`@BotService` 注解，声明这是一个botService

`@BotMessage(filter = "eq:helloWorld")` 注解，此方法接收到的上报 post_type 为 message ，并且只有收到消息内容为 helloWorld 才会执行方法

`ApiRequest` 参数，接收到方法时，里面包含了所有的上报信息

`ApiResponse` 快捷回复，里面包含了经常调用的 API 接口，如 response.reply(String message) ， 接受到群消息则回复群消息，接收到私聊则回复私聊等便捷方法
