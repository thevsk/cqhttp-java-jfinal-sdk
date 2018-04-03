package top.thevsk.api;

import com.jfinal.aop.Enhancer;
import top.thevsk.entity.ReturnJson;
import top.thevsk.enums.MessageType;

import java.util.HashMap;
import java.util.Map;

public class ApiSend {

    private static ApiBase apiBase = Enhancer.enhance(ApiBase.class);

    /**
     * 发送消息
     *
     * @param messageType 消息类型，支持 private、group、discuss，分别对应私聊、群组、讨论组
     * @param id
     * @param message     要发送的内容
     * @param autoEscape  消息内容是否作为纯文本发送（即不解析 CQ 码），message 数据类型为 array 时无效
     * @return
     */
    public static ReturnJson sendMsg(MessageType messageType, Long id, String message, Boolean autoEscape) {
        Map<String, Object> map = new HashMap<>();
        map.put("message_type", messageType.getCode());
        switch (messageType) {
            case PRIVATE:
                map.put("user_id", id);
                break;
            case GROUP:
                map.put("group_id", id);
                break;
            case DISCUSS:
                map.put("discuss_id", id);
                break;
            default:
                throw new RuntimeException();
        }
        map.put("message", message);
        map.put("auto_escape", autoEscape);
        return apiBase.post("send_msg", map);
    }

    /**
     * 发送私人消息
     *
     * @param userId     对方 QQ 号
     * @param message    要发送的内容
     * @param autoEscape 消息内容是否作为纯文本发送（即不解析 CQ 码），message 数据类型为 array 时无效
     * @return
     */
    public static ReturnJson sendPrivateMsg(Long userId, String message, Boolean autoEscape) {
        return sendMsg(MessageType.PRIVATE, userId, message, autoEscape);
    }

    /**
     * 发送群消息
     *
     * @param groupId    群号
     * @param message    要发送的内容
     * @param autoEscape 消息内容是否作为纯文本发送（即不解析 CQ 码），message 数据类型为 array 时无效
     * @return
     */
    public static ReturnJson sendGroupMsg(Long groupId, String message, Boolean autoEscape) {
        return sendMsg(MessageType.GROUP, groupId, message, autoEscape);
    }

    /**
     * 发送讨论组消息
     *
     * @param discussId  讨论组 ID（正常情况下看不到，需要从讨论组消息上报的数据中获得）
     * @param message    要发送的内容
     * @param autoEscape 消息内容是否作为纯文本发送（即不解析 CQ 码），message 数据类型为 array 时无效
     * @return
     */
    public static ReturnJson sendDiscussMsg(Long discussId, String message, Boolean autoEscape) {
        return sendMsg(MessageType.DISCUSS, discussId, message, autoEscape);
    }

    /**
     * 发送好友赞
     *
     * @param userId 对方 QQ 号
     * @param times  赞的次数，每个好友每天最多 10 次
     * @return
     */
    public static ReturnJson sendLike(Long userId, int times) {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("times", times);
        return apiBase.post("send_like", map);
    }

    /**
     * 撤回消息
     *
     * @param messageId 消息 ID
     * @return
     */
    public static ReturnJson deleteMsg(Long messageId) {
        Map<String, Object> map = new HashMap<>();
        map.put("message_id", messageId);
        return apiBase.post("delete_msg", map);
    }
}