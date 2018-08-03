package top.thevsk.annotation;

import top.thevsk.interceptor.interfaces.BotServiceInterceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author thevsk
 * @Title: BotServiceAop
 * @ProjectName cqhttp-java-jfinal-sdk
 * @date 2018-08-03 14:37
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface BotServiceAop {

    Class<? extends BotServiceInterceptor>[] value();
}
