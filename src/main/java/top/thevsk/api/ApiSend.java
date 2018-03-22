package top.thevsk.api;

import top.thevsk.enums.MessageType;

import java.util.HashMap;
import java.util.Map;

public class ApiSend {

    public Map<String, String> sendMsg(MessageType messageType, Long userId, Long groupId, Long discussId, String message, Boolean autoEscape) {
        Map<String, Object> map = new HashMap<>();
        map.put("message_type", messageType.getCode());
        switch (messageType) {
            case PRIVATE:
                map.put("user_id", userId);
                break;
            case GROUP:
                map.put("group_id", groupId);
                break;
            case DISCUSS:
                map.put("discuss_id", discussId);
                break;
            default:
                throw new RuntimeException();
        }
        map.put("message", message);
        map.put("auto_escape", autoEscape);
        return ApiBase.post("send_msg", map);
    }

    public Map<String, String> sendMsg(MessageType messageType, Long userId, Long groupId, Long discussId, String message) {
        return sendMsg(messageType, userId, groupId, discussId, message, false);
    }

    public Map<String, String> sendPrivateMsg(Long userId, String message, Boolean autoEscape) {
        return sendMsg(MessageType.PRIVATE, userId, null, null, message, autoEscape);
    }

    public Map<String, String> sendPrivateMsg(Long userId, String message) {
        return sendPrivateMsg(userId, message, false);
    }

    public Map<String, String> sendGroupMsg(Long groupId, String message, Boolean autoEscape) {
        return sendMsg(MessageType.GROUP, null, groupId, null, message, autoEscape);
    }

    public Map<String, String> sendGroupMsg(Long groupId, String message) {
        return sendGroupMsg(groupId, message, false);
    }

    public Map<String, String> sendDiscussMsg(Long discussId, String message, Boolean autoEscape) {
        return sendMsg(MessageType.DISCUSS, null, null, discussId, message, autoEscape);
    }

    public Map<String, String> sendDiscussMsg(Long discussId, String message) {
        return sendDiscussMsg(discussId, message, false);
    }

    public Map<String, String> sendLike(Long userId, int times) {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("times", times);
        return ApiBase.post("send_like", map);
    }

    public Map<String, String> sendLike(Long userId) {
        return sendLike(userId, 10);
    }

    public Map<String, String> deleteMsg(Long messageId) {
        Map<String, Object> map = new HashMap<>();
        map.put("message_id", messageId);
        return ApiBase.post("delete_msg", map);
    }
}