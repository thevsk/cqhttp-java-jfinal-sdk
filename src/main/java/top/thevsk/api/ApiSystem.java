package top.thevsk.api;

import com.jfinal.aop.Enhancer;
import top.thevsk.entity.ReturnJson;

import java.util.HashMap;
import java.util.Map;

public class ApiSystem {

    private static ApiBase apiBase = Enhancer.enhance(ApiBase.class);

    public static ReturnJson getRecord(String file, String out_format) {
        Map<String, Object> map = new HashMap<>();
        map.put("file", file);
        map.put("outFormat", out_format);
        return apiBase.post("get_record", map);
    }

    /**
     * 获取插件运行状态
     *
     * @return
     * {
     *      good : 插件状态符合预期，意味着插件已初始化，需要启动的服务都在正常运行，且 QQ 在线
     *      app_initialized : 插件已初始化
     *      app_enabled : 插件已启用
     *      online : 当前 QQ 在线
     *      http_service_good : use_http 配置项为 yes 时有此字段，表示 HTTP 服务正常运行
     *      ws_service_good : use_ws 配置项为 yes 时有此字段，表示 WebSocket 服务正常运行
     *      ws_reverse_service_good : use_ws_reverse 配置项为 yes 时有此字段，表示反向 WebSocket 服务正常运行
     * }
     */
    public static ReturnJson getStatus() {
        return apiBase.post("get_status", null);
    }

    /**
     * 获取酷 Q 及 HTTP API 插件的版本信息
     *
     * @return
     * {
     *      coolq_directory : 酷 Q 根目录路径
     *      coolq_edition : 酷 Q 版本，air 或 pro
     *      plugin_version : HTTP API 插件版本，例如 2.1.3
     *      plugin_build_number : HTTP API 插件 build 号
     *      plugin_build_configuration : HTTP API 插件编译配置，debug 或 release
     * }
     */
    public static ReturnJson getVersionInfo() {
        return apiBase.post("get_version_info", null);
    }

    /**
     * 重启酷 Q，并以当前登录号自动登录（需勾选快速登录）
     *
     * @return
     */
    public static ReturnJson setRestart() {
        return apiBase.post("set_restart", null);
    }

    /**
     * 重启 HTTP API 插件
     *
     * @return
     */
    public static ReturnJson setRestartPlugin() {
        return apiBase.post("set_restart_plugin", null);
    }

    /**
     * 清理数据目录
     *
     * @return
     */
    public static ReturnJson cleanDataDir() {
        return apiBase.post("clean_data_dir", null);
    }
}