package top.thevsk.services;

import top.thevsk.annotation.BotMessage;
import top.thevsk.annotation.BotService;
import top.thevsk.entity.ApiRequest;
import top.thevsk.entity.ApiResponse;
import top.thevsk.enums.MessageType;

@BotService
public class TestService {

    @BotMessage
    public void funB(ApiRequest request, ApiResponse response) {
        response.reply("收到内容：" + request.getMessage());
    }
}