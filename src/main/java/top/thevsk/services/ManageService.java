package top.thevsk.services;

import top.thevsk.annotation.BotMessage;
import top.thevsk.annotation.BotService;
import top.thevsk.api.ApiGet;
import top.thevsk.api.ApiSet;
import top.thevsk.api.ApiSystem;
import top.thevsk.entity.ApiRequest;
import top.thevsk.entity.ApiResponse;
import top.thevsk.entity.ReturnJson;
import top.thevsk.enums.MessageType;

@BotService
public class ManageService {

    @BotMessage(messageType = MessageType.PRIVATE, filter = "eq:!groupList")
    public void groupList(ApiRequest request, ApiResponse response) {
        try {
            ReturnJson returnJson = ApiGet.getGroupList();
            if (returnJson.getRetcode() == 0) {
                StringBuffer sbf = new StringBuffer();
                sbf.append("Num:" + returnJson.getDataList().size());
                sbf.append("\n");
                for (int i = 0; i < returnJson.getDataList().size(); i++) {
                    if (i != 0) {
                        sbf.append("\n");
                    }
                    sbf.append(returnJson.getDataList().getJSONObject(i).get("group_id") + ":" + returnJson.getDataList().getJSONObject(i).get("group_name"));
                }
                response.reply(sbf.toString());
            } else
                response.reply("retCode:" + returnJson.getRetcode());
        } catch (Exception e) {
            response.replyAt(e.getMessage());
        }
    }

    @BotMessage(messageType = MessageType.PRIVATE, filter = "startWith:!clean")
    public void clean(ApiRequest request, ApiResponse response) {
        if (request.getMessage().trim().equals("?")) {
            response.reply("image record show bface");
            return;
        }
        response.reply(ApiSystem.cleanDataDir(request.getMessage().trim()).toString());
    }

    @BotMessage(messageType = MessageType.PRIVATE, filter = "startWith:!noticeGroup")
    public void noticeGroup(ApiRequest request, ApiResponse response) {
        ReturnJson returnJson = ApiGet.getGroupList();
        if (returnJson.getRetcode() == 0) {
            for (int i = 0; i < returnJson.getDataList().size(); i++) {
                response.replyGroup(request.getMessage(), returnJson.getDataList().getJSONObject(i).getLong("group_id"));
            }
        } else
            response.reply("retCode:" + returnJson.getRetcode());
    }

    @BotMessage(messageType = MessageType.PRIVATE, filter = "startWith:!outGroup")
    public void outGroup(ApiRequest request, ApiResponse response) {
        response.reply(ApiSet.setGroupLeave(Long.valueOf(request.getMessage().trim()), false).toString());
    }
}
