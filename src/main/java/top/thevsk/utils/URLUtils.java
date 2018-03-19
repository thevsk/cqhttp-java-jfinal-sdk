package top.thevsk.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class URLUtils {

    public URLUtils() {
    }

    public static URL fromFile(String file) {
        return fromFile(new File(file));
    }

    public static URL fromFile(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException var2) {
            throw new RuntimeException(var2);
        }
    }

    public static File toFileObject(URL url) {
        return url == null ? null : new File(toFilePath(url));
    }

    public static String toFilePath(URL url) {
        if (url == null) {
            return null;
        } else {
            String protocol = url.getProtocol();
            String file = url.getPath();

            try {
                file = URLDecoder.decode(file, "utf-8");
            } catch (UnsupportedEncodingException var4) {
                ;
            }

            if ("file".equals(protocol)) {
                return file;
            } else {
                int ipos;
                if (!"jar".equals(protocol) && !"zip".equals(protocol)) {
                    if ("vfs".equals(protocol)) {
                        ipos = file.indexOf("!/");
                        if (ipos > 0) {
                            file = file.substring(0, ipos);
                        } else if (file.endsWith("/")) {
                            file = file.substring(0, file.length() - 1);
                        }

                        return file;
                    } else {
                        return file;
                    }
                } else {
                    ipos = file.indexOf("!/");
                    if (ipos > 0) {
                        file = file.substring(0, ipos);
                    }

                    if (file.startsWith("file:")) {
                        file = file.substring("file:".length());
                    }

                    return file;
                }
            }
        }
    }
}
