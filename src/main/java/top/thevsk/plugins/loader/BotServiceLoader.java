package top.thevsk.plugins.loader;

import com.jfinal.aop.Enhancer;
import com.jfinal.log.Log;
import top.thevsk.annotation.BotEvent;
import top.thevsk.annotation.BotMessage;
import top.thevsk.annotation.BotRequest;
import top.thevsk.annotation.BotService;
import top.thevsk.plugins.BotServiceKit;
import top.thevsk.plugins.searcher.ClassSearcher;
import top.thevsk.plugins.searcher.MethodSearcher;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BotServiceLoader {

    private final Log log = Log.getLog(BotServiceLoader.class);

    private String[] packages;

    private static final ConcurrentMap<String, Object> iocBeanMap = new ConcurrentHashMap<>();

    public BotServiceLoader(String[] packages) {
        this.packages = packages;
    }

    public void load() {
        List<Method> botMessageMethods = new ArrayList<>();
        List<Method> botRequestMethods = new ArrayList<>();
        List<Method> botEventMethods = new ArrayList<>();
        log.info("Load bot service");
        Set<Class<?>> classes = ClassSearcher.getClasses(this.packages, BotService.class);
        for (Class<?> clazz : classes) {
            log.info("load class " + clazz.getName());
            iocBeanMap.putIfAbsent(clazz.getName(), Enhancer.enhance(clazz));
            MethodSearcher.addMethod(clazz, BotMessage.class, botMessageMethods);
            MethodSearcher.addMethod(clazz, BotRequest.class, botRequestMethods);
            MethodSearcher.addMethod(clazz, BotEvent.class, botEventMethods);
            log.info("class " + clazz.getName() + " load success");
        }
        BotServiceKit.init(botMessageMethods, botRequestMethods, botEventMethods);
    }
}