package top.thevsk.start;

import com.jfinal.core.JFinalFilter;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import javax.servlet.DispatcherType;
import java.io.File;
import java.util.EnumSet;

public class JettyStart {

    public static String separator = "/";

    static Log log = Log.getLog(JettyStart.class);

    public static String getStartPath() {
        String path = JettyStart.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        if (path.contains(".jar")) {
            log.info("[读取目录] jar 环境");
            path = path.substring(0, path.lastIndexOf(separator));
        } else {
            log.info("[读取目录] 非 jar 环境");
            path = path.substring(0, path.length() - 1);
            path = path.substring(0, path.lastIndexOf(separator));
        }
        return path;
    }

    public static void main(String[] args) {
        String path = getStartPath();
        if (path == null) {
            throw new RuntimeException("jar path can't find");
        }
        path += separator + "config.properties";
        File file = new File(path);
        if (!file.exists()) {
            throw new RuntimeException("properties not find");
        }
        PropKit.use(file);
        EnumSet<DispatcherType> all = EnumSet.of(DispatcherType.ASYNC, DispatcherType.ERROR,
                DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.REQUEST);
        int port = PropKit.getInt("server.port");
        final Server server = new Server(port);
        log.info("[服务] 端口 " + port);
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