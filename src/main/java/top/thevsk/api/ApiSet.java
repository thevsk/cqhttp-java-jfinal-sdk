package top.thevsk.api;

import com.jfinal.aop.Enhancer;
import top.thevsk.entity.ReturnJson;
import top.thevsk.utils.NullUtils;

import java.util.HashMap;
import java.util.Map;

public class ApiSet {

    private static ApiBase apiBase = Enhancer.enhance(ApiBase.class);

    /**
     * 群组踢人
     *
     * @param groupId          群号
     * @param userId           要踢的 QQ 号
     * @param rejectAddRequest 拒绝此人的加群请求
     * @return
     */
    public static ReturnJson setGroupKick(Long groupId, Long userId, Boolean rejectAddRequest) {
        Map<String, Object> map = new HashMap<>();
        map.put("group_id", groupId);
        map.put("user_id", userId);
        map.put("reject_add_request", rejectAddRequest);
        return apiBase.post("set_group_kick", map);
    }

    /**
     * 群组单人禁言
     *
     * @param groupId  群号
     * @param userId   要禁言的 QQ 号
     * @param duration 禁言时长，单位秒，0 表示取消禁言
     * @return
     */
    public static ReturnJson setGroupBan(Long groupId, Long userId, Long duration) {
        Map<String, Object> map = new HashMap<>();
        map.put("group_id", groupId);
        map.put("user_id", userId);
        map.put("duration", duration);
        return apiBase.post("set_group_ban", map);
    }

    /**
     * 群组匿名用户禁言
     *
     * @param groupId  群号
     * @param flag     要禁言的匿名用户的 flag（需从群消息上报的数据中获得）
     * @param duration 禁言时长，单位秒，无法取消匿名用户禁言
     * @return
     */
    public static ReturnJson setGroupAnonymousBan(Long groupId, String flag, Long duration) {
        Map<String, Object> map = new HashMap<>();
        map.put("group_id", groupId);
        map.put("flag", flag);
        map.put("duration", duration);
        return apiBase.post("set_group_anonymous_ban", map);
    }

    /**
     * 群组全员禁言
     *
     * @param groupId 群号
     * @param enable  是否禁言
     * @return
     */
    public static ReturnJson setGroupWholeBan(Long groupId, Boolean enable) {
        Map<String, Object> map = new HashMap<>();
        map.put("group_id", groupId);
        map.put("enable", enable);
        return apiBase.post("set_group_whole_ban", map);
    }

    /**
     * 群组设置管理员
     *
     * @param groupId 群号
     * @param userId  要设置管理员的 QQ 号
     * @param enable  true 为设置，false 为取消
     * @return
     */
    public static ReturnJson setGroupAdmin(Long groupId, Long userId, Boolean enable) {
        Map<String, Object> map = new HashMap<>();
        map.put("group_id", groupId);
        map.put("user_id", userId);
        map.put("enable", enable);
        return apiBase.post("set_group_admin", map);
    }

    /**
     * 群组匿名
     *
     * @param groupId 群号
     * @param enable  是否允许匿名聊天
     * @return
     */
    public static ReturnJson setGroupAnonymous(Long groupId, Boolean enable) {
        Map<String, Object> map = new HashMap<>();
        map.put("group_id", groupId);
        map.put("enable", enable);
        return apiBase.post("set_group_anonymous", map);
    }

    /**
     * 设置群名片（群备注）
     *
     * @param groupId 群号
     * @param userId  要设置的 QQ 号
     * @param card    群名片内容，不填或空字符串表示删除群名片
     * @return
     */
    public static ReturnJson setGroupCard(Long groupId, Long userId, String card) {
        Map<String, Object> map = new HashMap<>();
        map.put("group_id", groupId);
        map.put("user_id", userId);
        map.put("card", card);
        return apiBase.post("set_group_card", map);
    }

    /**
     * 退出群组
     *
     * @param groupId   群号
     * @param isDismiss 是否解散，如果登录号是群主，则仅在此项为 true 时能够解散
     * @return
     */
    public static ReturnJson setGroupLeave(Long groupId, Boolean isDismiss) {
        Map<String, Object> map = new HashMap<>();
        map.put("group_id", groupId);
        map.put("is_dismiss", isDismiss);
        return apiBase.post("set_group_leave", map);
    }

    /**
     * 设置群组专属头衔
     *
     * @param groupId      群号
     * @param userId       要设置的 QQ 号
     * @param specialTitle 专属头衔，不填或空字符串表示删除专属头衔
     * @return
     */
    public static ReturnJson setGroupSpecialTitle(Long groupId, Long userId, String specialTitle) {
        Map<String, Object> map = new HashMap<>();
        map.put("group_id", groupId);
        map.put("user_id", userId);
        map.put("special_title", specialTitle);
        map.put("duration", -1L);
        return apiBase.post("set_group_special_title", map);
    }

    /**
     * 退出讨论组
     *
     * @param discussId 讨论组 ID（正常情况下看不到，需要从讨论组消息上报的数据中获得）
     * @return
     */
    public static ReturnJson setDiscussLeave(Long discussId) {
        Map<String, Object> map = new HashMap<>();
        map.put("discuss_id", discussId);
        return apiBase.post("set_discuss_leave", map);
    }

    /**
     * 处理加好友请求
     *
     * @param flag    加好友请求的 flag（需从上报的数据中获得）
     * @param approve 是否同意请求
     * @param remark  添加后的好友备注（仅在同意时有效）
     * @return
     */
    public static ReturnJson setFriendAddRequest(String flag, Boolean approve, String remark) {
        Map<String, Object> map = new HashMap<>();
        map.put("flag", flag);
        map.put("approve", approve);
        if (NullUtils.isNotNullOrBlank(remark))
            map.put("remark", remark);
        return apiBase.post("set_friend_add_request", map);
    }

    /**
     * 处理加群请求／邀请
     *
     * @param flag    加好友请求的 flag（需从上报的数据中获得）
     * @param type    add 或 invite，请求类型（需要和上报消息中的 sub_type 字段相符）
     * @param approve 是否同意请求／邀请
     * @param reason  拒绝理由（仅在拒绝时有效）
     * @return
     */
    public static ReturnJson setGroupAddRequest(String flag, String type, Boolean approve, String reason) {
        Map<String, Object> map = new HashMap<>();
        map.put("flag", flag);
        map.put("type", type);
        map.put("approve", approve);
        if (NullUtils.isNotNullOrBlank(reason))
            map.put("reason", reason);
        return apiBase.post("set_group_add_request", map);
    }
}
