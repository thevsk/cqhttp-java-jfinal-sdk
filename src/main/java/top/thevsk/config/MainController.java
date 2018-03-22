package top.thevsk.config;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.log.Log;
import top.thevsk.entity.ApiRequest;
import top.thevsk.entity.ApiResponse;
import top.thevsk.enums.EventType;
import top.thevsk.enums.MessageType;
import top.thevsk.enums.RequestType;
import top.thevsk.plugins.BotServiceKit;

import java.lang.reflect.Method;
import java.util.Set;

public class MainController extends Controller {

    public String body = null;

    Log log = Log.getLog(MainController.class);

    public void index() {
        run();
        renderNull();
    }

    private void run() {
        new Thread(() -> {
            if (body == null) {
                body = HttpKit.readData(getRequest());
            }
            JSONObject j = JSONObject.parseObject(body);
            Set<Method> methods = null;
            switch (j.getString("post_type")) {
                case "message":
                    methods = BotServiceKit.getBotMessageMethods(MessageType.DEFAULT);
                    methods.addAll(BotServiceKit.getBotMessageMethods(MessageType.valueOf(j.getString("message_type").toUpperCase())));
                    break;
                case "event":
                    methods = BotServiceKit.getBotEventMethods(EventType.DEFAULT);
                    methods.addAll(BotServiceKit.getBotEventMethods(EventType.valueOf(j.getString("event_type").toUpperCase())));
                    break;
                case "request":
                    methods = BotServiceKit.getBotRequestMethods(RequestType.DEFAULT);
                    methods.addAll(BotServiceKit.getBotRequestMethods(RequestType.valueOf(j.getString("request_type").toUpperCase())));
                    break;
                default:
                    log.warn("收到了未知的消息类型");
            }
            if (methods == null) {
                log.info("没有找到能匹配信息的方法");
                return;
            }
            invoke(methods, j);
        }).start();
    }

    private void invoke(Set<Method> methods, JSONObject jsonObject) {
        ApiRequest apiRequest = new ApiRequest(jsonObject);
        ApiResponse apiResponse = getApiResponse(jsonObject);
        for (Method method : methods) {
            try {
                method.invoke(BotServiceKit.getClassInstanceByMethod(method), apiRequest, apiResponse);
            } catch (Exception e) {
                log.error("方法执行失败", e);
            }
        }
    }

    private ApiResponse getApiResponse(JSONObject jsonObject) {

        return null;
    }
}
