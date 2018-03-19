package top.thevsk.start;

import com.jfinal.core.JFinalFilter;
import com.jfinal.kit.PropKit;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class JettyStart {
    public static void main(String[] args) {
        PropKit.use("config.properties");
        EnumSet<DispatcherType> all = EnumSet.of(DispatcherType.ASYNC, DispatcherType.ERROR,
                DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.REQUEST);
        int port = PropKit.getInt("server.port");
        final Server server = new Server(port);
        System.out.println("sever port:" + port);
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
