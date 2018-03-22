package top.thevsk.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import top.thevsk.config.MainController;
import top.thevsk.entity.Constants;
import top.thevsk.utils.HmacSHA1Utils;

public class SecretInterceptor implements Interceptor {

    Log log = Log.getLog(SecretInterceptor.class);

    @Override
    public void intercept(Invocation invocation) {
        try {
            String secret = Constants.getCfg("http.api.secret");
            String sha1 = invocation.getController().getHeader("X-Signature");
            if (StrKit.isBlank(sha1)) {
                //TODO: 传入了不合法的参数
                log.warn("传入了不合法的参数");
                invocation.getController().redirect("https://www.google.com");
                return;
            }
            String body = HttpKit.readData(invocation.getController().getRequest());
            String hmacSha1 = HmacSHA1Utils.hmacSha1(body.getBytes(), secret.getBytes());
            if (!sha1.contains(hmacSha1.toLowerCase())) {
                //TODO: 传入了不合法的参数
                log.warn("传入了不合法的参数");
                invocation.getController().redirect("https://www.google.com");
                return;
            }
            MainController mainController = invocation.getTarget();
            mainController.body = body;
            invocation.invoke();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}