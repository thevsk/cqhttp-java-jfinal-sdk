package top.thevsk.utils;

import com.jfinal.kit.StrKit;

public class NullUtils {

    public static boolean isNull(Object... objects) {
        if (objects == null) {
            return true;
        }
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNullOrBlank(Object... objects) {
        if (objects == null) {
            return true;
        }
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] == null) {
                return true;
            }
            if (objects[i] instanceof String) {
                boolean flag = StrKit.isBlank(objects[i].toString());
                if (flag) return true;
            }
        }
        return false;
    }

    public static boolean isNotNullOrBlank(Object... objects) {
        return !isNullOrBlank(objects);
    }

    public static boolean isNotNull(Object... objects) {
        return !isNull(objects);
    }
}
