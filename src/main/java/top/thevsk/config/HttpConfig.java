package top.thevsk.config;

import com.jfinal.config.*;
import com.jfinal.template.Engine;
import top.thevsk.interceptor.SecretInterceptor;
import top.thevsk.plugins.loader.BotServiceLoader;

public class HttpConfig extends JFinalConfig {
    public void configConstant(Constants constants) {
        constants.setDevMode(top.thevsk.entity.Constants.getCfgBoolean("dev.mode", false));
    }

    public void configRoute(Routes routes) {
        routes.add("/", MainController.class);
    }

    public void configEngine(Engine engine) {

    }

    public void configPlugin(Plugins plugins) {

    }

    public void configInterceptor(Interceptors interceptors) {
        if (top.thevsk.entity.Constants.getCfg("http.api.secret") != null) {
            interceptors.add(new SecretInterceptor());
        }
    }

    public void configHandler(Handlers handlers) {
    }

    public void afterJFinalStart() {
        BotServiceLoader botServiceLoader = new BotServiceLoader(top.thevsk.entity.Constants.getCfg("bot.service.packages").split(","));
        botServiceLoader.load();
    }
}