package top.thevsk.utils;

import java.util.HashMap;
import java.util.Map;

public class MessageUtils {
    public static Map<String, String> parseMap(String message) throws Exception {
        try {
            Map<String, String> map = new HashMap<>();
            String[] str = message.trim().split(" ");
            for (int i = 0; i < str.length; i++) {
                String m = str[i];
                map.put(m.substring(0, m.indexOf(":")), m.substring(m.indexOf(":") + 1, m.length()));
            }
            return map;
        } catch (Exception e) {
            throw new Exception("parse map error");
        }
    }

    public static String getOrEx(Map<String, String> map, String key) throws Exception {
        if (map == null) throw new Exception("empty map");
        if (map.get(key) == null) throw new Exception("key " + key + " is null");
        return map.get(key).replace("+", " ");
    }
}
