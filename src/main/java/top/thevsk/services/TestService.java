package top.thevsk.services;

import top.thevsk.annotation.BotMessage;
import top.thevsk.annotation.BotService;
import top.thevsk.entity.ApiRequest;
import top.thevsk.entity.ApiResponse;
import top.thevsk.enums.MessageType;

@BotService
public class TestService{

    @BotMessage(messageType = MessageType.GROUP)
    public void funA(ApiRequest request, ApiResponse response) {
    }

    @BotMessage(messageType = MessageType.PRIVATE)
    public void funB(ApiRequest request, ApiResponse response) {
    }
}