package top.thevsk.services;

import top.thevsk.annotation.BotMessage;
import top.thevsk.annotation.BotService;
import top.thevsk.entity.ApiRequest;
import top.thevsk.entity.ApiResponse;
import top.thevsk.enums.MessageType;

@BotService
public class TestService {

    /**
     * 群点歌
     */
    @BotMessage(messageType = MessageType.GROUP, filter = "startWith:点歌")
    public void music(ApiRequest request, ApiResponse response) {
        response.reply("用户 [CQ:at,qq=" + request.getUserId() + "] 使用了群点歌，内容：" + request.getMessage());
        //禁言
        response.ban(60L);
    }
}