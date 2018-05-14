package top.thevsk.services;

import top.thevsk.annotation.BotMessage;
import top.thevsk.annotation.BotService;
import top.thevsk.entity.ApiRequest;
import top.thevsk.entity.ApiResponse;
import top.thevsk.enums.MessageType;
import top.thevsk.utils.CQUtils;

import java.util.Map;

import static top.thevsk.utils.MessageUtils.getOrEx;
import static top.thevsk.utils.MessageUtils.parseMap;

@BotService
public class TestService {

    /**
     * 发送 share 卡片
     *
     * @param request
     * @param response
     */
    @BotMessage(messageType = MessageType.GROUP, filter = "startWith:!share")
    public void share(ApiRequest request, ApiResponse response) {
        try {
            Map<String, String> map = parseMap(request.getMessage());
            response.reply(CQUtils.share(getOrEx(map, "url"), getOrEx(map, "title"), getOrEx(map, "content"), getOrEx(map, "image")));
        } catch (Exception e) {
            response.replyAt(e.getMessage());
        }
    }

    /**
     * 发送 custom 卡片
     * @param request
     * @param response
     */
    @BotMessage(messageType = MessageType.GROUP, filter = "startWith:!custom")
    public void custom(ApiRequest request, ApiResponse response) {
        try {
            Map<String, String> map = parseMap(request.getMessage());
            response.reply(CQUtils.music(getOrEx(map, "url"), getOrEx(map, "audio"), getOrEx(map, "title"), getOrEx(map, "content"), getOrEx(map, "image")));
        } catch (Exception e) {
            response.replyAt(e.getMessage());
        }
    }

    /**
     * say 说话命令
     * @param request
     * @param response
     */
    @BotMessage(messageType = MessageType.GROUP, filter = "startWith:!say")
    public void say(ApiRequest request, ApiResponse response) {
        response.reply(request.getMessage());
    }

    /**
     * getUrl 获取图片内路径
     * @param request
     * @param response
     */
    @BotMessage(messageType = MessageType.GROUP, filter = "startWith:!getUrl")
    public void getUrl(ApiRequest request, ApiResponse response) {
        try {
            response.reply(CQUtils.getUrlInCqImage(request.getMessage().trim())[0]);
        } catch (Exception e) {
            response.replyAt(e.getMessage());
        }
    }
}
