package top.thevsk.plugins;

import com.jfinal.aop.Enhancer;
import com.jfinal.kit.LogKit;
import top.thevsk.annotation.BotEvent;
import top.thevsk.annotation.BotMessage;
import top.thevsk.annotation.BotRequest;
import top.thevsk.annotation.BotServiceAop;
import top.thevsk.enums.EventType;
import top.thevsk.enums.MessageType;
import top.thevsk.enums.RequestType;
import top.thevsk.interceptor.interfaces.BotServiceInterceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BotServiceKit {
    private static Set<Method> botMessageMethods;
    private static Set<Method> botRequestMethods;
    private static Set<Method> botEventMethods;
    private static Map<String, Set<Method>> botMessageMethodsMap = new HashMap<>();
    private static Map<String, Set<Method>> botRequestMethodsMap = new HashMap<>();
    private static Map<String, Set<Method>> botEventMethodsMap = new HashMap<>();
    private static ConcurrentMap<String, Object> iocBeanMap;
    private static ConcurrentMap<String, Object> aopIocBeanMap;

    public static void init(Set<Method> botMessageMethods,
                            Set<Method> botRequestMethods,
                            Set<Method> botEventMethods,
                            ConcurrentMap<String, Object> iocBeanMap,
                            Set<Class<?>> servicesClass) {
        BotServiceKit.botMessageMethods = botMessageMethods;
        BotServiceKit.botRequestMethods = botRequestMethods;
        BotServiceKit.botEventMethods = botEventMethods;
        BotServiceKit.iocBeanMap = iocBeanMap;
        initAop(servicesClass);
        eachBotMessage();
        eachBotRequest();
        eachBotEvent();
    }

    private static void initAop(Set<Class<?>> servicesClass) {
        aopIocBeanMap = new ConcurrentHashMap<>();
        Set<Class<? extends BotServiceInterceptor>> aopInterceptors = new HashSet<>();
        for (Class<?> clazz : servicesClass) {
            aopInterceptors.addAll(getBotAopInter(clazz.getAnnotations()));
        }
        initAop(aopInterceptors, botMessageMethods);
        initAop(aopInterceptors, botRequestMethods);
        initAop(aopInterceptors, botEventMethods);
        LogKit.info("[预加载] AOP");
        for (Class<? extends BotServiceInterceptor> clazz : aopInterceptors) {
            LogKit.info("[预加载] AOP " + clazz.getName());
            aopIocBeanMap.put(clazz.getName(), Enhancer.enhance(clazz));
        }
        LogKit.info("[预加载] AOP加载完成");
    }

    private static void initAop(Set<Class<? extends BotServiceInterceptor>> aopInterceptors, Set<Method> methods) {
        for (Method method : methods) {
            aopInterceptors.addAll(getBotAopInter(method.getAnnotations()));
        }
    }

    private static void eachBotMessage() {
        for (MessageType messageType : MessageType.class.getEnumConstants()) {
            if (MessageType.DEFAULT.equals(messageType)) continue;
            Set<Method> set = new HashSet<>();
            for (Method method : botMessageMethods) {
                BotMessage botMessage = method.getAnnotation(BotMessage.class);
                if (messageType.equals(botMessage.messageType())) {
                    set.add(method);
                }
                if (MessageType.DEFAULT.equals(botMessage.messageType())) {
                    set.add(method);
                }
            }
            botMessageMethodsMap.put("MessageType:" + messageType.getCode(), set);
        }
    }

    private static void eachBotRequest() {
        for (RequestType requestType : RequestType.class.getEnumConstants()) {
            if (RequestType.DEFAULT.equals(requestType)) continue;
            Set<Method> set = new HashSet<>();
            for (Method method : botRequestMethods) {
                BotRequest botRequest = method.getAnnotation(BotRequest.class);
                if (requestType.equals(botRequest.requestType())) {
                    set.add(method);
                }
                if (RequestType.DEFAULT.equals(botRequest.requestType())) {
                    set.add(method);
                }
            }
            botRequestMethodsMap.put("RequestType:" + requestType.getCode(), set);
        }
    }

    private static void eachBotEvent() {
        for (EventType eventType : EventType.class.getEnumConstants()) {
            if (EventType.DEFAULT.equals(eventType)) continue;
            Set<Method> set = new HashSet<>();
            for (Method method : botEventMethods) {
                BotEvent botEvent = method.getAnnotation(BotEvent.class);
                if (eventType.equals(botEvent.eventType())) {
                    set.add(method);
                }
                if (EventType.DEFAULT.equals(botEvent.eventType())) {
                    set.add(method);
                }
            }
            botEventMethodsMap.put("EventType:" + eventType.getCode(), set);
        }
    }

    public static Set<Method> getBotMessageMethods(MessageType messageType) {
        return botMessageMethodsMap.get("MessageType:" + messageType.getCode());
    }

    public static Set<Method> getBotRequestMethods(RequestType requestType) {
        return botRequestMethodsMap.get("RequestType:" + requestType.getCode());
    }

    public static Set<Method> getBotEventMethods(EventType eventType) {
        return botEventMethodsMap.get("EventType:" + eventType.getCode());
    }

    public static Object getClassInstanceByMethod(Method method) {
        return iocBeanMap.get(method.getDeclaringClass().getName());
    }

    public static Object[] getClassAopInstanceByMethod(Method method) {
        List<Object> objects = new ArrayList<>();
        Set<Class<? extends BotServiceInterceptor>> aopInterceptors = new HashSet<>();
        aopInterceptors.addAll(getBotAopInter(method.getDeclaringClass().getAnnotations()));
        aopInterceptors.addAll(getBotAopInter(method.getAnnotations()));
        for (Class<? extends BotServiceInterceptor> clazz : aopInterceptors) {
            objects.add(aopIocBeanMap.get(clazz.getName()));
        }
        return objects.toArray();
    }

    private static List<Class<? extends BotServiceInterceptor>> getBotAopInter(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(BotServiceAop.class)) {
                BotServiceAop botServiceAop = (BotServiceAop) annotation;
                return Arrays.asList(botServiceAop.value());
            }
        }
        return new ArrayList<>();
    }
}