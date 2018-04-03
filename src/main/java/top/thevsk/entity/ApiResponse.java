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
     *
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
     *
     * @param message
     * @return
     */
    public ReturnJson replyAt(String message) {
        return reply(CQUtils.at(apiRequest.getUserId()) + " " + message);
    }

    /**
     * 踢出消息发送人 仅限群
     *
     * @return
     */
    public ReturnJson kick() {
        return kick(apiRequest.getUserId());
    }


    /**
     * 踢出某人 仅限群
     *
     * @return
     */
    public ReturnJson kick(Long userId) {
        if (NullUtils.isNotNullOrBlank(apiRequest.getGroupId(), userId)) {
            return ApiSet.setGroupKick(apiRequest.getGroupId(), userId, false);
        }
        return null;
    }

    /**
     * 禁言消息发送人 仅限群
     *
     * @param time
     * @return
     */
    public ReturnJson ban(Long time) {
        if (NullUtils.isNotNullOrBlank(apiRequest.getGroupId(), apiRequest.getFlag())) {
            return ApiSet.setGroupAnonymousBan(apiRequest.getGroupId(), apiRequest.getFlag(), time);
        }
        return ban(apiRequest.getUserId(), time);
    }

    /**
     * 禁言某人 仅限群
     *
     * @param time
     * @return
     */
    public ReturnJson ban(Long userId, Long time) {
        if (NullUtils.isNotNullOrBlank(apiRequest.getGroupId(), userId)) {
            return ApiSet.setGroupBan(apiRequest.getGroupId(), userId, time);
        }
        return null;
    }

    /**
     * 登录号退出群 or 讨论组
     *
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
     *
     * @param card
     * @return
     */
    public ReturnJson setCard(String card) {
        return setCard(apiRequest.getUserId(), card);
    }

    /**
     * 设置某人的名片
     *
     * @param card
     * @return
     */
    public ReturnJson setCard(Long userId, String card) {
        if (NullUtils.isNotNullOrBlank(apiRequest.getGroupId(), userId)) {
            return ApiSet.setGroupCard(apiRequest.getGroupId(), userId, card);
        }
        return null;
    }

    /**
     * 设置消息发送人头衔
     *
     * @param title
     * @return
     */
    public ReturnJson setSpecialTitle(String title) {
        return setSpecialTitle(apiRequest.getUserId(), title);
    }

    /**
     * 设置某人头衔
     *
     * @param title
     * @return
     */
    public ReturnJson setSpecialTitle(Long userId, String title) {
        if (NullUtils.isNotNullOrBlank(apiRequest.getGroupId(), userId)) {
            return ApiSet.setGroupSpecialTitle(apiRequest.getGroupId(), userId, title);
        }
        return null;
    }

    /**
     * 设置消息发送人为管理员 or 取消管理员
     *
     * @param enable
     * @return
     */
    public ReturnJson setAdmin(Boolean enable) {
        return setAdmin(apiRequest.getUserId(), enable);
    }

    /**
     * 设置某人为管理员 or 取消管理员
     *
     * @param enable
     * @return
     */
    public ReturnJson setAdmin(Long userId, Boolean enable) {
        if (NullUtils.isNotNullOrBlank(apiRequest.getGroupId(), userId)) {
            return ApiSet.setGroupAdmin(apiRequest.getGroupId(), userId, enable);
        }
        return null;
    }

    /**
     * 回复加群 or 加好友
     *
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
     *
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