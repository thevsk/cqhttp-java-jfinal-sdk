package top.thevsk.services;

import top.thevsk.annotation.BotRequest;
import top.thevsk.annotation.BotService;
import top.thevsk.entity.ApiRequest;
import top.thevsk.entity.ApiResponse;
import top.thevsk.enums.RequestType;

@BotService
public class RequestService {

    @BotRequest(requestType = RequestType.GROUP)
    public void welocome(ApiRequest request, ApiResponse response) {
        if (request.getSubType().equals("invite")) {
            response.requestAdd(true, null);
        }
    }
}