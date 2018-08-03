package top.thevsk.interceptor;

import com.alibaba.fastjson.JSON;
import com.jfinal.kit.LogKit;
import top.thevsk.entity.ApiRequest;

import java.lang.reflect.Method;

public class ServiceInterceptor {

    public static void onError(Method method, ApiRequest apiRequest, Exception exception) {
        LogKit.error("[上报] 方法执行失败");
        LogKit.error("[上报] 方法名 " + method.getName());
        LogKit.error("[上报] 方法所在类 " + method.getDeclaringClass().getName());
        LogKit.error("[上报] 接收到的上报信息 " + JSON.toJSONString(apiRequest));
        LogKit.error("[上报] 错误 ", exception);
    }
}