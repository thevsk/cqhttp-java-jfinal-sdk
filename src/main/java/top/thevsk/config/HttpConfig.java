package top.thevsk.config;

import com.jfinal.config.*;
import com.jfinal.kit.PropKit;
import com.jfinal.template.Engine;
import top.thevsk.plugins.loader.BotServiceLoader;

public class HttpConfig extends JFinalConfig {
    public void configConstant(Constants constants) {
        PropKit.use("config.properties");
        constants.setDevMode(PropKit.getBoolean("dev.mode", false));
    }

    public void configRoute(Routes routes) {
        routes.add("/", MainController.class);
    }

    public void configEngine(Engine engine) {

    }

    public void configPlugin(Plugins plugins) {

    }

    public void configInterceptor(Interceptors interceptors) {

    }

    public void configHandler(Handlers handlers) {
    }

    public void afterJFinalStart() {
        BotServiceLoader botServiceLoader = new BotServiceLoader(PropKit.get("bot.service.packages").split(","));
        botServiceLoader.load();
    }
}
