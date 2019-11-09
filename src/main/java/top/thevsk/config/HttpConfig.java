package top.thevsk.config;

import com.jfinal.config.*;
import com.jfinal.kit.LogKit;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.template.Engine;
import top.thevsk.interceptor.SecretInterceptor;
import top.thevsk.plugins.loader.BotServiceLoader;

public class HttpConfig extends JFinalConfig {

    public void configConstant(Constants constants) {
        PathKit.setWebRootPath("/asd");
    }

    public void configRoute(Routes routes) {
        routes.add("/", MainController.class);
    }

    public void configEngine(Engine engine) {
    }

    public void configPlugin(Plugins plugins) {

    }

    public void configInterceptor(Interceptors interceptors) {
//        if (PropKit.get("http.api.secret") != null) {
            interceptors.add(new SecretInterceptor());
            LogKit.info("[拦截器] Secret 加载成功");
//        }
    }

    public void configHandler(Handlers handlers) {
    }

    public void afterJFinalStart() {
        BotServiceLoader botServiceLoader = new BotServiceLoader(PropKit.get("bot.service.packages").split(","));
        botServiceLoader.load();
        LogKit.info("[服务] 启动完成");
        LogKit.info("[服务] 端口 " + PropKit.getInt("server.port"));
    }
}