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

    public static ReturnJson getStatus() {
        return apiBase.post("get_status", null);
    }

    public static ReturnJson getVersionInfo() {
        return apiBase.post("get_version_info", null);
    }

    public static ReturnJson setRestart() {
        return apiBase.post("set_restart", null);
    }

    public static ReturnJson setRestartPlugin() {
        return apiBase.post("set_restart_plugin", null);
    }

    public static ReturnJson cleanDataDir() {
        return apiBase.post("clean_data_dir", null);
    }
}