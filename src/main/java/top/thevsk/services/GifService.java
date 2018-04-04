package top.thevsk.services;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.HttpKit;
import top.thevsk.annotation.BotMessage;
import top.thevsk.annotation.BotService;
import top.thevsk.entity.ApiRequest;
import top.thevsk.entity.ApiResponse;
import top.thevsk.utils.CQUtils;

import java.util.HashMap;
import java.util.Map;

@BotService
public class GifService {

    private final String path = "http://127.0.0.1:4567";
    private final String pathDown = "http://172.27.0.12:4567";

    @BotMessage(filter = "startWith:!王境泽,！王境泽")
    public void wangjingze(ApiRequest request, ApiResponse response) {
        try {
            String[] talk = request.getMessage().trim().split(" ");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("0", talk[0]);
            jsonObject.put("1", talk[1]);
            jsonObject.put("2", talk[2]);
            jsonObject.put("3", talk[3]);
            String url = browserPost(path + "/api/wangjingze/make", jsonObject.toJSONString());
            //response.reply("image created :" + url);
            response.reply(CQUtils.image(pathDown + url));
        } catch (Exception e) {
            response.reply("error:" + e.getMessage());
            response.reply("!王境泽 第一句 第二句 第三句 第四句");
        }
    }

    @BotMessage(filter = "startWith:!为所欲为,！为所欲为")
    public void sorry(ApiRequest request, ApiResponse response) {
        try {
            String[] talk = request.getMessage().trim().split(" ");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("0", talk[0]);
            jsonObject.put("1", talk[1]);
            jsonObject.put("2", talk[2]);
            jsonObject.put("3", talk[3]);
            jsonObject.put("4", talk[4]);
            jsonObject.put("5", talk[5]);
            jsonObject.put("6", talk[6]);
            jsonObject.put("7", talk[7]);
            jsonObject.put("8", talk[8]);
            String url = browserPost(path + "/api/sorry/make", jsonObject.toJSONString());
            //response.reply("image created :" + url);
            response.reply(CQUtils.image(pathDown + url));
        } catch (Exception e) {
            response.reply("error:" + e.getMessage());
            response.reply("!为所欲为 第一句 第二句 第三句 第四句 第五句 第六句(*) 第七句 第八句 第九句");
        }
    }

    private String browserPost(String url, String body) {
        Map<String, String> header = new HashMap<>();
        header.put("accept", "application/json");
        header.put("accept-encoding", "gzip, deflate");
        header.put("accept-language", "en-US,en;q=0.8");
        header.put("content-type", "application/json");
        header.put("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
        return HttpKit.post(url, body, header);
    }
}