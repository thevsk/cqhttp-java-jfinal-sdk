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

@BotService(enable = false)
public class MusicService {

    @BotMessage(messageType = MessageType.GROUP, filter = "startWith:点歌,點歌")
    public void music(ApiRequest request, ApiResponse response) {
        String result = postQQ(request.getMessage());
        if (NullUtils.isNotNullOrBlank(result)) {
            response.reply(result);
        } else {
            response.reply("songs not find 歌曲未找到或不存在");
        }
    }

    /**
     * 163网易点歌，如果 coolQ 在 docker 内会卡顿并阻塞，原因未知
     *
     * @param message
     * @return
     */
    private String post163(String message) {
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
            String id = jsonObject.getJSONObject("result").getJSONArray("songs").getJSONObject(0).getString("id");
            return CQUtils.music("163", id);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * qq点歌，可以正常使用
     *
     * @param message
     * @return
     */
    private String postQQ(String message) {
        try {
            String url = "http://s.music.qq.com/fcgi-bin/music_search_new_platform?t=0&n=1&aggr=1&cr=1&loginUin=0&format=json&inCharset=GB2312&outCharset=utf-8&notice=0&platform=jqminiframe.json&needNewCode=0&p=1&catZhida=0&remoteplace=sizer.newclient.next_song&w=";
            String result = HttpKit.get(url + URLEncoder.encode(message.trim(), "UTF-8"));
            JSONObject jsonObject = JSON.parseObject(result);
            String id = jsonObject.getJSONObject("data").getJSONObject("song").getJSONArray("list").getJSONObject(0).getString("f").split("\\|")[0];
            return CQUtils.music("qq", id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}