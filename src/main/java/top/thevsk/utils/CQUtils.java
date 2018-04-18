package top.thevsk.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CQUtils {

    private static String base(String function, String keys[], Object... values) {
        if (keys.length != values.length) return null;
        String cq = "[CQ:" + function;
        if (NullUtils.isNull(keys, values)) return cq + "]";
        StringBuffer sbf = new StringBuffer();
        for (int i = 0; i < keys.length; i++) {
            if (i != 0) {
                sbf.append(",");
            }
            sbf.append(keys[i]);
            sbf.append("=");
            sbf.append(values[i]);
        }
        return cq + "," + sbf.toString() + "]";
    }

    public static String face(String id) {
        return base("face", new String[]{"id"}, id);
    }

    public static String emoji(String id) {
        return base("emoji", new String[]{"id"}, id);
    }

    public static String bface(String id) {
        return base("bface", new String[]{"id"}, id);
    }

    public static String sface(String id) {
        return base("sface", new String[]{"id"}, id);
    }

    public static String image(String file) {
        return base("image", new String[]{"file"}, file);
    }

    public static String imageNoCache(String file) {
        return base("image", new String[]{"file", "cache"}, file, 0);
    }

    public static String record(String file) {
        return base("record", new String[]{"file"}, file);
    }

    public static String recordNoCache(String file) {
        return base("record", new String[]{"file", "cache"}, file, 0);
    }

    public static String at(Long userId) {
        return base("at", new String[]{"qq"}, userId);
    }

    public static String rps() {
        return base("rps", null);
    }

    public static String dice() {
        return base("dice", null);
    }

    public static String shake() {
        return base("shake", null);
    }

    public static String anonymous(boolean ignore) {
        return base("anonymous", new String[]{"ignore"}, ignore);
    }

    public static String music(String type, String id) {
        return base("music", new String[]{"type", "id"}, type, id);
    }

    public static String music(String url, String audio, String title, String content, String image) {
        return base("music", new String[]{"type", "url", "audio", "title", "content", "image"}, "custom", url, audio, title, content, image);
    }

    public static String share(String url, String title, String content, String image) {
        return base("share", new String[]{"url", "title", "content", "image"}, url, title, content, image);
    }

    /**
     * 从信息中抽取[CQ:at,qq={qq}]里的qq号返回列表
     *
     * @param message
     * @return
     */
    public static String[] getUserIdInCqAtMessage(String message) {
        List<String> list = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\[CQ:at,qq=(\\d+)]");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            list.add(matcher.group(1));
        }
        return list.toArray(new String[]{});
    }

    public static String[] getUrlInCqImage(String message) {
        List<String> list = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\[CQ:image,file=.+,url=(.+)]");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            list.add(matcher.group(1));
        }
        return list.toArray(new String[]{});
    }
}