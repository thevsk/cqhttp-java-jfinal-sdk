package top.thevsk.services;

import top.thevsk.annotation.BotMessage;
import top.thevsk.annotation.BotService;
import top.thevsk.entity.ApiRequest;
import top.thevsk.entity.ApiResponse;
import top.thevsk.enums.MessageType;
import top.thevsk.utils.CQUtils;

@BotService
public class TestService {

    @BotMessage(messageType = MessageType.GROUP, filter = "startWith:share")
    public void share(ApiRequest request, ApiResponse response) {
        try {
            String[] str = request.getMessage().trim().split(" ");
            response.reply(CQUtils.share(str[0], str[1], str[2], str[3]));
        } catch (Exception e) {
            response.replyAt(e.getMessage());
        }
    }
}
