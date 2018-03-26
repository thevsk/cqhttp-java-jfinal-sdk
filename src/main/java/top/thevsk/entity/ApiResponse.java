package top.thevsk.entity;

import top.thevsk.api.ApiSend;

public class ApiResponse {

    private ApiRequest apiRequest;

    public ApiResponse(ApiRequest apiRequest) {
        this.apiRequest = apiRequest;
    }

    public ReturnJson reply(String message) {
        if (!Constants.POST_TYPE_MESSAGE.equals(apiRequest.getPostType())) return null;
        switch (apiRequest.getMessageType()) {
            case DISCUSS:
                return ApiSend.sendDiscussMsg(apiRequest.getDiscussId(), message);
            case GROUP:
                return ApiSend.sendGroupMsg(apiRequest.getGroupId(), message);
            case PRIVATE:
                return ApiSend.sendPrivateMsg(apiRequest.getUserId(), message);
            default:
                return null;
        }
    }
}