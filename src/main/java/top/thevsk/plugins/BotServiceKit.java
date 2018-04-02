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
        eachBotMessage();
        eachBotRequest();
        eachBotEvent();
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
            if (EventType.DEFAULT.equals(requestType)) continue;
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
}