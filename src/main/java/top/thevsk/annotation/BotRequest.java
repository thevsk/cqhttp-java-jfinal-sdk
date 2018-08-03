package top.thevsk.annotation;

import top.thevsk.enums.RequestType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface BotRequest {
    RequestType requestType() default RequestType.DEFAULT;
}