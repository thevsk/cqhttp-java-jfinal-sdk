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
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BotServiceLoader {

    private final Log log = Log.getLog(BotServiceLoader.class);

    private String[] packages;

    public BotServiceLoader(String[] packages) {
        this.packages = packages;
    }

    public void load() {
        ConcurrentMap<String, Object> iocBeanMap = new ConcurrentHashMap<>();
        Set<Method> botMessageMethods = new HashSet<>();
        Set<Method> botRequestMethods = new HashSet<>();
        Set<Method> botEventMethods = new HashSet<>();
        log.info("[预加载] botService");
        Set<Class<?>> classes = ClassSearcher.getClasses(this.packages, BotService.class);
        for (Class<?> clazz : classes) {
            iocBeanMap.put(clazz.getName(), Enhancer.enhance(clazz));
            MethodSearcher.addMethod(clazz, BotMessage.class, botMessageMethods);
            MethodSearcher.addMethod(clazz, BotRequest.class, botRequestMethods);
            MethodSearcher.addMethod(clazz, BotEvent.class, botEventMethods);
            log.info("[预加载] " + clazz.getName());
        }
        BotServiceKit.init(botMessageMethods, botRequestMethods, botEventMethods, iocBeanMap);
        log.info("[预加载] botService 完成");
    }
}