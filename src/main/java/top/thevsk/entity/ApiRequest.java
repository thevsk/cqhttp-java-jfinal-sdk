package top.thevsk.entity;

import java.util.HashMap;
import java.util.Map;

public class ApiRequest {

    public ApiRequest(Map<String, Object> map) {
        this.map = map;
    }

    private Map<String, Object> map = new HashMap<>();

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
    public String getMessageType() {
        return getStr("message_type");
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
     * 获取事件类型
     *
     * @return
     */
    public String getEvent() {
        return getStr("event");
    }

    /**
     * 获取操作者 QQ 号
     *
     * @return
     */
    public Long getOperatorId() {
        return getLong("operator_id");
    }
}
