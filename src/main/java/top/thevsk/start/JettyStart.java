package top.thevsk.start;

import com.jfinal.core.JFinalFilter;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import top.thevsk.entity.Constants;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class JettyStart {

    static Log log = Log.getLog(JettyStart.class);

    public static void main(String[] args) {
        PropKit.use("config.properties");
        EnumSet<DispatcherType> all = EnumSet.of(DispatcherType.ASYNC, DispatcherType.ERROR,
                DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.REQUEST);
        int port = Constants.getCfgInteger("server.port");
        final Server server = new Server(port);
        log.info("sever port:" + port);
        try {
            WebAppContext context = new WebAppContext("/", "/");
            FilterHolder filter = new FilterHolder(new JFinalFilter());
            filter.setInitParameter("configClass", Constants.getCfg("config.class.path"));
            context.addFilter(filter, "/*", all);
            server.setHandler(context);
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
