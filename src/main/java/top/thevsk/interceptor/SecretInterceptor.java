package top.thevsk.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.LogKit;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import top.thevsk.config.MainController;
import top.thevsk.utils.HmacSHA1Utils;

public class SecretInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation invocation) {
        try {
//            String secret = PropKit.get("http.api.secret");
//            String sha1 = invocation.getController().getHeader("X-Signature");
//            if (StrKit.isBlank(sha1)) {
//                onError(invocation);
//                return;
//            }
            String body = HttpKit.readData(invocation.getController().getRequest());
//            String hmacSha1 = HmacSHA1Utils.hmacSha1(body.getBytes(), secret.getBytes());
//            if (hmacSha1 == null) {
//                onError(invocation);
//                return;
//            }
//            if (!sha1.contains(hmacSha1.toLowerCase())) {
//                onError(invocation);
//                return;
//            }
            MainController mainController = invocation.getTarget();
            mainController.body = body;
            invocation.invoke();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onError(Invocation invocation) {
        LogKit.warn("[拦截器] Secret 传入了不合法的参数");
        invocation.getController().redirect("https://www.google.com");
    }
}