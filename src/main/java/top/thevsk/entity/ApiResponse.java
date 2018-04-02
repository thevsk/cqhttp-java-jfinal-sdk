package top.thevsk.entity;

import top.thevsk.api.ApiSend;
import top.thevsk.api.ApiSet;
import top.thevsk.enums.MessageType;
import top.thevsk.enums.RequestType;
import top.thevsk.utils.NullUtils;

public class ApiResponse {

    private ApiRequest apiRequest;

    public ApiResponse(ApiRequest apiRequest) {
        this.apiRequest = apiRequest;
    }

    public ReturnJson reply(String message) {
        if (apiRequest.getMessageType() != null) {
            switch (apiRequest.getMessageType()) {
                case DISCUSS:
                    return ApiSend.sendDiscussMsg(apiRequest.getDiscussId(), message, false);
                case GROUP:
                    return ApiSend.sendGroupMsg(apiRequest.getGroupId(), message, false);
                case PRIVATE:
                    return ApiSend.sendPrivateMsg(apiRequest.getUserId(), message, false);
                default:
                    return null;
            }
        }
        if (apiRequest.getGroupId() != null) {
            return ApiSend.sendGroupMsg(apiRequest.getGroupId(), message, false);
        }
        return null;
    }

    public ReturnJson kick() {
        if (NullUtils.isNotNullOrBlank(apiRequest.getGroupId(), apiRequest.getUserId())) {
            return ApiSet.setGroupKick(apiRequest.getGroupId(), apiRequest.getUserId(), false);
        }
        return null;
    }

    public ReturnJson ban(Long time) {
        if (NullUtils.isNotNullOrBlank(apiRequest.getGroupId(), apiRequest.getUserId())) {
            return ApiSet.setGroupBan(apiRequest.getGroupId(), apiRequest.getUserId(), time);
        }
        if (NullUtils.isNotNullOrBlank(apiRequest.getGroupId(), apiRequest.getFlag()) && MessageType.GROUP.equals(apiRequest.getMessageType())) {
            return ApiSet.setGroupAnonymousBan(apiRequest.getGroupId(), apiRequest.getFlag(), time);
        }
        return null;
    }

    public ReturnJson leave() {
        if (NullUtils.isNotNullOrBlank(apiRequest.getDiscussId())) {
            return ApiSet.setDiscussLeave(apiRequest.getDiscussId());
        }
        if (NullUtils.isNotNullOrBlank(apiRequest.getGroupId())) {
            return ApiSet.setGroupLeave(apiRequest.getGroupId(), false);
        }
        return null;
    }

    public ReturnJson setCard(String card) {
        if (NullUtils.isNotNullOrBlank(apiRequest.getGroupId(), apiRequest.getUserId())) {
            return ApiSet.setGroupCard(apiRequest.getGroupId(), apiRequest.getUserId(), card);
        }
        return null;
    }

    public ReturnJson setSpecialTitle(String title) {
        if (NullUtils.isNotNullOrBlank(apiRequest.getGroupId(), apiRequest.getUserId())) {
            return ApiSet.setGroupSpecialTitle(apiRequest.getGroupId(), apiRequest.getUserId(), title);
        }
        return null;
    }

    public ReturnJson setAdmin(Boolean enable) {
        if (NullUtils.isNotNullOrBlank(apiRequest.getGroupId(), apiRequest.getUserId())) {
            return ApiSet.setGroupAdmin(apiRequest.getGroupId(), apiRequest.getUserId(), enable);
        }
        return null;
    }

    public ReturnJson requestAdd(Boolean approve, String message) {
        if (!Constants.POST_TYPE_REQUEST.equals(apiRequest.getPostType())) return null;
        if (RequestType.FRIEND.equals(apiRequest.getMessageType())) {
            return ApiSet.setFriendAddRequest(apiRequest.getFlag(), approve, message);
        } else {
            return ApiSet.setGroupAddRequest(apiRequest.getFlag(), apiRequest.getSubType(), approve, message);
        }
    }
}