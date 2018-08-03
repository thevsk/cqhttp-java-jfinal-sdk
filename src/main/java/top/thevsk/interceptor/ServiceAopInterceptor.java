package top.thevsk.interceptor;

import com.alibaba.fastjson.JSON;
import com.jfinal.kit.LogKit;
import top.thevsk.entity.ApiRequest;
import top.thevsk.interceptor.interfaces.BotServiceInterceptor;

import java.lang.reflect.Method;

public class ServiceAopInterceptor {

    public static void onError(BotServiceInterceptor botServiceInterceptor, Method method, ApiRequest apiRequest, Exception exception) {
        LogKit.error("[AOP] AOP执行失败");
        LogKit.error("[AOP] AOP名 " + botServiceInterceptor.getClass().getSimpleName());
        LogKit.error("[AOP] AOP所在方法名 " + method.getName());
        LogKit.error("[AOP] AOP所在方法所在类 " + method.getDeclaringClass().getName());
        LogKit.error("[AOP] 接收到的上报信息 " + JSON.toJSONString(apiRequest));
        LogKit.error("[AOP] 错误 ", exception);
    }
}