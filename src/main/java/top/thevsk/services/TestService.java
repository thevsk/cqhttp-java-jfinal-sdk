package top.thevsk.services;

import com.jfinal.kit.StrKit;
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

    @BotMessage(messageType = MessageType.GROUP, filter = "startWith:getQQ")
    public void getQQ(ApiRequest request, ApiResponse response) {
        String[] strings = CQUtils.getUserIdInCqAtMessage(request.getMessage());
        response.reply(StrKit.join(strings, ","));
    }

    @BotMessage(messageType = MessageType.GROUP, filter = "startWith:say")
    public void say(ApiRequest request, ApiResponse response) {
        response.reply(request.getMessage());
    }
}
