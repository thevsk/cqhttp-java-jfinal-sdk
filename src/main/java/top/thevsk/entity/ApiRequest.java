package top.thevsk.entity;

import top.thevsk.enums.MessageType;
import top.thevsk.enums.NoticeType;
import top.thevsk.enums.RequestType;

import java.util.HashMap;
import java.util.Map;

public class ApiRequest {

    private Map<String, Object> map = new HashMap<>();

    public ApiRequest(Map<String, Object> map) {
        this.map = map;
    }

    public void set(String key, Object value) {
        map.put(key, value);
    }

    private String getStr(String key) {
        if (map.get(key) != null) {
            return map.get(key).toString();
        }
        return null;
    }

    private Long getLong(String key) {
        if (map.get(key) != null) {
            return Long.valueOf(map.get(key).toString());
        }
        return null;
    }

    /**
     * 获取上报类型
     *
     * @return
     */
    public String getPostType() {
        return getStr("post_type");
    }

    /**
     * 获取子类型
     *
     * @return
     */
    public String getSubType() {
        return getStr("sub_type");
    }

    /**
     * 获取事件产生者 QQ 号
     *
     * @return
     */
    public Long getUserId() {
        return getLong("user_id");
    }

    /**
     * 获取事件产生群号
     *
     * @return
     */
    public Long getGroupId() {
        return getLong("group_id");
    }

    /**
     * 请求 flag，在调用处理请求的 API 时需要传入
     *
     * @return
     */
    public String getFlag() {
        return getStr("flag");
    }

    /**
     * 获取消息类型
     *
     * @return
     */
    public MessageType getMessageType() {
        String messageType = getStr("message_type");
        if (messageType == null) return null;
        return MessageType.valueOf(messageType.toUpperCase());
    }

    /**
     * 获取事件类型
     *
     * @return
     */
    public NoticeType getNoticeType() {
        String noticeType = getStr("notice_type");
        if (noticeType == null) return null;
        return NoticeType.valueOf(noticeType.toUpperCase());
    }

    /**
     * 请求类型
     *
     * @return
     */
    public RequestType getRequestType() {
        String requestType = getStr("request_type");
        if (requestType == null) return null;
        return RequestType.valueOf(requestType.toUpperCase());
    }

    /**
     * 获取消息内容
     *
     * @return
     */
    public String getMessage() {
        return getStr("message");
    }

    /**
     * 获取字体
     *
     * @return
     */
    public Long getFront() {
        return getLong("font");
    }

    /**
     * 获取消息id
     *
     * @return
     */
    public Long getMessageId() {
        return getLong("message_id");
    }

    /**
     * 获取讨论组id
     *
     * @return
     */
    public Long getDiscussId() {
        return getLong("discuss_id");
    }

    /**
     * 获取匿名用户显示名
     *
     * @return
     */
    public String getAnonymous() {
        return getStr("anonymous");
    }

    /**
     * 获取匿名用户 flag，在调用禁言 API 时需要传入
     *
     * @return
     */
    public String getAnonymousFlag() {
        return getStr("anonymous_flag");
    }

    /**
     * 获取操作者 QQ 号
     *
     * @return
     */
    public Long getOperatorId() {
        return getLong("operator_id");
    }

    /**
     * 获取机器人的 QQ 号
     *
     * @return
     */
    public Long getSelfId() {
        return getLong("self_id");
    }

    /**
     * 是否为机器人自己产生的消息
     *
     * @return
     */
    public boolean isSelf() {
        return getSelfId().equals(getUserId());
    }
}
