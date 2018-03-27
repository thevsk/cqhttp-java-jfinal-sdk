package top.thevsk.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.log.Log;
import top.thevsk.entity.ApiRequest;
import top.thevsk.entity.ApiResponse;
import top.thevsk.entity.Constants;
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
            ApiRequest apiRequest = new ApiRequest(JSONObject.parseObject(body));
            log.info("[上报] 收到了消息 " + JSON.toJSONString(apiRequest));
            Set<Method> methods = null;
            switch (apiRequest.getPostType()) {
                case Constants.POST_TYPE_MESSAGE:
                    methods = BotServiceKit.getBotMessageMethods(MessageType.DEFAULT);
                    methods.addAll(BotServiceKit.getBotMessageMethods(apiRequest.getMessageType()));
                    break;
                case Constants.POST_TYPE_EVENT:
                    methods = BotServiceKit.getBotEventMethods(EventType.DEFAULT);
                    methods.addAll(BotServiceKit.getBotEventMethods(apiRequest.getEvent()));
                    break;
                case Constants.POST_TYPE_REQUEST:
                    methods = BotServiceKit.getBotRequestMethods(RequestType.DEFAULT);
                    methods.addAll(BotServiceKit.getBotRequestMethods(apiRequest.getRequestType()));
                    break;
                default:
                    log.warn("[上报] 收到了未知的消息类型");
            }
            if (methods.size() == 0) {
                log.info("[上报] 没有找到能匹配信息的方法");
                return;
            }
            invoke(methods, apiRequest);
        }).start();
    }

    private void invoke(Set<Method> methods, ApiRequest apiRequest) {
        ApiResponse apiResponse = new ApiResponse(apiRequest);
        for (Method method : methods) {
            try {
                method.invoke(BotServiceKit.getClassInstanceByMethod(method), apiRequest, apiResponse);
            } catch (Exception e) {
                onError(method, apiRequest, e);
            }
        }
    }

    private void onError(Method method, ApiRequest apiRequest, Exception exception) {
        log.error("[上报] 方法执行失败");
        log.error("[上报] 方法名 " + method.getName());
        log.error("[上报] 方法所在类 " + method.getDeclaringClass().getName());
        log.error("[上报] 接收到的上报信息 " + JSON.toJSONString(apiRequest));
        log.error("[上报] 错误 ", exception);
    }
}