package top.thevsk.interceptor;

import com.alibaba.fastjson.JSON;
import com.jfinal.log.Log;
import top.thevsk.entity.ApiRequest;

import java.lang.reflect.Method;

public class ServiceInterceptor {

    static Log log = Log.getLog(ServiceInterceptor.class);

    public static void onError(Method method, ApiRequest apiRequest, Exception exception) {
        log.error("[上报] 方法执行失败");
        log.error("[上报] 方法名 " + method.getName());
        log.error("[上报] 方法所在类 " + method.getDeclaringClass().getName());
        log.error("[上报] 接收到的上报信息 " + JSON.toJSONString(apiRequest));
        log.error("[上报] 错误 ", exception);

    }
}