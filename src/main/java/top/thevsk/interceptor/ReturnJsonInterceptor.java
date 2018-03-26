package top.thevsk.interceptor;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.log.Log;
import top.thevsk.entity.ReturnJson;

public class ReturnJsonInterceptor implements Interceptor {

    Log log = Log.getLog(ReturnJsonInterceptor.class);

    @Override
    public void intercept(Invocation invocation) {
        try {
            invocation.invoke();
            ReturnJson returnObject = invocation.getReturnValue();
            if (returnObject.getRetcode() != 0) {
                log.error("插件返回错误, code:" + returnObject.getRetcode());
                onError(invocation);
            }
        } catch (Exception e) {
            log.error("插件错误", e);
            onError(invocation);
        }
    }

    private void onError(Invocation invocation) {
        log.error("发生错误的url:" + invocation.getArg(0));
        log.error("发送错误的参数:" + JSON.toJSONString(invocation.getArg(1)));
    }
}