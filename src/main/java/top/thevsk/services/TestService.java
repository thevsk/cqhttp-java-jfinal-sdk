package top.thevsk.services;

import top.thevsk.annotation.BotMessage;
import top.thevsk.annotation.BotService;
import top.thevsk.entity.ApiRequest;
import top.thevsk.entity.ApiResponse;
import top.thevsk.enums.MessageType;

@BotService
public class TestService {

    @BotMessage(messageType = MessageType.GROUP)
    public void funA(ApiRequest request, ApiResponse response) {
        System.out.println("group");
    }

    @BotMessage(messageType = MessageType.PRIVATE)
    public void funB(ApiRequest request, ApiResponse response) {
        System.out.println("private");
    }

    @BotMessage(messageType = MessageType.DISCUSS)
    public void funC(ApiRequest request, ApiResponse response) {
        System.out.println("discuss");
    }

    @BotMessage
    public void funD(ApiRequest request, ApiResponse response) {
        System.out.println("all");
    }
}