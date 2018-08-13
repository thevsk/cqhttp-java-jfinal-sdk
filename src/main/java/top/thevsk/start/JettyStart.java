package top.thevsk.start;

import com.jfinal.core.JFinalFilter;
import com.jfinal.kit.LogKit;
import com.jfinal.kit.PropKit;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import javax.servlet.DispatcherType;
import java.io.File;
import java.util.EnumSet;

public class JettyStart {

    public static String separator = "/";

    public static boolean isJar() {
        return JettyStart.class.getProtectionDomain().getCodeSource().getLocation().getFile().contains(".jar");
    }

    public static String getStartPath() {
        String path = JettyStart.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        if (isJar()) {
            LogKit.info("[读取目录] jar 环境");
            path = path.substring(0, path.lastIndexOf(separator));
        } else {
            LogKit.info("[读取目录] 非 jar 环境");
            path = path.substring(0, path.length() - 1);
            path = path.substring(0, path.lastIndexOf(separator));
        }
        return path;
    }

    public static void main(String[] args) {
        String path = getStartPath();
        if (path == null) {
            throw new RuntimeException("jar path no find");
        }
        path += separator + "config.properties";
        File file = new File(path);
        if (!file.exists()) {
            if (isJar()) throw new RuntimeException("properties no find");
            PropKit.use("config.properties");
        } else {
            PropKit.use(file);
        }
        EnumSet<DispatcherType> all = EnumSet.of(DispatcherType.ASYNC, DispatcherType.ERROR,
                DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.REQUEST);
        int port = PropKit.getInt("server.port");
        final Server server = new Server(port);
        LogKit.info("[服务] 端口 " + port);
        try {
            WebAppContext context = new WebAppContext("/", "/");
            FilterHolder filter = new FilterHolder(new JFinalFilter());
            filter.setInitParameter("configClass", PropKit.get("config.class.path"));
            context.addFilter(filter, "/*", all);
            server.setHandler(context);
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}