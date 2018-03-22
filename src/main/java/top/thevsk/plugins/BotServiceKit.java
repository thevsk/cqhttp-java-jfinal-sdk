package top.thevsk.plugins;

import top.thevsk.annotation.BotEvent;
import top.thevsk.annotation.BotMessage;
import top.thevsk.annotation.BotRequest;
import top.thevsk.enums.EventType;
import top.thevsk.enums.MessageType;
import top.thevsk.enums.RequestType;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public class BotServiceKit {
    private static Set<Method> botMessageMethods;
    private static Set<Method> botRequestMethods;
    private static Set<Method> botEventMethods;
    private static Map<String, Set<Method>> botMessageMethodsMap = new HashMap<>();
    private static Map<String, Set<Method>> botRequestMethodsMap = new HashMap<>();
    private static Map<String, Set<Method>> botEventMethodsMap = new HashMap<>();
    private static ConcurrentMap<String, Object> iocBeanMap;

    public static void init(Set<Method> botMessageMethods, Set<Method> botRequestMethods, Set<Method> botEventMethods, ConcurrentMap<String, Object> iocBeanMap) {
        BotServiceKit.botMessageMethods = botMessageMethods;
        BotServiceKit.botRequestMethods = botRequestMethods;
        BotServiceKit.botEventMethods = botEventMethods;
        BotServiceKit.iocBeanMap = iocBeanMap;
    }

    public static Set<Method> getBotMessageMethods(MessageType messageType) {
        Set<Method> result = botMessageMethodsMap.get("MessageType:" + messageType.getCode());
        if (result == null) {
            result = new HashSet<>();
            for (Method method : botMessageMethods) {
                BotMessage botMessage = method.getAnnotation(BotMessage.class);
                if (messageType.equals(botMessage.messageType())) {
                    result.add(method);
                }
            }
            botMessageMethodsMap.put("MessageType:" + messageType.getCode(), result);
        }
        return result;
    }

    public static Set<Method> getBotRequestMethods(RequestType requestType) {
        Set<Method> result = botRequestMethodsMap.get("RequestType:" + requestType.getCode());
        if (result == null) {
            result = new HashSet<>();
            for (Method method : botRequestMethods) {
                BotRequest botRequest = method.getAnnotation(BotRequest.class);
                if (requestType.equals(botRequest.requestType())) {
                    result.add(method);
                }
            }
            botRequestMethodsMap.put("RequestType:" + requestType.getCode(), result);
        }
        return result;
    }

    public static Set<Method> getBotEventMethods(EventType eventType) {
        Set<Method> result = botEventMethodsMap.get("EventType:" + eventType.getCode());
        if (result == null) {
            result = new HashSet<>();
            for (Method method : botEventMethods) {
                BotEvent botEvent = method.getAnnotation(BotEvent.class);
                if (eventType.equals(botEvent.eventType())) {
                    result.add(method);
                }
            }
            botEventMethodsMap.put("EventType:" + eventType.getCode(), result);
        }
        return result;
    }

    public static Object getClassInstanceByMethod(Method method) {
        return iocBeanMap.get(method.getDeclaringClass().getName());
    }
}