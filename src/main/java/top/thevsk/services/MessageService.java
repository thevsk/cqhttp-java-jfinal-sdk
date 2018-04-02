package top.thevsk.services;

import top.thevsk.annotation.BotMessage;
import top.thevsk.annotation.BotService;
import top.thevsk.entity.ApiRequest;
import top.thevsk.entity.ApiResponse;
import top.thevsk.enums.MessageType;

import java.util.HashMap;
import java.util.Map;

@BotService
public class MessageService {

    private static Map<String, String> msgs = new HashMap<>();

    @BotMessage(messageType = MessageType.GROUP, filter = "startWith:save")
    public void save(ApiRequest request, ApiResponse response) {
        String message = request.getMessage().trim();
        try {
            msgs.put(message.substring(0, message.indexOf(" ")), message.substring(message.indexOf(" ") + 1, message.length()));
            response.reply("success");
        } catch (Exception e) {
            response.reply(e.getMessage());
        }
    }

    @BotMessage(messageType = MessageType.GROUP)
    public void read(ApiRequest request, ApiResponse response) {
        try {
            if (msgs.get(request.getMessage()) != null) {
                response.reply(msgs.get(request.getMessage()));
            }
        } catch (Exception e) {
            response.reply(e.getMessage());
        }
    }

    @BotMessage(messageType = MessageType.GROUP, filter = "startWith:remove")
    public void remove(ApiRequest request, ApiResponse response) {
        String message = request.getMessage().trim();
        try {
            msgs.remove(message);
            response.reply("success");
        } catch (Exception e) {
            response.reply(e.getMessage());
        }
    }

    @BotMessage(messageType = MessageType.GROUP, filter = "eq:removeAll")
    public void removeAll(ApiRequest request, ApiResponse response) {
        try {
            msgs.clear();
            response.reply("success");
        } catch (Exception e) {
            response.reply(e.getMessage());
        }
    }
}