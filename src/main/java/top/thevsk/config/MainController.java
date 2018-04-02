package top.thevsk.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.log.Log;
import top.thevsk.annotation.BotMessage;
import top.thevsk.entity.ApiRequest;
import top.thevsk.entity.ApiResponse;
import top.thevsk.entity.Constants;
import top.thevsk.interceptor.MessageFilterInterceptor;
import top.thevsk.interceptor.ServiceInterceptor;
import top.thevsk.plugins.BotServiceKit;
import top.thevsk.utils.NullUtils;

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
                    methods = BotServiceKit.getBotMessageMethods(apiRequest.getMessageType());
                    break;
                case Constants.POST_TYPE_EVENT:
                    methods = BotServiceKit.getBotEventMethods(apiRequest.getEvent());
                    break;
                case Constants.POST_TYPE_REQUEST:
                    methods = BotServiceKit.getBotRequestMethods(apiRequest.getRequestType());
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
        if (Constants.POST_TYPE_MESSAGE.equals(apiRequest.getPostType())) {
            invokeMessage(methods, apiRequest, apiResponse);
            return;
        }
        for (Method method : methods) {
            try {
                method.invoke(BotServiceKit.getClassInstanceByMethod(method), apiRequest, apiResponse);
            } catch (Exception e) {
                ServiceInterceptor.onError(method, apiRequest, e);
            }
        }
    }

    private void invokeMessage(Set<Method> methods, ApiRequest apiRequest, ApiResponse apiResponse) {
        for (Method method : methods) {
            BotMessage botMessage = method.getAnnotation(BotMessage.class);
            String filter = botMessage.filter();
            boolean flag = true;
            if (NullUtils.isNotNullOrBlank(filter, apiRequest.getMessage())) {
                flag = MessageFilterInterceptor.getInstance().filter(filter, apiRequest);
            }
            if (flag) {
                try {
                    method.invoke(BotServiceKit.getClassInstanceByMethod(method), apiRequest, apiResponse);
                } catch (Exception e) {
                    ServiceInterceptor.onError(method, apiRequest, e);
                }
            }
        }
    }
}