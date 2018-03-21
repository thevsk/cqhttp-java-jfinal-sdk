package top.thevsk.plugins;

import top.thevsk.annotation.BotEvent;
import top.thevsk.annotation.BotMessage;
import top.thevsk.annotation.BotRequest;
import top.thevsk.enums.EventType;
import top.thevsk.enums.MessageType;
import top.thevsk.enums.RequestType;
import top.thevsk.enums.SubType;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BotServiceKit {
    private static List<Method> botMessageMethods;
    private static List<Method> botRequestMethods;
    private static List<Method> botEventMethods;
    private static Map<String, List<Method>> botMessageMethodsMap = new HashMap<>();
    private static Map<String, List<Method>> botRequestMethodsMap = new HashMap<>();
    private static Map<String, List<Method>> botEventMethodsMap = new HashMap<>();

    public static void init(List<Method> botMessageMethods, List<Method> botRequestMethods, List<Method> botEventMethods) {
        BotServiceKit.botMessageMethods = botMessageMethods;
        BotServiceKit.botRequestMethods = botRequestMethods;
        BotServiceKit.botEventMethods = botEventMethods;
    }

    public static List<Method> getBotMessageMethods(MessageType messageType) {
        List<Method> result = botMessageMethodsMap.get("MessageType:" + messageType.getCode());
        if (result == null) {
            result = new ArrayList<>();
            for (Method method : botMessageMethods) {
                if (messageType.equals(method.getAnnotation(BotMessage.class).messageType())) {
                    result.add(method);
                }
            }
            botMessageMethodsMap.put("MessageType:" + messageType.getCode(), result);
        }
        return result;
    }

    public static List<Method> getBotRequestMethods(RequestType requestType, SubType subType) {
        List<Method> result = botRequestMethodsMap.get("RequestType:" + requestType.getCode() + "|SubType:" + subType.getCode());
        if (result == null) {
            result = new ArrayList<>();
            for (Method method : botRequestMethods) {
                BotRequest botRequest = method.getAnnotation(BotRequest.class);
                if (requestType.equals(botRequest.requestType()) && subType.equals(botRequest.subType())) {
                    result.add(method);
                }
            }
            botRequestMethodsMap.put("RequestType:" + requestType.getCode() + "|SubType:" + subType.getCode(), result);
        }
        return result;
    }

    public static List<Method> getBotEventMethods(EventType eventType, SubType subType) {
        List<Method> result = botEventMethodsMap.get("EventType:" + eventType.getCode() + "|SubType:" + subType.getCode());
        if (result == null) {
            result = new ArrayList<>();
            for (Method method : botEventMethods) {
                BotEvent botEvent = method.getAnnotation(BotEvent.class);
                if (eventType.equals(botEvent.eventType()) && subType.equals(botEvent.subType())) {
                    result.add(method);
                }
            }
            botEventMethodsMap.put("EventType:" + eventType.getCode() + "|SubType:" + subType.getCode(), result);
        }
        return result;
    }
}