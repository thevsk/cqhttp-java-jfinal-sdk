package top.thevsk.plugins.loader;

import top.thevsk.annotation.BotService;
import top.thevsk.plugins.searcher.ClassSearcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BotServiceLoader {

    private String[] packages;

    private static final ConcurrentMap<String, Object> iocBeanMap = new ConcurrentHashMap();

    public BotServiceLoader(String[] packages) {
        this.packages = packages;
    }

    public int start() {
        System.out.println("Load bot service");

        Set<Class<?>> classes = ClassSearcher.getClasses(this.packages, BotService.class);

        for (Class<?> clazz : classes) {
            System.out.println("class name :" + clazz.getName());
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                System.out.println("method name :" + method.getName());
                Annotation[] annotations = method.getAnnotations();
                for (Annotation annotation : annotations) {
                    System.out.println("method annotation :" + annotation.annotationType().getName());
                }
            }
        }

        return 0;
    }
}
