package top.thevsk.entity;

import top.thevsk.api.ApiGet;
import top.thevsk.api.ApiSend;
import top.thevsk.api.ApiSet;
import top.thevsk.enums.MessageType;
import top.thevsk.enums.RequestType;
import top.thevsk.utils.CQUtils;
import top.thevsk.utils.NullUtils;

public class ApiResponse {

    private ApiRequest apiRequest;

    public ApiResponse(ApiRequest apiRequest) {
        this.apiRequest = apiRequest;
    }

    /**
     * 回复消息
     * @param message
     * @return
     */
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

    /**
     * 回复消息，并且 @ 消息发送人
     * @param message
     * @return
     */
    public ReturnJson replyAt(String message) {
        return reply(CQUtils.at(apiRequest.getUserId()) + " " + message);
    }

    /**
     * 踢出消息发送人 仅限群
     * @return
     */
    public ReturnJson kick() {
        if (NullUtils.isNotNullOrBlank(apiRequest.getGroupId(), apiRequest.getUserId())) {
            return ApiSet.setGroupKick(apiRequest.getGroupId(), apiRequest.getUserId(), false);
        }
        return null;
    }

    /**
     * 禁言消息发送人 仅限群
     * @param time
     * @return
     */
    public ReturnJson ban(Long time) {
        if (NullUtils.isNotNullOrBlank(apiRequest.getGroupId(), apiRequest.getUserId())) {
            return ApiSet.setGroupBan(apiRequest.getGroupId(), apiRequest.getUserId(), time);
        }
        if (NullUtils.isNotNullOrBlank(apiRequest.getGroupId(), apiRequest.getFlag()) && MessageType.GROUP.equals(apiRequest.getMessageType())) {
            return ApiSet.setGroupAnonymousBan(apiRequest.getGroupId(), apiRequest.getFlag(), time);
        }
        return null;
    }

    /**
     * 登录号退出群 or 讨论组
     * @return
     */
    public ReturnJson leave() {
        if (NullUtils.isNotNullOrBlank(apiRequest.getDiscussId())) {
            return ApiSet.setDiscussLeave(apiRequest.getDiscussId());
        }
        if (NullUtils.isNotNullOrBlank(apiRequest.getGroupId())) {
            return ApiSet.setGroupLeave(apiRequest.getGroupId(), false);
        }
        return null;
    }

    /**
     * 设置消息发送人的名片
     * @param card
     * @return
     */
    public ReturnJson setCard(String card) {
        if (NullUtils.isNotNullOrBlank(apiRequest.getGroupId(), apiRequest.getUserId())) {
            return ApiSet.setGroupCard(apiRequest.getGroupId(), apiRequest.getUserId(), card);
        }
        return null;
    }

    /**
     * 设置消息发送人头衔
     * @param title
     * @return
     */
    public ReturnJson setSpecialTitle(String title) {
        if (NullUtils.isNotNullOrBlank(apiRequest.getGroupId(), apiRequest.getUserId())) {
            return ApiSet.setGroupSpecialTitle(apiRequest.getGroupId(), apiRequest.getUserId(), title);
        }
        return null;
    }

    /**
     * 设置消息发送人为管理员 or 取消管理员
     * @param enable
     * @return
     */
    public ReturnJson setAdmin(Boolean enable) {
        if (NullUtils.isNotNullOrBlank(apiRequest.getGroupId(), apiRequest.getUserId())) {
            return ApiSet.setGroupAdmin(apiRequest.getGroupId(), apiRequest.getUserId(), enable);
        }
        return null;
    }

    /**
     * 回复加群 or 加好友
     * @param approve
     * @param message
     * @return
     */
    public ReturnJson requestAdd(Boolean approve, String message) {
        if (!Constants.POST_TYPE_REQUEST.equals(apiRequest.getPostType())) return null;
        if (RequestType.FRIEND.equals(apiRequest.getMessageType())) {
            return ApiSet.setFriendAddRequest(apiRequest.getFlag(), approve, message);
        } else {
            return ApiSet.setGroupAddRequest(apiRequest.getFlag(), apiRequest.getSubType(), approve, message);
        }
    }

    /**
     * 获取登录号 qq id
     * @return
     */
    public Long getSelfId() {
        try {
            ReturnJson returnJson = ApiGet.getLoginInfo();
            if (returnJson != null) {
                return Long.valueOf(returnJson.getData().get("user_id").toString());
            }
        } catch (Exception e) {
        }
        return null;
    }
}