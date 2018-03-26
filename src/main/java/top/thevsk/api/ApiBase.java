package top.thevsk.api;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Before;
import com.jfinal.kit.HttpKit;
import top.thevsk.entity.Constants;
import top.thevsk.entity.ReturnJson;
import top.thevsk.interceptor.ReturnJsonInterceptor;

import java.util.HashMap;
import java.util.Map;

public class ApiBase {

    @Before(ReturnJsonInterceptor.class)
    public static ReturnJson post(String url, Map<String, Object> map) {
        String path = Constants.getCfg("http.api.url") + url;
        Map<String, String> header = new HashMap<>();
        header.put("accept", "application/json");
        header.put("accept-encoding", "gzip, deflate");
        header.put("accept-language", "en-US,en;q=0.8");
        header.put("content-type", "application/json");
        header.put("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
        if (Constants.getCfg("http.api.access_token") != null) {
            header.put("Authorization", "Token " + Constants.getCfg("http.api.access_token").trim());
        }
        String result = HttpKit.post(path, JSON.toJSONString(map), header);
        return JSON.parseObject(result, ReturnJson.class);
    }
}