package top.thevsk.services;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.HttpKit;
import top.thevsk.annotation.BotMessage;
import top.thevsk.annotation.BotService;
import top.thevsk.entity.ApiRequest;
import top.thevsk.entity.ApiResponse;
import top.thevsk.enums.MessageType;
import top.thevsk.utils.CQUtils;
import top.thevsk.utils.NullUtils;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@BotService(state = false)
public class MusicService {

    @BotMessage(messageType = MessageType.GROUP, filter = "startWith:点歌,點歌")
    public void music(ApiRequest request, ApiResponse response) {
        String id = post(request.getMessage());
        if (NullUtils.isNotNullOrBlank(id)) {
            response.reply(CQUtils.music("163", id));
        } else {
            response.reply("songs not find");
        }
    }

    private String post(String message) {
        try {
            String url = "http://music.163.com/api/search/get/web";
            String bodyStr = "s=" + URLEncoder.encode(message.trim(), "UTF-8") + "&limit=1&type=1";
            Map<String, String> header = new HashMap<>();
            header.put("cache-control", "no-cache");
            header.put("Content-Type", "application/x-www-form-urlencoded");
            header.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.75 Safari/537.36");
            header.put("Accept", "*/*");
            header.put("Host", "music.163.com");
            header.put("accept-encoding", "deflate");
            header.put("content-length", bodyStr.length() + "");
            header.put("Connection", "keep-alive");
            String result = HttpKit.post(url, bodyStr, header);
            JSONObject jsonObject = JSON.parseObject(result);
            return jsonObject.getJSONObject("result").getJSONArray("songs").getJSONObject(0).getString("id");
        } catch (Exception e) {
            return null;
        }
    }
}