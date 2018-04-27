package top.thevsk.services;

import com.alibaba.fastjson.JSON;
import com.jfinal.kit.StrKit;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import top.thevsk.annotation.BotMessage;
import top.thevsk.annotation.BotService;
import top.thevsk.entity.ApiRequest;
import top.thevsk.entity.ApiResponse;
import top.thevsk.entity.Constants;
import top.thevsk.enums.MessageType;
import top.thevsk.start.JettyStart;
import top.thevsk.utils.CQUtils;
import top.thevsk.utils.MessageUtils;
import top.thevsk.utils.SQLiteUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@BotService
public class BlShipService {

    private static final String wikiUrl = "http://wiki.joyme.com/blhx/";

    private static final String dbUrl = JettyStart.getStartPath() + JettyStart.separator + "database" + JettyStart.separator + "blShip.db";

    private static SQLiteUtils db = null;

    private static boolean debugger = false;

    private String read(String key) throws Exception {
        if (StrKit.isBlank(key)) throw new Exception("key is null");
        if (db == null) {
            initDb();
        }
        Map map = db.executeQueryMap("select * from t_main where key = '" + key + "';");
        if (map == null) return null;
        Object value = map.get("value");
        return value == null ? null : value.toString();
    }

    private String readEx(String key) throws Exception {
        String value = read(key);
        if (StrKit.isBlank(value)) throw new Exception("db key " + key + " is null");
        return value;
    }

    private void update(String key, String value) throws Exception {
        if (db == null) {
            initDb();
        }
        if (read(key) == null) {
            db.execute("insert into t_main (key, value) values ('" + key + "','" + value + "');");
        } else {
            db.executeUpdate("update t_main set value = '" + value + "' where key = '" + key + "';");
        }
    }

    private void initDb() {
        try {
            db = new SQLiteUtils(dbUrl);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        try {
            db.createTable("t_main", new ArrayList<SQLiteUtils.TableColumn>() {
                {
                    add(new SQLiteUtils.TableColumn("key"));
                    add(new SQLiteUtils.TableColumn("value"));
                }
            });
        } catch (Exception e) {
            Constants.doNothing();
        }
    }

    @BotMessage(filter = "eq:!initDb")
    public void initDb(ApiRequest request, ApiResponse response) {
        try {
            initDb();
            response.reply("ok");
        } catch (Exception e) {
            response.reply(e.getMessage());
        }
    }

    @BotMessage(filter = "startWith:!readDb")
    public void readDb(ApiRequest request, ApiResponse response) {
        try {
            String value = read(request.getMessage().trim());
            if (StrKit.isBlank(value)) {
                response.reply("null");
                return;
            }
            response.reply(value);
        } catch (Exception e) {
            response.reply(e.getMessage());
        }
    }

    @BotMessage(filter = "startWith:!editDb")
    public void editDb(ApiRequest request, ApiResponse response) {
        try {
            Map<String, String> map = MessageUtils.parseMap(request.getMessage());
            update(MessageUtils.getOrEx(map, "key"), MessageUtils.getOrEx(map, "value"));
            response.reply("ok");
        } catch (Exception e) {
            response.reply(e.getMessage());
        }
    }

    @BotMessage(filter = "eq:!debugger")
    public void debugger(ApiRequest request, ApiResponse response) {
        try {
            if (debugger) {
                debugger = false;
                response.reply("close");
                return;
            }
            debugger = true;
            response.reply("open");
        } catch (Exception e) {
            response.reply(e.getMessage());
        }
    }

    @BotMessage(messageType = MessageType.GROUP, filter = "groupId:615524453|startWith:-,—")
    public void search(ApiRequest request, ApiResponse response) {
        try {
            response.reply(search(request.getMessage().trim(), response));
        } catch (Exception e) {
            response.replyAt(e.getMessage());
        }
    }

    private String search(String name, ApiResponse response) throws Exception {
        Document document = loadPageSoup(name, response);
        if (document == null) {
            return "404";
        }
        String type = parseType(document, response);
        if (type == null) {
            return "no type";
        }
        Map<String, String> map = parsePage(document, type, response);
        if (map == null) {
            return "parse page error, type is " + type;
        }
        String result = parseMap(map, type, response);
        if (result == null) {
            return "parse map error, type is " + type;
        }
        return result;
    }

    private String parseMap(Map<String, String> map, String type, ApiResponse response) throws Exception {
        String render = readEx(type + ".template");
        if (debugger) {
            response.reply("套用模板：" + render);
        }
        for (String key : map.keySet()) {
            if (key.contains("IMG")) {
                render = render.replace("${" + key + "}", CQUtils.image(map.get(key)));
            } else {
                render = render.replace("${" + key + "}", map.get(key));
            }
            if (debugger) {
                response.reply("替换：" + "${" + key + "}");
            }
        }
        if (debugger) {
            response.reply("展示数据生成完毕");
        }
        return render;
    }

    private Map<String, String> parsePage(Document document, String type, ApiResponse response) throws Exception {
        String[] reg = readEx(type).split(",");
        if (debugger) {
            response.reply("开始解析页面");
        }
        Map<String, String> map = new HashMap<>();
        for (String reg0 : reg) {
            String regEx = readEx(reg0);
            if (debugger) {
                response.reply("解析：" + reg0);
                response.reply("解析规则：" + regEx);
            }
            try {
                if (reg0.contains("IMG")) {
                    map.put(reg0, document.select(regEx).attr("src"));
                    if (debugger) {
                        response.reply("解析到了图片：" + map.get(reg0));
                    }
                } else {
                    map.put(reg0, document.select(regEx).text());
                    if (debugger) {
                        response.reply("解析到了文本：" + map.get(reg0));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (debugger) {
            response.reply("解析结果：" + JSON.toJSONString(map));
        }
        return map;
    }

    private String parseType(Document document, ApiResponse response) throws Exception {
        String[] types = readEx("types").split(",");
        if (debugger) {
            response.reply("解析数据的类型，已有的类型：" + StrKit.join(types, ","));
        }
        for (String type : types) {
            String reg = readEx(type + ".reg");
            String regV = readEx(type + ".reg.value");
            if (debugger) {
                response.reply("解析目标：" + reg);
                response.reply("理想的值：" + regV);
            }
            try {
                String v = document.select(reg).text();
                if (debugger) {
                    response.reply("解析到的值：" + v);
                }
                if (v != null && v.contains(regV)) {
                    if (debugger) {
                        response.reply("解析成功，解析出类型：" + type);
                    }
                    return type;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (debugger) {
            response.reply("解析失败，没有解析出数据的类型");
        }
        return null;
    }

    private Document loadPageSoup(String name, ApiResponse response) {
        try {
            if (debugger) {
                response.reply("爬虫发起，目标页面：" + wikiUrl + name);
            }
            return Jsoup.connect(wikiUrl + name).get();
        } catch (IOException e) {
            if (debugger) {
                response.reply("爬取数据失败，错误：" + e.getMessage());
            }
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            Document document = Jsoup.connect(wikiUrl + "新月JP").get();
            System.out.println(document.select(".jntj > .wikitable:eq(0) tr:eq(2) td:eq(1)").outerHtml());
        } catch (IOException e) {
            Constants.doNothing();
        }
    }
}