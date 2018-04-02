package top.thevsk.services;

import top.thevsk.annotation.BotEvent;
import top.thevsk.annotation.BotService;
import top.thevsk.api.ApiGet;
import top.thevsk.entity.ApiRequest;
import top.thevsk.entity.ApiResponse;
import top.thevsk.entity.ReturnJson;
import top.thevsk.enums.EventType;

@BotService
public class WelcomeService {

    @BotEvent(eventType = EventType.GROUP_INCREASE)
    public void welocome(ApiRequest request, ApiResponse response) {
        response.reply("欢迎 [CQ:at,qq=" + request.getUserId() + "] 加入本群！");
    }

    @BotEvent(eventType = EventType.GROUP_DECREASE)
    public void leave(ApiRequest request, ApiResponse response) {
        try {
            ReturnJson returnJson = ApiGet.getStrangerInfo(request.getUserId(), false);
            if (returnJson != null && returnJson.getData().get("nickname") != null) {
                response.reply("成员 " + returnJson.getData().get("nickname") + " 离开本群");
            }
        } catch (Exception e) {
        }
    }
}