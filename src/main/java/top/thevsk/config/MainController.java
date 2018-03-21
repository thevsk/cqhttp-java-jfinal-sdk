package top.thevsk.config;

import com.jfinal.core.Controller;
import top.thevsk.enums.MessageType;
import top.thevsk.plugins.BotServiceKit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class MainController extends Controller {

    public void index() {
        try {
            String str = getPara();
            System.out.println("\nmessage type = " + str);
            List<Method> default_ = BotServiceKit.getBotMessageMethods(MessageType.DEFAULT);
            for (Method method : default_) {
                method.invoke(BotServiceKit.getClassInstanceByMethod(method), null, null);
            }
            List<Method> methods = null;
            if (MessageType.GROUP.getCode().equals(str)) {
                methods = BotServiceKit.getBotMessageMethods(MessageType.GROUP);
            } else if (MessageType.PRIVATE.getCode().equals(str)) {
                methods = BotServiceKit.getBotMessageMethods(MessageType.PRIVATE);
            } else {
                methods = BotServiceKit.getBotMessageMethods(MessageType.DISCUSS);
            }
            for (Method method : methods) {
                method.invoke(BotServiceKit.getClassInstanceByMethod(method), null, null);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        renderNull();
    }
}
