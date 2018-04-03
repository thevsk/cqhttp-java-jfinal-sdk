package top.thevsk.api;

import com.jfinal.aop.Enhancer;
import top.thevsk.entity.ReturnJson;

import java.util.HashMap;
import java.util.Map;

public class ApiGet {

    private static ApiBase apiBase = Enhancer.enhance(ApiBase.class);

    /**
     * 获取登录号信息
     * @return
     * {
     *     user_id : QQ 号
     *     nickname : QQ 昵称
     * }
     */
    public static ReturnJson getLoginInfo() {
        return apiBase.post("get_login_info", null);
    }

    /**
     * 获取陌生人信息
     * @param userId qq
     * @param noCache 是否不使用缓存（使用缓存可能更新不及时，但响应更快）
     * @return
     * {
     *     user_id : QQ 号
     *     nickname : QQ 昵称
     *     sex : 性别，male 或 female 或 unknown
     *     age : 年龄
     * }
     */
    public static ReturnJson getStrangerInfo(Long userId, Boolean noCache) {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("no_cache", noCache);
        return apiBase.post("get_stranger_info", map);
    }

    /**
     * 获取群列表
     * @return
     * [
     *      {
     *          group_id : 群号
     *          group_name : 群名
     *      }
     * ]
     */
    public static ReturnJson getGroupList() {
        return apiBase.post("get_group_list", null);
    }

    /**
     * 获取群成员信息
     * @param groupId 群id
     * @param userId qq
     * @param noCache 缓存
     * @return
     * {
     *      group_id : 群号
     *      user_id : QQ 号
     *      nickname : 昵称
     *      card : 群名片／备注
     *      sex : 性别，male 或 female 或 unknown
     *      age : 年龄
     *      area : 地区
     *      join_time : 加群时间戳
     *      last_sent_time : 最后发言时间戳
     *      level : 成员等级
     *      role : 角色，owner 或 admin 或 member
     *      unfriendly : 是否不良记录成员
     *      title : 专属头衔
     *      title_expire_time : 专属头衔过期时间戳
     *      card_changeable : 是否允许修改群名片
     * }
     */
    public static ReturnJson getGroupMemberInfo(Long groupId, Long userId, Boolean noCache) {
        Map<String, Object> map = new HashMap<>();
        map.put("group_id", groupId);
        map.put("user_id", userId);
        map.put("no_cache", noCache);
        return apiBase.post("get_group_member_info", map);
    }

    /**
     * 获取群成员列表
     * @param groupId 群id
     * @return
     * [{...}]
     */
    public static ReturnJson getGroupMemberList(Long groupId) {
        Map<String, Object> map = new HashMap<>();
        map.put("group_id", groupId);
        return apiBase.post("get_group_member_list", map);
    }

    /**
     * 获取cookies
     * @return
     */
    public static ReturnJson getCookies() {
        return apiBase.post("get_cookies", null);
    }

    /**
     * 获取 CSRF Token
     * @return
     */
    public static ReturnJson getCsrfToken() {
        return apiBase.post("get_csrf_token", null);
    }

    /**
     * 获取好友列表 不稳定接口
     * @return
     * [
     *      {
     *          friend_group_id : 好友分组 ID
     *          friend_group_name : 好友分组名称
     *          friends : [
     *              {
     *                  nickname : 好友昵称
     *                  remark : 好友备注
     *                  user_id : 好友 QQ 号
     *              }
     *          ]
     *      }
     * ]
     */
    public static ReturnJson _getFriendList() {
        return apiBase.post("_get_friend_list", null);
    }
}