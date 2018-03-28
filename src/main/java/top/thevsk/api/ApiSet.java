package top.thevsk.api;

import com.jfinal.aop.Enhancer;
import top.thevsk.entity.ReturnJson;
import top.thevsk.utils.NullUtils;

import java.util.HashMap;
import java.util.Map;

public class ApiSet {

    private static ApiBase apiBase = Enhancer.enhance(ApiBase.class);

    public static ReturnJson setGroupKick(Long groupId, Long userId, Boolean rejectAddRequest) {
        Map<String, Object> map = new HashMap<>();
        map.put("group_id", groupId);
        map.put("user_id", userId);
        map.put("reject_add_request", rejectAddRequest);
        return apiBase.post("set_group_kick", map);
    }

    public static ReturnJson setGroupBan(Long groupId, Long userId, Long duration) {
        Map<String, Object> map = new HashMap<>();
        map.put("group_id", groupId);
        map.put("user_id", userId);
        map.put("duration", duration);
        return apiBase.post("set_group_ban", map);
    }

    public static ReturnJson setGroupAnonymousBan(Long groupId, String flag, Long duration) {
        Map<String, Object> map = new HashMap<>();
        map.put("group_id", groupId);
        map.put("flag", flag);
        map.put("duration", duration);
        return apiBase.post("set_group_anonymous_ban", map);
    }

    public static ReturnJson setGroupWholeBan(Long groupId, Boolean enable) {
        Map<String, Object> map = new HashMap<>();
        map.put("group_id", groupId);
        map.put("enable", enable);
        return apiBase.post("set_group_whole_ban", map);
    }

    public static ReturnJson setGroupAdmin(Long groupId, Long userId, Boolean enable) {
        Map<String, Object> map = new HashMap<>();
        map.put("group_id", groupId);
        map.put("user_id", userId);
        map.put("enable", enable);
        return apiBase.post("set_group_admin", map);
    }

    public static ReturnJson setGroupAnonymous(Long groupId, Boolean enable) {
        Map<String, Object> map = new HashMap<>();
        map.put("group_id", groupId);
        map.put("enable", enable);
        return apiBase.post("set_group_anonymous", map);
    }

    public static ReturnJson setGroupCard(Long groupId, Long userId, String card) {
        Map<String, Object> map = new HashMap<>();
        map.put("group_id", groupId);
        map.put("user_id", userId);
        map.put("card", card);
        return apiBase.post("set_group_card", map);
    }

    public static ReturnJson setGroupLeave(Long groupId, Boolean isDismiss) {
        Map<String, Object> map = new HashMap<>();
        map.put("group_id", groupId);
        map.put("is_dismiss", isDismiss);
        return apiBase.post("set_group_leave", map);
    }

    public static ReturnJson setGroupSpecialTitle(Long groupId, Long userId, String specialTitle) {
        Map<String, Object> map = new HashMap<>();
        map.put("group_id", groupId);
        map.put("user_id", userId);
        map.put("special_title", specialTitle);
        map.put("duration", -1L);
        return apiBase.post("set_group_special_title", map);
    }

    public static ReturnJson setDiscussLeave(Long discussId) {
        Map<String, Object> map = new HashMap<>();
        map.put("discuss_id", discussId);
        return apiBase.post("set_discuss_leave", map);
    }

    public static ReturnJson setFriendAddRequest(String flag, Boolean approve, String remark) {
        Map<String, Object> map = new HashMap<>();
        map.put("flag", flag);
        map.put("approve", approve);
        if (NullUtils.isNotNullOrBlank(remark))
            map.put("remark", remark);
        return apiBase.post("set_friend_add_request", map);
    }

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
