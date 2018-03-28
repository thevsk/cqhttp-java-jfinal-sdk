package top.thevsk.api;

import com.jfinal.aop.Enhancer;
import top.thevsk.entity.ReturnJson;

import java.util.HashMap;
import java.util.Map;

public class ApiGet {

    private static ApiBase apiBase = Enhancer.enhance(ApiBase.class);

    public static ReturnJson getLoginInfo() {
        return apiBase.post("get_login_info", null);
    }

    public static ReturnJson getStrangerInfo(Long userId, Boolean noCache) {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("no_cache", noCache);
        return apiBase.post("get_stranger_info", map);
    }

    public static ReturnJson getGroupList() {
        return apiBase.post("get_group_list", null);
    }

    public static ReturnJson getGroupMemberInfo(Long groupId, Long userId, Boolean noCache) {
        Map<String, Object> map = new HashMap<>();
        map.put("group_id", groupId);
        map.put("user_id", userId);
        map.put("no_cache", noCache);
        return apiBase.post("get_group_member_info", map);
    }

    public static ReturnJson getGroupMemberList(Long groupId) {
        Map<String, Object> map = new HashMap<>();
        map.put("group_id", groupId);
        return apiBase.post("get_group_member_list", map);
    }

    public static ReturnJson getCookies() {
        return apiBase.post("get_cookies", null);
    }

    public static ReturnJson getCsrfToken() {
        return apiBase.post("get_csrf_token", null);
    }

    public static ReturnJson _getFriendList() {
        return apiBase.post("_get_friend_list", null);
    }
}