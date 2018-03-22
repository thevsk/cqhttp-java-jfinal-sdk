package top.thevsk.entity;

import com.jfinal.kit.PropKit;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    private static Map<String, String> cfgMap = new HashMap<>();

    public static String getCfg(String key) {
        String value = Constants.cfgMap.get(key);
        if (value == null) {
            value = PropKit.get(key);
            cfgMap.put(key, value);
        }
        return value;
    }

    public static Integer getCfgInteger(String key, Integer defaultValue) {
        String value = getCfg(key);
        if (value != null) {
            return Integer.valueOf(value.trim());
        }
        return defaultValue;
    }

    public static Integer getCfgInteger(String key) {
        return getCfgInteger(key, null);
    }

    public static Boolean getCfgBoolean(String key, Boolean defaultValue) {
        String value = getCfg(key);
        if (value != null) {
            if (value.toUpperCase().equals("TRUE")) {
                return true;
            } else if (value.toUpperCase().equals("FALSE")) {
                return false;
            } else {
                throw new RuntimeException();
            }
        }
        return defaultValue;
    }
}
