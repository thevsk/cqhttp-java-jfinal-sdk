package top.thevsk.services;

import top.thevsk.annotation.BotMessage;
import top.thevsk.annotation.BotService;
import top.thevsk.entity.ApiRequest;
import top.thevsk.entity.ApiResponse;
import top.thevsk.enums.MessageType;
import top.thevsk.utils.CQUtils;

import java.util.Random;

@BotService
public class RandomService {

    @BotMessage(messageType = MessageType.GROUP, filter = "eq:随机数")
    public void random20(ApiRequest request, ApiResponse response) {
        response.replyAt(new Random().nextInt(20) + 1 + "");
    }
}