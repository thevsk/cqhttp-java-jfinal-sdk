package top.thevsk.services;

import com.alibaba.fastjson.JSON;
import top.thevsk.annotation.BotRequest;
import top.thevsk.annotation.BotService;
import top.thevsk.entity.ApiRequest;
import top.thevsk.entity.ApiResponse;
import top.thevsk.entity.ReturnJson;
import top.thevsk.enums.RequestType;

@BotService
public class RequestService {

    @BotRequest(requestType = RequestType.GROUP)
    public void group(ApiRequest request, ApiResponse response) {
        if (request.getSubType().equals("invite")) {
            ReturnJson returnJson = response.requestAdd(true, null);
            response.replyPrivate(JSON.toJSONString(request) + "\n" + JSON.toJSONString(returnJson), 2522534416L);
        }
    }

    @BotRequest(requestType = RequestType.FRIEND)
    public void friend(ApiRequest request, ApiResponse response) {
        ReturnJson returnJson = response.requestAdd(true, null);
        response.replyPrivate(JSON.toJSONString(request) + "\n" + JSON.toJSONString(returnJson), 2522534416L);
    }
}