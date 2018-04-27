package top.thevsk.services;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import top.thevsk.annotation.BotMessage;
import top.thevsk.annotation.BotService;
import top.thevsk.entity.ApiRequest;
import top.thevsk.entity.ApiResponse;
import top.thevsk.enums.MessageType;
import top.thevsk.start.JettyStart;
import top.thevsk.utils.CQUtils;
import top.thevsk.utils.MessageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@BotService
public class BlShipService {

    private static final String wikiUrl = "http://wiki.joyme.com/blhx/";

    private static final String xpathUrl = JettyStart.getStartPath() + JettyStart.separator + "resources" + JettyStart.separator + "blShipXpath.properties";

    private static Prop prop = null;

    private String readProp(String key) throws Exception {
        if (StrKit.isBlank(key)) throw new Exception("key is null");
        if (prop == null) {
            loadProp();
        }
        return prop.get(key);
    }

    private String readPropEx(String key) throws Exception {
        String value = readProp(key);
        if (StrKit.isBlank(value)) throw new Exception("prop key " + key + " is null");
        return value;
    }

    private void updateProp(String key, String value) throws Exception {
        if (prop == null) {
            throw new Exception("properties is null");
        }
        prop.getProperties().setProperty(key, value);
        FileOutputStream fileOutputStream = new FileOutputStream(xpathUrl);
        prop.getProperties().store(fileOutputStream, null);
        fileOutputStream.close();
    }

    private void loadProp() {
        File file = new File(xpathUrl);
        if (!file.exists()) {
            throw new RuntimeException("properties not find");
        }
        try {
            prop = PropKit.use(file);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @BotMessage(filter = "eq:!loadProp")
    public void loadProp(ApiRequest request, ApiResponse response) {
        try {
            loadProp();
            response.reply("ok");
        } catch (Exception e) {
            response.reply(e.getMessage());
        }
    }

    @BotMessage(filter = "startWith:!readProp")
    public void readProp(ApiRequest request, ApiResponse response) {
        try {
            String value = readProp(request.getMessage().trim());
            if (StrKit.isBlank(value)) {
                response.reply("null");
                return;
            }
            response.reply(value);
        } catch (Exception e) {
            response.reply(e.getMessage());
        }
    }

    @BotMessage(filter = "startWith:!editProp")
    public void editProp(ApiRequest request, ApiResponse response) {
        try {
            Map<String, String> map = MessageUtils.parseMap(request.getMessage());
            updateProp(MessageUtils.getOrEx(map, "key"), MessageUtils.getOrEx(map, "value"));
            response.reply("ok");
        } catch (Exception e) {
            response.reply(e.getMessage());
        }
    }

    @BotMessage(messageType = MessageType.GROUP, filter = "groupId:615524453|startWith:-,—")
    public void search(ApiRequest request, ApiResponse response) {
        try {
            response.reply(search(request.getMessage().trim()));
        } catch (Exception e) {
            response.replyAt(e.getMessage());
        }
    }

    private String search(String name) throws Exception {
        Document document = loadPageSoup(name);
        if (document == null) {
            return "404";
        }
        String type = parseType(document);
        if (type == null) {
            return "no type";
        }
        Map<String, String> map = parsePage(document, type);
        if (map == null) {
            return "parse page error, type is " + type;
        }
        String result = parseMap(map, type);
        if (result == null) {
            return "parse map error, type is " + type;
        }
        return result;
    }

    private String parseMap(Map<String, String> map, String type) throws Exception {
        String render = readPropEx(type + ".template");
        for (String key : map.keySet()) {
            render = render.replace("${" + key + "}", map.get(key));
            render = render.replace("${IMG" + key + "}", CQUtils.image(map.get(key)));
        }
        return render;
    }

    private Map<String, String> parsePage(Document document, String type) throws Exception {
        String[] reg = readPropEx(type).split(" ");
        Map<String, String> map = new HashMap<>();
        for (String reg0 : reg) {
            String regEx = readPropEx(reg0);
            try {
                map.put(reg0, document.select(regEx).text());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    private String parseType(Document document) throws Exception {
        String[] types = readPropEx("types").split(" ");
        for (String type : types) {
            String reg = readPropEx(type + ".reg");
            String regV = readPropEx(type + ".reg.value");
            try {
                String v = document.select(reg).text();
                if (v != null && v.contains(regV)) {
                    return type;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Document loadPageSoup(String name) {
        try {
            return Jsoup.connect(wikiUrl + name).get();
        } catch (IOException e) {
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            Document document = Jsoup.connect(wikiUrl + "1-1").get();
            System.out.println(document.select("table.wikitable:eq(0) tr:eq(9) td").text());
            System.out.println(document.select("table.table-DropList").text());
        } catch (IOException e) {
        }
    }
}